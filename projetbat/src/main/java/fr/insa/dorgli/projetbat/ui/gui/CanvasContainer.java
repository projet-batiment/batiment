package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Drawable;
import fr.insa.dorgli.projetbat.objects.DrawableLine;
import fr.insa.dorgli.projetbat.objects.DrawablePoint;
import fr.insa.dorgli.projetbat.objects.Niveau;
import fr.insa.dorgli.projetbat.objects.Point;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.awt.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashSet;

public class CanvasContainer extends Pane {
	private final Config config;

	private final Canvas canvas;
	private final GraphicsContext ctxt;
	private final DrawingContext drawingContext;
	private final HashSet<DrawableLine> drawnLines;
	private final HashMap<Path2D.Double, Drawable> drawnPolygons;
	private final HashSet<DrawablePoint> drawnPoints;

	private Niveau currentNiveau;
	private Rectangle totalDrawingRectangle;

	private final double zoomFactor;
	private final double moveFactor;
	private double scaledMoveFactor;
	private boolean isFitted;

	private boolean disableDrawing = true;

	private final int pointRadius = 5;

	public CanvasContainer(Config config, MainWindow mainWindow) {
		this.config = config;

		canvas = new Canvas(this.getWidth(), this.getHeight());
		canvas.widthProperty().bind(this.widthProperty());
		canvas.widthProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: widht has changed, recsaling and redrawing");
			scaleMoveFactor();
			if (isFitted)
				moveView(Direction.FIT);
			else
				redraw();
		});
		canvas.heightProperty().bind(this.heightProperty());
		canvas.heightProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: height has changed, recsaling and redrawing");
			scaleMoveFactor();
			if (isFitted)
				moveView(Direction.FIT);
			else
				redraw();
		});

		canvas.setOnMouseClicked(eh -> {
			mainWindow.getController().canvasClicked(eh);
		});
		canvas.setOnMouseMoved(eh -> {
			//mainWindow.getController().canvasClicked(eh);
		});

 		ctxt = canvas.getGraphicsContext2D();
		totalDrawingRectangle = new Rectangle( (int)Math.ceil(super.getWidth()), (int)Math.ceil(super.getHeight()) );

		drawnLines = new HashSet<>();
		drawnPolygons = new HashMap<>();
		drawnPoints = new HashSet<>();

		drawingContext = new DrawingContext(config, this);
		drawingContext.setSelectedObject(currentNiveau); // TODO!! TMP !!

		zoomFactor = Math.sqrt(2);
		moveFactor = 20;
		disableDrawing = false;

		super.getChildren().add(canvas);
	}

	//////////////// scaling stuff

	// Renvoie dans les dimensions du canvas l'équivalent d'une dimension dans "la vue" (d'unité arbitraire, à zoom=1)
	private double scaleToView(double incoming) {
		return scaleToView(ctxt.getTransform(), incoming);
	}
	private double scaleToView(Affine affine, double incoming) {
		// Mxx est un coef de la matrice de transformation du canvas, donc en partie de son zoom
		// Comme le zoom est orthonormal, mxx=myy=mzz, donc on choisit mxx arbitrairement
		return incoming / affine.getMxx();
	}
	private double scaleFromView(double incoming) {
		// Mxx est un coef de la matrice de transformation du canvas, donc en partie de son zoom
		// Comme le zoom est orthonormal, mxx=myy=mzz, donc on choisit mxx arbitrairement
		return incoming * ctxt.getTransform().getMxx();
	}

	// moveFactor définit un pas fixe qui ne s'adapte pas au zoom
	// scaledMoveFactor définit la taille réelle du pas fixe ramené au zoom actuel
	// scaleMoveFactor() permet de calculer scaledMoveFactor après un changement de zoom
	private void scaleMoveFactor() {
 		scaledMoveFactor = scaleToView(moveFactor);
		config.tui.debug("canvasContainer/scaleMoveFactor: new: " + moveFactor + " scaled -> " + scaledMoveFactor);
	}

	// convertit les distances de la partie "structure" en distances sur le canvas
	// actuellement (première norme) : (double) 1 mètre <=> (double) 100 pointsCanvas
	public double dataToCanvasUnit(double dataUnit) {
		return 100 * dataUnit;
	}

	//////////////// zoom and movement stuff

	private Affine zoomWithScale(double scale) {
		return zoomWithScale(scale, true);
	}
	private Affine zoomWithScale(double scale, boolean setIt) {
		config.tui.debug("zoomWithScale: applying zoom with scale " + scale);

		Affine affine = ctxt.getTransform();
		double ourCenterX = scaleToView(super.getWidth() / 2 - affine.getTx());
		double ourCenterY = scaleToView(super.getHeight() / 2 - affine.getTy());
		affine.appendScale(scale, scale, ourCenterX, ourCenterY);

		// only apply if ask to do so
		if (setIt)
			ctxt.setTransform(affine);

		// rescale every buffered scaling value
		scaleMoveFactor();
		return affine;
	}

	public void moveView(Direction direction) {
		config.tui.diveWhere("canvasContainer/moveView");

		clear();
		isFitted = false;

		switch (direction) {
			// TODO: mauvaise adaptation lors d'un changement de taille de fenêtre (bugs visuels lors de redraw, formes étirées
			//       => à cause du bind des Properties width et height

			case Direction.LEFT -> ctxt.translate(- scaledMoveFactor, 0);
			case Direction.RIGHT -> ctxt.translate(scaledMoveFactor, 0);
			case Direction.UP -> ctxt.translate(0, - scaledMoveFactor);
			case Direction.DOWN -> ctxt.translate(0, scaledMoveFactor);

			case Direction.FORWARDS -> zoomWithScale(zoomFactor);
			case Direction.BACKWARDS -> zoomWithScale(1 / zoomFactor);

			case Direction.RESET -> {
				Affine affine = zoomWithScale(1 / ctxt.getTransform().getMxx(), false);
				affine.setTx(0);
				affine.setTy(0);
				ctxt.setTransform(affine);
			}

			case Direction.FIT -> {
				// on recalcule le totalDrawingRectangle
				recalculateTotalDrawingRectangle();
				config.tui.diveWhere("zoom FIT");

				isFitted = true;

				// on calcul le zoom à appliquer
				// totalDrawingRectangle ne peut techniquement jamais avoir des dimensions nulles
				// donc on omet le contrôle d'une division par zéro
				double fitScaleX = scaleToView(super.getWidth()) / totalDrawingRectangle.width;
				double fitScaleY = scaleToView(super.getHeight()) / totalDrawingRectangle.height;

				// on applique le zoom voulu suivant si les fitScales présentent x<y ou x>y (cas x==y trivial)
				double newRelativeScale = fitScaleY; // relative => par rapport au zoom actuel
				if (Math.ceil(scaleToView(super.getWidth()) * 100 / totalDrawingRectangle.width) / 100 < Math.ceil(scaleToView(super.getHeight()) * 100 / totalDrawingRectangle.height) / 100) {
					newRelativeScale = fitScaleX;
				}
				Affine affine = zoomWithScale(newRelativeScale);

				// on ajoute une translation qui ramène le canvas au bon endroit (suivant le rectangle avec un nom trop long)
				double newAbsoluteScale = affine.getMxx(); // absolute => par rapport au parent
				affine.setTx(Math.max(0, super.getWidth() - totalDrawingRectangle.width * newAbsoluteScale) / 2 - scaleFromView(totalDrawingRectangle.x));
				affine.setTy(Math.max(0, super.getHeight() - totalDrawingRectangle.height * newAbsoluteScale) / 2 - scaleFromView(totalDrawingRectangle.y));

				// et on applique la nouvelle transformation !
				ctxt.setTransform(affine);

				// deux trois debugs qui ont été utiles et qui le seront peut être encore
				config.tui.debug("fitScaleX: " + scaleToView(super.getWidth()) + " / " + totalDrawingRectangle.width);
				config.tui.debug("fitScaleY: " + scaleToView(super.getHeight()) + " / " + totalDrawingRectangle.height);
				config.tui.debug("scales " + fitScaleX + ", " + fitScaleY);
				config.tui.debug("x " + super.getWidth() + ", " + totalDrawingRectangle.width * newAbsoluteScale + ", " + (Math.max(0, super.getWidth() - totalDrawingRectangle.width * newAbsoluteScale) / 2 - scaleFromView(totalDrawingRectangle.x)));
				config.tui.debug("y " + super.getHeight() + ", " + totalDrawingRectangle.height * newAbsoluteScale + ", " + (Math.max(0, super.getHeight() - totalDrawingRectangle.height * newAbsoluteScale) / 2 - scaleFromView(totalDrawingRectangle.y)));

				config.tui.popWhere();
			}

			default -> config.tui.error("bad direction: " + direction);
		}

		config.tui.debug("triggering redraw");
		redraw();
		config.tui.popWhere();
	}

	//////////////// fit stuff

	private void fitArea(Rectangle pointArea) {
		config.tui.diveWhere("canvasContainer/fitArea");

		if (! totalDrawingRectangle.contains(pointArea)) {
			config.tui.debug("Recalculating the totalDrawingRectangle: old " + totalDrawingRectangle.toString());
			totalDrawingRectangle.add(pointArea);
			config.tui.debug("Recalculated the totalDrawingRectangle:  new " + totalDrawingRectangle.toString());
		} else {
			config.tui.debug("The totalDrawingRectangle already fits the pointArea: " + totalDrawingRectangle.toString());
		}

		config.tui.popWhere();
	}

	private void fitPoint(Point2D.Double point) {
		fitPoint(point.getX(), point.getY(), pointRadius);
	}
	private void fitPoint(double x, double y) {
		fitPoint(x, y, pointRadius);
	}
	private void fitPoint(double x, double y, int radius) {
		config.tui.diveWhere("canvasContainer/fitPoint");

		int topLeftX = (int)Math.floor(x - radius);
		int topLeftY = (int)Math.floor(y - radius);
		int diameter = 2 * radius + 2; // +2 kind of catches the double.floor-s

		Rectangle pointArea = new Rectangle(topLeftX, topLeftY, diameter, diameter);
		config.tui.debug("Point (" + x + ":" + y + "), radius " + radius + ", lives in pointArea: " + pointArea);

		fitArea(pointArea);
		config.tui.popWhere();
	}

	//////////////// linking stuff

	public Drawable getClosestLinked(double clickX, double clickY) {
		return getClosestLinked(clickX, clickY, scaleToView(15));
	}

	public Drawable getClosestLinked(double clickX, double clickY, double closestMaxDistance) {
		config.tui.log("canvasContainer/getClosestLinked: x: " + scaleToView(clickX) + ", " + scaleToView(ctxt.getTransform().getTx()));
		config.tui.log("canvasContainer/getClosestLinked: y: " + scaleToView(clickY) + ", " + scaleToView(ctxt.getTransform().getTy()));
		clickX = scaleToView(clickX - ctxt.getTransform().getTx());
		clickY = scaleToView(clickY - ctxt.getTransform().getTy());
		config.tui.log("canvasContainer/getClosestLinked: scaled and translated coordinates: (" + clickX + ":" + clickY + ")");

		double closestDistance = closestMaxDistance;
		double currentDistance;

//		Point2D.Double closestPoint = null;
//
//		// on cherche parmi les points
//		for (Point2D.Double currentPoint: drawnPoints) {
//			currentDistance = currentPoint.distance(clickX, clickY);
//			config.tui.debug("canvasContainer/getClosestLinked: closest " + (closestPoint == null ? null : closestPoint) + "(" + closestDistance + ")");
//			config.tui.debug("canvasContainer/getClosestLinked: current "
//			    + currentPoint + "(" + currentDistance + ")" 
//			    + " at (" + currentPoint.getX() + ":" + currentPoint.getY()+ ")");
//
//			if (currentDistance < closestDistance) {
//				config.tui.debug("canvasContainer/getClosestLinked: this is closer");
//				closestDistance = currentDistance;
//				closestPoint = currentPoint;
//			}
//		}

//		// si on a un points assez proche, on cherche le BObject qui le représente le renvoie
//		if (closestPoint != null) {
//			// TODO: rootObject != Niveau
//			Point template = new Point(-1, scaleFromView(closestPoint.getX()), scaleFromView(closestPoint.getY()), (Niveau) drawingContext.getRootObject());
//			for (Point bp: config.project.objects.points.values()) {
//				if (
//					template.getX() - bp.getX() <= pointRadius * 2
//					&& template.getY() - bp.getY() <= pointRadius * 2
//					&& template.getNiveau() == bp.getNiveau()
//				) {
//					config.tui.debug("canvasContainer/getClosestLinked: found a close point: " + bp.toStringShort());
//					return (Drawable) bp;
//				}
//			}
//			config.tui.error("canvasContainer/getClosestLinked: could not find equivalent point: " + template);
//		}

		// on cherche parmi les points
		DrawablePoint closestPointObject = null;

		for (DrawablePoint currentPointObject: drawnPoints) {
			currentDistance = currentPointObject.getPointCanvas().distance(clickX, clickY);
			config.tui.debug("canvasContainer/getClosestLinked: closest " + (closestPointObject == null ? null : closestPointObject.getId()) + "(" + closestDistance + ")");
			config.tui.debug("canvasContainer/getClosestLinked: current "
			    + currentPointObject.getId() + "(" + currentDistance + ")" 
			    + " at (" + currentPointObject.getPointCanvas().getX() + ":" + currentPointObject.getPointCanvas().getY() + ")");

			if (currentDistance < closestDistance) {
				config.tui.debug("canvasContainer/getClosestLinked: this is closer");
				closestDistance = currentDistance;
				closestPointObject = currentPointObject;
			}
		}

		// si on a un point assez proche, on le renvoie
		if (closestPointObject != null) {
			config.tui.debug("canvasContainer/getClosestLinked: found a close point: " + closestPointObject.toStringShort());
			return (Drawable) closestPointObject;
		}

		// on cherche parmi les lignes
		DrawableLine closestLineObject = null;

		for (DrawableLine currentLineObject: drawnLines) {
			currentDistance = currentLineObject.getLineCanvas().ptSegDist(clickX, clickY);
			config.tui.debug("canvasContainer/getClosestLinked: closest " + (closestLineObject == null ? null : closestLineObject.getId()) + "(" + closestDistance + ")");
			config.tui.debug("canvasContainer/getClosestLinked: current "
			    + currentLineObject.getId() + "(" + currentDistance + ")" 
			    + " at (" + currentLineObject.getLineCanvas().getX1() + ":" + currentLineObject.getLineCanvas().getY1()
			    + ") -- (" + currentLineObject.getLineCanvas().getX1() + ":" + currentLineObject.getLineCanvas().getY1() + ")");

			if (currentDistance < closestDistance) {
				config.tui.debug("canvasContainer/getClosestLinked: this is closer");
				closestDistance = currentDistance;
				closestLineObject = currentLineObject;
			}
		}

		// si on a une ligne assez proche, on la renvoie
		if (closestLineObject != null) {
			config.tui.debug("canvasContainer/getClosestLinked: found a close line: " + closestLineObject.toStringShort());
			return (Drawable) closestLineObject;
		}

		// on cherche parmi les path2d (polygones)
		for (HashMap.Entry<Path2D.Double, Drawable> currentEntry: drawnPolygons.entrySet()) {
			if (currentEntry.getKey().contains(clickX, clickY)) {
				config.tui.debug("canvasContainer/getClosestLinked: found a close path2d: " + currentEntry.getValue().toStringShort());
				return currentEntry.getValue();
			}
		}

		return null;
	}

	//////////////// draw stuff

	/**
	 * @param linkedObject
	 * @param point point with coordinates in DATA standards, the coordinates are then converted into CANVAS standards
	 * @param color
	 * @param important
	 */
