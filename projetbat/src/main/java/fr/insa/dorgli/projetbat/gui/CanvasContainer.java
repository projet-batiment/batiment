package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import java.awt.Rectangle;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasContainer extends Pane {
	private Config config;
	private MainPane mainPane;

	private Canvas canvas;
	private GraphicsContext ctxt;

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
			config.tui.debug("canvasContainer: width has changed: refitting the canvas inside the totalDrawingRectangle...");
			fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());
			redraw();
		});
		canvas.heightProperty().bind(this.heightProperty());
		canvas.heightProperty().addListener((o) -> {
			config.tui.debug("canvasContainer: height has changed: refitting the canvas inside the totalDrawingRectangle...");
			fitPoint((int)canvas.getWidth(), (int)canvas.getHeight());
			redraw();
		});

 		ctxt = canvas.getGraphicsContext2D();
		totalDrawingRectangle = new Rectangle( (int)Math.ceil(super.getWidth()), (int)Math.ceil(super.getHeight()) );

		zoomFactor = 2;
		moveFactor = 20;

		super.getChildren().add(canvas);
	}

	// moveFactor définit un pas fixe qui ne s'adapte pas au zoom
	// scaledMoveFactor définit la taille réelle du pas fixe ramené au zoom actuel
	// scaleMoveFactor() permet de calculer scaledMoveFactor après un changement de zoom
	private void scaleMoveFactor() {
 		scaledMoveFactor = moveFactor * (zoomFactor * 1/ctxt.getTransform().getMxx());
	}

	public void moveView(Direction direction) {
		config.tui.diveWhere("canvasContainer/moveView");

		clear();
		double scaledMoveFactor = moveFactor * (zoomFactor * 1/ctxt.getTransform().getMxx());

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

	public void clear() {
		ctxt.clearRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());

		Bounds thisBoundsInScene = super.localToScene(super.getBoundsInLocal());
		Node parent = super.getParent();
		Bounds parentBoundsInScene = parent.localToScene(parent.getBoundsInLocal());

		double parentRelativeMinX = parentBoundsInScene.getMinX() - thisBoundsInScene.getMinX();
		double parentRelativeMinY = parentBoundsInScene.getMinY() - thisBoundsInScene.getMinY();
		double parentWidth = parentBoundsInScene.getMaxX() - parentBoundsInScene.getMinX();
		double parentHeight = parentBoundsInScene.getMaxY() - parentBoundsInScene.getMinY();

		config.tui.debug("canvasContainer/clear: parentRelativeCoordinates: (" + parentRelativeMinX + ":" + parentRelativeMinY + ") -> (" + parentWidth + ":"+ parentHeight + ")");
	}

	private void fitPoint(double x, double y) {
		config.tui.diveWhere("canvasContainer/fitPoint");

		int topLeftX = (int)(x - pointRadius);
		int topLeftY = (int)(y - pointRadius);

		Rectangle pointArea = new Rectangle(topLeftX, topLeftY, pointRadius, pointRadius);
		config.tui.debug("IntPoint (" + x + ":" + y + ") lives in pointArea: " + pointArea);

		if (! totalDrawingRectangle.contains(pointArea)) {
			config.tui.debug("Recalculating the totalDrawingRectangle: old " + totalDrawingRectangle.toString());
			totalDrawingRectangle.add(pointArea);
			config.tui.debug("Recalculated the totalDrawingRectangle:  new " + totalDrawingRectangle.toString());
		} else {
			config.tui.debug("The totalDrawingRectangle already fits the pointArea: " + totalDrawingRectangle.toString());
		}

		config.tui.popWhere();
	}

	public void drawPoint(double x, double y) {
		config.tui.diveWhere("canvasContainer/drawPoint");

		double topLeftX = x - pointRadius;
		double topLeftY = y - pointRadius;
		fitPoint(x, y);

		ctxt.setFill(pointColor);
		ctxt.fillOval(topLeftX, topLeftY, pointRadius*2, pointRadius*2);
		config.tui.debug("Drew point with center (" + x + ":" + y + "), radius " + pointRadius);

		config.tui.popWhere();
	}

	public void logTotalDrawingRectangle() {
		ctxt.strokeRect(totalDrawingRectangle.getMinX(), totalDrawingRectangle.getMinY(), totalDrawingRectangle.getWidth(), totalDrawingRectangle.getHeight());
		config.tui.debug("canvasContainer/logEtc: " + totalDrawingRectangle.getMinX() + "," + totalDrawingRectangle.getMinY() + "," + totalDrawingRectangle.getWidth() + "," + totalDrawingRectangle.getHeight());
	}

	public void redraw() {
		clear();
		config.tui.debug("canvasContainer/redraw: graphicsContext.transform (affine): " + canvas.getGraphicsContext2D().getTransform().toString());

		ctxt.setFill(Color.TEAL);
		ctxt.fillRect(0, 0, getWidth(), getHeight());

//		ctxt.setFill(Color.BLUE);
//		ctxt.fillRect(10, 10, 20, 20);

		//config.project.objects.drawAll(this);
	}

	public Canvas getCanvas() {
		return canvas;
	}
}