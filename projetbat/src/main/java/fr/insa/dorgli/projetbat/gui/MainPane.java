package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import fr.insa.dorgli.projetbat.Controller;
import fr.insa.dorgli.projetbat.TUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

	private Button buttonMoveLeft;
	private Button buttonMoveRight;
	private Button buttonMoveUp;
	private Button buttonMoveDown;

	private Button buttonFancy;
	private Button buttonFancy2;
	private Button buttonFancy3;

	private Label labelCanvasScale;

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
			controller.moveCanvasView(Direction.FORWARDS);
		});
		buttonZoomOut = new Button("Zoom Out");
		buttonZoomOut.setOnAction(event -> {
			config.tui.log("clicked: zoom out");
			controller.moveCanvasView(Direction.BACKWARDS);
		});
		buttonZoomFit = new Button("Zoom Fit");
		buttonZoomFit.setOnAction(event -> {
			config.tui.log("clicked: zoom fit");
			controller.moveCanvasView(Direction.FIT);
		});
		buttonMoveLeft = new Button("Move Left");
		buttonMoveLeft.setOnAction(event -> {
			config.tui.log("clicked: move left");
			controller.moveCanvasView(Direction.LEFT);
		});
		buttonMoveRight = new Button("Move Right");
		buttonMoveRight.setOnAction(event -> {
			config.tui.log("clicked: move right");
			controller.moveCanvasView(Direction.RIGHT);
		});
		buttonMoveUp = new Button("Move Up");
		buttonMoveUp.setOnAction(event -> {
			config.tui.log("clicked: move up");
			controller.moveCanvasView(Direction.UP);
		});
		buttonMoveDown = new Button("Move Down");
		buttonMoveDown.setOnAction(event -> {
			config.tui.log("clicked: move down");
			controller.moveCanvasView(Direction.DOWN);
		});

		buttonFancy = new Button("Fancy !");
		buttonFancy.setOnAction(event -> {
			config.tui.log("clicked: fancy");
			canvasContainer.logTotalDrawingRectangle();
		});
		buttonFancy2 = new Button("Fancy2 !");
		buttonFancy2.setOnAction(event -> {
			config.tui.log("clicked: fancy2");
			canvasContainer.logTotalDrawingRectangle();
		});
		buttonFancy3 = new Button("Fancy3 !");
		buttonFancy3.setOnAction(event -> {
			config.tui.log("clicked: fancy3");
		});

		labelCanvasScale = new Label("canvasScale: 1");

		super.setRight(new VBox(
		    buttonOpenFile,
		    buttonDebug,

		    buttonZoomIn,
		    buttonZoomOut,
		    buttonZoomFit,

		    buttonMoveLeft,
		    buttonMoveRight,
		    buttonMoveUp,
		    buttonMoveDown,

		    buttonFancy,
		    buttonFancy2,
		    buttonFancy3,

		    labelCanvasScale
		));

		this.canvasContainer = new CanvasContainer(config, this);
		super.setCenter(this.canvasContainer);

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());
	}

	public void setLabelCanvasScaleText(double value) {
		this.labelCanvasScale.setText("canvasScale: " + value);
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