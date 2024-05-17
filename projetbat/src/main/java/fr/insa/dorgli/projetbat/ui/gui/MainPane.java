package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Controller;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPane extends BorderPane {
	private final Controller controller;

	private final CanvasContainer canvasContainer;

	private final OutilsTop outilsTop;
//	private final OutilsRight outilsRight;
//	private final OutilsDown outilsDown;

	public MainPane(Config config, Stage mainStage) {
		config.setMainPane(this);
		this.controller = config.controller;

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());

		this.canvasContainer = new CanvasContainer(config, this);
		this.setCenter(canvasContainer);

		this.outilsTop = new OutilsTop(config);
		this.setTop(outilsTop);

//		this.outilsRight = new OutilsRight(config);
//		this.outilsDown = new OutilsDown(config);

//		this.setTop(this.outilsLeft);
//		this.setTop(this.outilsRight);
//		this.setTop(this.outilsDown);
	}

	public Controller getController() {
		return controller;
	}

	public CanvasContainer getCanvasContainer() {
		return canvasContainer;
	}
}