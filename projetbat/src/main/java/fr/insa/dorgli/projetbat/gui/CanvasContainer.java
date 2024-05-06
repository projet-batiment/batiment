package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import fr.insa.dorgli.projetbat.objects.Niveau;
import java.awt.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class CanvasContainer extends Pane {
	private Config config;
	private MainPane mainPane;

	private Canvas canvas;
	private GraphicsContext ctxt;
	private DrawingContext drawingContext;

	private Niveau currentNiveau;
	private Rectangle totalDrawingRectangle;

	private double zoomFactor;
	private double moveFactor;
	private double scaledMoveFactor;

	private boolean disableDrawing = true;

	private int pointRadius = 5;
	private Color pointColor = Color.RED;

//	private double scaleLabelValue = 1;

	public CanvasContainer(Config config, MainPane mainPane) {
		this.config = config;
		this.mainPane = mainPane;

		canvas = new Canvas(this.getWidth(), this.getHeight());
		canvas.widthProperty().bind(this.widthProperty());
		canvas.widthProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: widht has changed, recsaling and redrawing");
			rescaleBufferedScaledValues();
			redraw();
		});
		canvas.heightProperty().bind(this.heightProperty());
		canvas.heightProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: height has changed, recsaling and redrawing");
			rescaleBufferedScaledValues();
			redraw();
		});

 		ctxt = canvas.getGraphicsContext2D();
		totalDrawingRectangle = new Rectangle( (int)Math.ceil(super.getWidth()), (int)Math.ceil(super.getHeight()) );

		drawingContext = new DrawingContext(config, this);
		drawingContext.setFocusedObject(currentNiveau); // TODO!! TMP !!

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

	// Rescale after dimensions having resized
	public void rescaleBufferedScaledValues() {
		config.tui.debug("canvasContainer/rescale Etc: rescaling the buffered values and refiting the canvas within the totalDrawingRectangle");

		// first off: fit the canvas inside the totalDrawingRectangle
		fitPoint(0, 0); // origin doesn't move once it is initialized
		fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());

		// then: rescale all the buffered values
		scaleMoveFactor();

		// finally: redraw
		config.tui.debug("canvasContainer/rescale Etc: triggering a redraw");