//	public void drawPoint(DrawablePoint linkedObject, Point2D.Double point, double radius, Color color, boolean important) {
//		drawPoint(linkedObject, point, true, color, important);
//	}
	/**
	 * @param linkedObject
	 * @param point
//	 * @param convert wether to convert coordinates from DATA into CANVAS or not
	 * @param color
	 * @param important
	 */
	public void drawPoint(DrawablePoint linkedObject, Point2D.Double point, double radius, Color color, boolean important) {
//	public void drawPoint(DrawablePoint linkedObject, Point2D.Double point, boolean convert, Color color, boolean important) {
		config.tui.diveWhere("canvasContainer/drawPoint");

		Point2D.Double pointCanvas;
//		if (convert) {
//			// convertir les coordonnées DATA -> CANVAS
			pointCanvas = new Point2D.Double(dataToCanvasUnit(point.getX()), dataToCanvasUnit(point.getY()));
//		} else {
//			// copier le point puisqu'il est déjà en coordonnées CANVAS
//			pointCanvas = point;
//		}

		// ajouter le nouveau point au totalDrawingRectangle et à drawnPoints
		fitPoint(pointCanvas);
		linkedObject.setPointCanvas(pointCanvas);
		drawnPoints.add(linkedObject);

		// dessiner + log
		if (disableDrawing) {
			config.tui.debug("NODRAW - processed point with center (" + pointCanvas.getX() + ":" + pointCanvas.getY() + "), radius " + radius);
		} else if (important || config.tui.logLevelGreaterOrEqual(TUI.LogLevel.DEBUG)) {
			ctxt.setFill(color);
			ctxt.fillOval(pointCanvas.getX() - radius, pointCanvas.getY() - radius, radius*2, radius*2);
			config.tui.debug("Drew point with center (" + pointCanvas.getX() + ":" + pointCanvas.getY() + "), radius " + radius);
		} else {
			config.tui.debug("NOT IMPORTANT - processed point with center (" + pointCanvas.getX() + ":" + pointCanvas.getY() + "), radius " + radius);
		}

		config.tui.popWhere();
	}

	/**
	 * @param linkedObject Drawable object to be attached to this line
	 * @param lineData the line to draw with coordinates in DATA standards
	 * @param width width of the line in DATA standards
	 * @param color
	 */
	public void drawLine(DrawableLine linkedObject, Line2D.Double lineData, double width, Color color) {
		config.tui.diveWhere("canvasContainer/drawLine");

		double x1 = dataToCanvasUnit(lineData.getX1());
		double y1 = dataToCanvasUnit(lineData.getY1());
		double x2 = dataToCanvasUnit(lineData.getX2());
		double y2 = dataToCanvasUnit(lineData.getY2());

		Line2D.Double lineCanvas = new Line2D.Double(x1, y1, x2, y2); // in CANVAS standards
		linkedObject.setLineCanvas(lineCanvas);
		drawnLines.add(linkedObject);

		// ajouter les extrémités de la nouvelle ligne au totalDrawingRectangle
		// on prend ici width pour rayon, mais en soit tant que fit prend plus large, ç'est nickel
		fitPoint(x1, y1, (int)Math.ceil(width));
		fitPoint(x2, y2, (int)Math.ceil(width));

		// dessiner + log
		if (disableDrawing) {
			config.tui.debug("NODRAW - processed line with (" + x1 + ":" + y1 + ") -- (" + x2 + ":" + y2 + "), width " + width);
		} else {
			double savedWidth = ctxt.getLineWidth();

			ctxt.setStroke(color);
			ctxt.setLineWidth(width);
			ctxt.strokeLine(x1, y1, x2, y2);

			config.tui.debug("Drew line with (" + x1 + ":" + y1 + ") -- (" + x2 + ":" + y2 + "), width " + width);

			ctxt.setLineWidth(savedWidth);
		}

		config.tui.popWhere();
	}

	// classe uniquement pour trier des points dans le sens horaire autour d'un point central
	private class PointPolarCompare implements Comparator<Point2D.Double> {
		private final Point2D.Double center;

		public PointPolarCompare(Point2D.Double center) {
			this.center = center;
		}

		@Override
		public int compare(final Point2D.Double a, final Point2D.Double b) {
			double valueA = Math.atan2(a.getY() - center.getY(), a.getX() - center.getX());
			double valueB = Math.atan2(b.getY() - center.getY(), b.getX() - center.getX());

			if (valueA > valueB)
				return 1;
			if (valueA > valueB)
				return -1;
			else
				return 0;
		}
	}

	/**
	 * @param linkedObject Drawable object to be attached to this line
	 * @param points Points with coordinates in DATA standards
	 * @param color
	 */
	public void drawPolygon(Drawable linkedObject, Point2D.Double[] points, Color color) {
		if (points.length < 3) {
			config.tui.error("canvasContainer/drawPolygon: polygon contains less than 3 points: " + points.length);
			return;
		}

		config.tui.diveWhere("canvasContainer/drawPolygon");
		Path2D.Double path = new Path2D.Double();

		double centerX = 0;
		double centerY = 0;

		// pour chaque point:
		// - on récupère ses coordonnées en standards DATA qu'on convertit en CANVAS
		// - on l'ajoute à center pour ensuite calculer le centre du polygone
		for (Point2D.Double p: points) {
			p.setLocation(dataToCanvasUnit(p.getX()), dataToCanvasUnit(p.getY()));
			centerX += p.getX();
			centerY += p.getY();
		}

		// centre du polygone
		Point2D.Double center = new Point2D.Double(centerX/points.length, centerY/points.length);
		// trier les points par angle autour du centre
		Arrays.sort(points, new PointPolarCompare(center));

		double[] xlist = new double[points.length];
		double[] ylist = new double[points.length];

		// pour chaque point, dans le bon ordre:
		// - on l'ajoute au polygone à dessiner
		// - on le fitPoint
		// - on l'ajoute au path lié au linkedObject
		for (int i = 0; i < points.length; i++) {
			Point2D.Double p = points[i];
			double x = p.getX();
			double y = p.getY();

			xlist[i] = x;
			ylist[i] = y;

			fitPoint(x, y);

			if (i == 0)
				path.moveTo(x, y);
			else
				path.lineTo(x, y);
		}

		// ajouter le path et le linkedObject aux liens
		drawnPolygons.put(path, linkedObject);

		// dessiner + log
		if (disableDrawing) {
			config.tui.debug("NODRAW - processed polygon " + Arrays.toString(xlist) + ", " + Arrays.toString(ylist));
		} else {
			ctxt.setFill(color);
			ctxt.fillPolygon(xlist, ylist, points.length);

			config.tui.debug("Drew polygon " + Arrays.toString(xlist) + ", " + Arrays.toString(ylist) + ", " + color);
		}

		config.tui.popWhere();
	}

	//////////////// general purposal managing stuff

	public void logTotalDrawingRectangle() {
		ctxt.setStroke(Color.GREEN);
		ctxt.strokeRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/logTotalDrawingRectangle: " + totalDrawingRectangle.getMinX() + "," + totalDrawingRectangle.getMinY() + "," + totalDrawingRectangle.getWidth() + "," + totalDrawingRectangle.getHeight());
	}

	public void recalculateTotalDrawingRectangle() {
		config.tui.diveWhere("canvasContainer/recalculateTotalDrawingRectangle");
		config.tui.begin();

		totalDrawingRectangle = new Rectangle();
		boolean localDisableDrawing = disableDrawing;
		disableDrawing = true; // do not draw anything !!

		config.tui.debug("recalculateTotalDrawingRectangle: triggering redraw in order to place all the canvas-objects in that holly rectangle again");
		redraw();

		if (totalDrawingRectangle.getWidth() == 0 && totalDrawingRectangle.getHeight() == 0) {
			// the rectangle is empty => fit the canvas itself
			config.tui.debug("recalculateTotalDrawingRectangle: fitting the canvas because the rectangle is empty");
			fitPoint(0, 0);
			fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());
		}

		disableDrawing = localDisableDrawing; // restore the original disableDrawing flag

		config.tui.ended();
		config.tui.popWhere();
	}

	public void clearFancy() {
		ctxt.setFill(Color.ORANGERED);
		ctxt.fillRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/clearFancy: totalDrawingRectangle coordinates: (" + totalDrawingRectangle.getMinX() + ":" + totalDrawingRectangle.getMinY() + ") -> (" + totalDrawingRectangle.getWidth() + ":" + totalDrawingRectangle.getHeight() + ")");
	}

	public void clear() {
		ctxt.clearRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		drawnLines.clear();
		config.tui.debug("canvasContainer/clear: totalDrawingRectangle coordinates: (" + totalDrawingRectangle.getMinX() + ":" + totalDrawingRectangle.getMinY() + ") -> (" + totalDrawingRectangle.getWidth() + ":" + totalDrawingRectangle.getHeight() + ")");
	}

	public void redraw() {
		clear();
		config.tui.diveWhere("canvasContainer/redraw");
		config.tui.begin();
		config.tui.debug("graphicsContext.transform (affine): " + canvas.getGraphicsContext2D().getTransform().toString());

		if (config.tui.logLevelGreaterOrEqual(TUI.LogLevel.DEBUG) && ! disableDrawing) {
			Color c = Color.TEAL;
			config.tui.debug("drawing canvas with a " + c + " background");
			ctxt.setFill(Color.TEAL);
			ctxt.fillRect(0, 0, getWidth(), getHeight());
		}

		drawingContext.redraw();

		config.tui.ended();
		config.tui.popWhere();
	}

	public Niveau getCurrentNiveau() {
		return currentNiveau;
	}

	public DrawingContext getDrawingContext() {
		return drawingContext;
	}
}
