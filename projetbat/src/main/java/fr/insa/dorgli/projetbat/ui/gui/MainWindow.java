package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Controller;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.SidePaneContainer;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends BorderPane {
	private final Controller controller;

	private final OutilsTop outilsTop;
	private final CanvasContainer canvasContainer;
	private final SidePaneContainer sidePaneContainer;

	public MainWindow(Config config, Stage mainStage) {
		config.setMainWindow(this);
		this.controller = config.controller;

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());

		this.outilsTop = new OutilsTop(config);
		this.setTop(outilsTop);

		this.canvasContainer = new CanvasContainer(config, this);
		this.setCenter(canvasContainer);

		this.sidePaneContainer = new SidePaneContainer(config);
		this.setRight(sidePaneContainer);
	}

	public Controller getController() {
		return controller;
	}

	public CanvasContainer getCanvasContainer() {
		return canvasContainer;
	}

	public SidePaneContainer getSidePaneContainer() {
		return sidePaneContainer;
	}

	public void setButtonZoomZeroSelected(boolean value) {
		outilsTop.setButtonZoomFitSelected(value);
	}
}