//		redraw();
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

		// apply the changes + GUI log
		mainPane.setLabelCanvasScaleText(affine.getMxx());

		// only apply if ask to do so
		if (setIt)
			ctxt.setTransform(affine);

		// rescale every buffered scaling value
		rescaleBufferedScaledValues();
		return affine;
	}

	public void TMPDrawOriginPoint() {
		Affine affine = ctxt.getTransform();
		double ourCenterX = scaleToView(super.getWidth() / 2 - affine.getTx());
		double ourCenterY = scaleToView(super.getHeight() / 2 - affine.getTy());
		drawPoint(ourCenterX, ourCenterY, false);

		double ourMaxX = scaleToView(super.getWidth() - affine.getTx());
		double ourMaxY = scaleToView(super.getHeight() - affine.getTy());
		double ourMinX = scaleToView(- affine.getTx());
		double ourMinY = scaleToView(- affine.getTy());

		drawPoint(ourMinX, ourMinY, false);
		drawPoint(ourMaxX, ourMinY, false);
		drawPoint(ourMaxX, ourMaxY, false);
		drawPoint(ourMinX, ourMaxY, false);
	}

	public void moveView(Direction direction) {
		config.tui.diveWhere("canvasContainer/moveView");

		clear();

		switch (direction) {
			// TODO: mauvaise adaptation lors d'un changement de taille de fenêtre (bugs visuels lors de redraw, formes étirées
			//       => à cause du bind des Properties width et height

			case Direction.LEFT -> ctxt.translate(- scaledMoveFactor, 0);
			case Direction.RIGHT -> ctxt.translate(scaledMoveFactor, 0);
			case Direction.UP -> ctxt.translate(0, - scaledMoveFactor);
			case Direction.DOWN -> ctxt.translate(0, scaledMoveFactor);

			case Direction.FORWARDS -> zoomWithScale(zoomFactor);
			case Direction.BACKWARDS -> zoomWithScale(1 / zoomFactor);

			case Direction.CENTER -> config.tui.error("direction " + direction + " is not implemented yet!");
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

	//////////////// draw stuff

	/**
	 * @param x x coordinate in DATA standards, the coordinates are then converted into CANVAS standards
	 * @param y x coordinate in DATA standards, the coordinates are then converted into CANVAS standards
	 */
	public void drawPoint(double x, double y) {
		drawPoint(x, y, true);
	}
	/**
	 * @param x x coordinate
	 * @param y x coordinate
	 * @param convert wether to convert coordinates from DATA into CANVAS or not
	 */
	public void drawPoint(double x, double y, boolean convert) {
		config.tui.diveWhere("canvasContainer/drawPoint");

		if (convert) {
			// convertir les distances (cf normalisations)
			x = dataToCanvasUnit(x);
			y = dataToCanvasUnit(y);
		}

		// ajouter le nouveau point au totalDrawingRectangle
		fitPoint(x, y);

		// dessiner + log
		if (disableDrawing) {
			config.tui.debug("NODRAW - processed point with center (" + x + ":" + y + "), radius " + pointRadius);
		} else {
			ctxt.setFill(pointColor);
			ctxt.fillOval(x - pointRadius, y - pointRadius, pointRadius*2, pointRadius*2);
			config.tui.debug("Drew point with center (" + x + ":" + y + "), radius " + pointRadius);
		}

		config.tui.popWhere();
	}

	/**
	 * @param x1 first x coordinate in DATA standards
	 * @param y1 first y coordinate in DATA standards
	 * @param x2 second x coordinate in DATA standards
	 * @param y2 second y coordinate in DATA standards
	 * @param width width of the line in DATA standards
	 * @param color
	 */
	public void drawLine(double x1, double y1, double x2, double y2, double width, Color color) {
		config.tui.diveWhere("canvasContainer/drawPoint");

		x1 = dataToCanvasUnit(x1);
		y1 = dataToCanvasUnit(y1);
		x2 = dataToCanvasUnit(x2);
		y2 = dataToCanvasUnit(y2);

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

	//////////////// general purposal managing stuff

	public void logTotalDrawingRectangle() {
		ctxt.setStroke(Color.GREEN);
		ctxt.strokeRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/logTotalDrawingRectangle: " + totalDrawingRectangle.getMinX() + "," + totalDrawingRectangle.getMinY() + "," + totalDrawingRectangle.getWidth() + "," + totalDrawingRectangle.getHeight());
	}

	public void recalculateTotalDrawingRectangle() {
		totalDrawingRectangle = new Rectangle();
		boolean localDisableDrawing = disableDrawing;
		disableDrawing = true; // do not draw anything !!

		fitPoint(0, 0);
		fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());

		config.tui.debug("recalculateTotalDrawingRectangle: triggering redraw in order to place all the canvas-objects in that holly rectangle again");
		redraw();

		disableDrawing = localDisableDrawing; // restore the original disableDrawing flag
	}

	public void clearFancy() {
		ctxt.setFill(Color.ORANGERED);
		ctxt.fillRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/clearFancy: totalDrawingRectangle coordinates: (" + totalDrawingRectangle.getMinX() + ":" + totalDrawingRectangle.getMinY() + ") -> (" + totalDrawingRectangle.getWidth() + ":" + totalDrawingRectangle.getHeight() + ")");
	}

	public void clear() {
		ctxt.clearRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/clear: totalDrawingRectangle coordinates: (" + totalDrawingRectangle.getMinX() + ":" + totalDrawingRectangle.getMinY() + ") -> (" + totalDrawingRectangle.getWidth() + ":" + totalDrawingRectangle.getHeight() + ")");
	}

	public void redraw() {
		clear();
		config.tui.debug("canvasContainer/redraw: graphicsContext.transform (affine): " + canvas.getGraphicsContext2D().getTransform().toString());

		if (! disableDrawing) {
			Color c = Color.TEAL;
			config.tui.debug("canvasContainer/redraw: drawing canvas with a " + c + " background");
			ctxt.setFill(Color.TEAL);
			ctxt.fillRect(0, 0, getWidth(), getHeight());
		}

//		if (currentNiveau != null)
//			currentNiveau.draw(config.tui, this);
		drawingContext.redraw();
	}

	public Niveau getCurrentNiveau() {
		return currentNiveau;
	}

	public void setCurrentNiveau(Niveau currentNiveau) {
		this.currentNiveau = currentNiveau;
		drawingContext.setFocusedObject(currentNiveau); // TODO!! TMP !!
	}
}
