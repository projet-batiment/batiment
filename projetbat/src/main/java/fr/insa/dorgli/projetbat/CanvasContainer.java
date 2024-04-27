package fr.insa.dorgli.projetbat;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasContainer extends Pane {
	private Canvas canvas;
	private MainPane mainPane;
	private Config config;

	public CanvasContainer(Config config, MainPane mainPane) {
		this.config = config;
		this.mainPane = mainPane;

		canvas = new Canvas(this.getWidth(), this.getHeight());
		canvas.widthProperty().bind(this.widthProperty());
		canvas.widthProperty().addListener((o) -> {
		    redraw();
		});
		canvas.heightProperty().bind(this.heightProperty());
		canvas.heightProperty().addListener((o) -> {
		    redraw();
		});

		super.getChildren().add(canvas);
	}

	public void zoomScale(double scale) {
		GraphicsContext ctxt = canvas.getGraphicsContext2D();
		ctxt.scale(scale, scale);
	}

	public void redraw() {
		GraphicsContext ctxt = canvas.getGraphicsContext2D();
		ctxt.setFill(Color.WHITE);
		ctxt.fillRect(0, 0, getWidth(), getHeight());
		config.project.objects.drawAll(ctxt);
	}
}