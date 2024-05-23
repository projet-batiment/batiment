package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.control.Controller;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.SidePane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends BorderPane {
	private final Controller controller;

	private final OutilsTop outilsTop;
	private final CanvasContainer canvasContainer;
	private final SidePane sidePane;
	private final BottomBar bottomBar;

	public MainWindow(Config config, Stage mainStage) {
		config.setMainWindow(this);
		this.controller = config.controller;

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());

		this.outilsTop = new OutilsTop(config);
		this.canvasContainer = new CanvasContainer(config, this);
		this.sidePane = new SidePane(config);
		this.bottomBar = new BottomBar(config);

		BorderPane centerArea = new BorderPane();
		centerArea.setCenter(canvasContainer);
		centerArea.setBottom(bottomBar);

		SplitPane mainArea = new SplitPane();
		mainArea.setDividerPositions(0.75);
		mainArea.getItems().addAll(centerArea, sidePane);

		bottomBar.update();

		this.setTop(outilsTop);
		this.setCenter(mainArea);
	}

	public CanvasContainer getCanvasContainer() {
		return canvasContainer;
	}

	public SidePane getSidePane() {
		return sidePane;
	}

	public BottomBar getBottomBar() {
		return bottomBar;
	}

	public void setButtonZoomZeroSelected(boolean value) {
		outilsTop.setButtonZoomFitSelected(value);
	}
}