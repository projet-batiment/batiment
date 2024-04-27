package fr.insa.dorgli.projetbat;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPane extends BorderPane {
	private Config config;
	private Controller controller;
	private Stage mainStage;

	private Button buttonOpenFile;
	private Button buttonDebug;

	private Button buttonZoomIn;
	private Button buttonZoomOut;
	private Button buttonZoomFit;

	private CanvasContainer canvasContainer;

	public MainPane(Config config, Stage mainStage) {
		this.config = config;
		controller = new Controller(config, this);

		buttonOpenFile = new Button("Ouvrir...");
		buttonOpenFile.setOnAction(event -> {
			controller.openFile(event);
		});

		buttonDebug = new Button("Debug");
		buttonDebug.setOnAction(event -> {
			config.tui.setLogLevel(TUI.LogLevel.DEBUG);
			config.tui.debug("debugging: on");
		});

		buttonZoomIn = new Button("Zoom In");
		buttonZoomIn.setOnAction(event -> {
			config.tui.log("clicked: zoom in");
			controller.zoomIn();
		});
		buttonZoomOut = new Button("Zoom Out");
		buttonZoomOut.setOnAction(event -> {
			config.tui.log("clicked: zoom out");
			controller.zoomOut();
		});
		buttonZoomFit = new Button("Zoom Fit");
		buttonZoomFit.setOnAction(event -> {
			config.tui.log("clicked: zoom fit");
			/// TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
			//controller.zoomFit();
		});

		super.setRight(new VBox(
		    buttonOpenFile,
		    buttonDebug,
		    buttonZoomIn,
		    buttonZoomOut,
		    buttonZoomFit
		));

		this.canvasContainer = new CanvasContainer(config, this);
		super.setCenter(this.canvasContainer);

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());
	}

	public Stage getMainStage() {
		return mainStage;
	}

	public Controller getController() {
		return controller;
	}

	public CanvasContainer getCanvasContainer() {
		return canvasContainer;
	}
}