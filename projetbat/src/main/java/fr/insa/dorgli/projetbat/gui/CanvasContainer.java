package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import fr.insa.dorgli.projetbat.objects.Niveau;
import java.awt.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasContainer extends Pane {
	private Config config;
	private MainPane mainPane;

	private Canvas canvas;
	private GraphicsContext ctxt;

	private Niveau currentNiveau;
	private Rectangle totalDrawingRectangle;

	private double zoomFactor;
	private double moveFactor;
	private double scaledMoveFactor;

	private int pointRadius = 5;
	private Color pointColor = Color.RED;

	private double scaleLabelValue = 1;

	public CanvasContainer(Config config, MainPane mainPane) {
		this.config = config;
		this.mainPane = mainPane;

		canvas = new Canvas(this.getWidth(), this.getHeight());
		canvas.widthProperty().bind(this.widthProperty());
		canvas.widthProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: width has changed");
			rescaleBufferedScaledValues();
		});
		canvas.heightProperty().bind(this.heightProperty());
		canvas.heightProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: height has changed");
			rescaleBufferedScaledValues();
		});

 		ctxt = canvas.getGraphicsContext2D();
		totalDrawingRectangle = new Rectangle( (int)Math.ceil(super.getWidth()), (int)Math.ceil(super.getHeight()) );
		fitPoint(0, 0); // origin doesn't move once it is initialized

		zoomFactor = 2;
		moveFactor = 20;

		super.getChildren().add(canvas);
	}

	// Renvoie dans les dimensions du canvas l'équivalent d'une dimension dans "la vue" (d'unité arbitraire, à zoom=1)
	private double scaleToView(double incoming) {
		// Mxx est un coef de la matrice de transformation du canvas, donc en partie de son zoom
		// Comme le zoom est orthonormal, mxx=myy=mzz, donc on choisit mxx arbitrairement
		return incoming * (zoomFactor * 1/ctxt.getTransform().getMxx());
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
		fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());

		// then: rescale all the buffered values
		scaleMoveFactor();

		// finally: redraw
		redraw();
	}

	// convertit les distances de la partie "structure" en distances sur le canvas
	// actuellement (première norme) : (double) 1 mètre <=> (double) 100 pointsCanvas
	public double dataToCanvasUnit(double dataUnit) {
		return 100 * dataUnit;
	}

	public void moveView(Direction direction) {
		config.tui.diveWhere("canvasContainer/moveView");

		clear();

		switch (direction) {
			// TODO: le zoom s'effectue vers l'origine du canvas, pas vers le centre de la vue
			// TODO: mauvaise adaptation lors d'un changement de taille de fenêtre (bugs visuels lors de redraw, formes étirées
			case Direction.FORWARDS -> {
				ctxt.scale(zoomFactor, zoomFactor);
				scaleLabelValue *= zoomFactor;
				mainPane.setLabelCanvasScaleText(scaleLabelValue);
				scaleMoveFactor();
			}
			case Direction.BACKWARDS -> {
				ctxt.scale(1/zoomFactor, 1/zoomFactor);
				scaleLabelValue /= zoomFactor;
				mainPane.setLabelCanvasScaleText(scaleLabelValue);
				scaleMoveFactor();
			}

			case Direction.CENTER, Direction.FIT -> config.tui.error("direction " + direction + " is not implemented yet!");

			case Direction.LEFT -> ctxt.translate(- scaledMoveFactor, 0);
			case Direction.RIGHT -> ctxt.translate(scaledMoveFactor, 0);
			case Direction.UP -> ctxt.translate(0, - scaledMoveFactor);
			case Direction.DOWN -> ctxt.translate(0, scaledMoveFactor);

			default -> config.tui.error("bad direction: " + direction);
		}

		redraw();
		config.tui.popWhere();
	}

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
		config.tui.diveWhere("canvasContainer/fitPoint");

		int topLeftX = (int)Math.floor(x - pointRadius - 1);
		int topLeftY = (int)Math.floor(y - pointRadius - 1);
		int diameter = 2 * pointRadius + 2; // +2 catches the double.floor

		Rectangle pointArea = new Rectangle(topLeftX, topLeftY, diameter, diameter);
		config.tui.debug("IntPoint (" + x + ":" + y + ") lives in pointArea: " + pointArea);

		fitArea(pointArea);
		config.tui.popWhere();
	}

	/**
	 * @param x x coordinate in DATA standards, the coordinates are then converted into CANVAS standards
	 * @param y x coordinate in DATA standards, the coordinates are then converted into CANVAS standards
	 */
	public void drawPoint(double x, double y) {
		config.tui.diveWhere("canvasContainer/drawPoint");

		// convertir les distances (cf normalisations)
		double canvasX = dataToCanvasUnit(x);
		double canvasY = dataToCanvasUnit(y);

		// ajouter le nouveau point au totalDrawingRectangle
		fitPoint(canvasX, canvasY);

		// dessiner + log
		ctxt.setFill(pointColor);
		ctxt.fillOval(canvasX - pointRadius, canvasY - pointRadius, pointRadius*2, pointRadius*2);
		config.tui.debug("Drew point with center (" + canvasX + ":" + canvasY + "), radius " + pointRadius);

		config.tui.popWhere();
	}

	public void logTotalDrawingRectangle() {
		ctxt.setStroke(Color.GREEN);
		ctxt.strokeRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/logTotalDrawingRectangle: " + totalDrawingRectangle.getMinX() + "," + totalDrawingRectangle.getMinY() + "," + totalDrawingRectangle.getWidth() + "," + totalDrawingRectangle.getHeight());
	}

	public void clear() {
		ctxt.clearRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/clear: parentRelativeCoordinates: (" + totalDrawingRectangle.getMinX() + ":" + totalDrawingRectangle.getMinY() + ") -> (" + totalDrawingRectangle.getWidth() + ":" + totalDrawingRectangle.getHeight() + ")");
	}

	public void redraw() {
		clear();
		config.tui.debug("canvasContainer/redraw: graphicsContext.transform (affine): " + canvas.getGraphicsContext2D().getTransform().toString());

		ctxt.setFill(Color.TEAL);
		ctxt.fillRect(0, 0, getWidth(), getHeight());

		config.project.objects.drawAll(this);
	}

	public Niveau getCurrentNiveau() {
		return currentNiveau;
	}

	public void setCurrentNiveau(Niveau currentNiveau) {
		this.currentNiveau = currentNiveau;
	}
}