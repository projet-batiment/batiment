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
	private Button buttonSaveFile;
	private Button buttonDebug;

	private Button buttonZoomIn;
	private Button buttonZoomOut;
	private Button buttonZoomFit;
	private Button buttonResetView;

	private Button buttonMoveLeft;
	private Button buttonMoveRight;
	private Button buttonMoveUp;
	private Button buttonMoveDown;

	private Button buttonFancy;
	private Button buttonFancy2;
	private Button buttonFancy3;

	private Button refreshButton;
	private Button totrevButton;
	private Button totouvButton;
	private Button totalButton;
	private Button detailButton;

	private Label labelCanvasScale;

	private CanvasContainer canvasContainer;

	public MainPane(Config config, Stage mainStage) {
		this.config = config;
		config.setMainPane(this);
		controller = new Controller(config, this);

		buttonOpenFile = new Button("Ouvrir...");
		buttonOpenFile.setOnAction(event -> {
			controller.openFile(event);
		});

		buttonSaveFile = new Button("Enregistrer");
		buttonSaveFile.setOnAction(event -> {
			controller.saveFile(event);
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
		buttonResetView = new Button("Reset View");
		buttonResetView.setOnAction(event -> {
			config.tui.log("clicked: reset view");
			controller.moveCanvasView(Direction.RESET);
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
			canvasContainer.clearFancy();
		});
		buttonFancy3 = new Button("Fancy3 !");
		buttonFancy3.setOnAction(event -> {
			config.tui.log("clicked: fancy3");
			canvasContainer.TMPDrawOriginPoint();
		});

//		refreshButton = new Button("actualiser");
//    		refreshButton.setOnAction(evt -> {
//			System.out.println(Piece.prix);
//		});
//
//		totalButton = new Button ("Devis total");
//    		totalButton.setOnAction(evt -> {
//			controller.devisTotal();
//		});
//
//    		detailButton = new Button ("DetailPieces");
//    		detailButton.setOnAction(evt -> {
//			message.setText("DETAIL PIECES");
//			System.out.println(get.appart.TypeAppart + " n°: " + get.appart.nom);
//			System.out.println("piece : " + get.pieces.nom + "| prix : " + get.pieces.prix );
//			System.out.println("prix mur : " + get.pieces.murs.prixMur );
//			System.out.println("prix plafond : " + get.pieces.plafond.prixPlafondSol );
//			System.out.println("prix sol : " + get.pieces.sol.prixPlafondSol );
//		});
//
//    		totouvButton = new Button ("TotalOuverture");
//    		totouvButton.setOnAction(evt -> {
//			message.setText("TOTAL OUVERTURE");
//			System.out.println("ouvertures du plafond : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau.prixOuverture);
//			System.out.println("ouvertures du plafond : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau.prixOuverture);
//			System.out.println("ouvertures des murs : " + get.pieces.murs.ouvertures.TypeOuvertureMur + "| prix ouverture : " + get.pieces.murs.ouvertures.TypeOuvertureMur.prixUnitaire);
//		});
//
//    		totrevButton = new Button ("TotalRevetement");
//   		totrevButton.setOnAction(evt -> {
//			message.setText(" TOTAL REVETEMENT");
//			System.out.println(" revetement mur: " + get.pieces.murs.revetements1.typeRevetement + "| prix revetement : " + get.pieces.murs.revetement1.typeRevetement.prixUnitaire);
//			System.out.println(" revetement plafond: " + get.pieces.plafond.revetements.typeRevetement + "|prix revetement : " + get.pieces.plafond.revetements.typeRevetement.prixUnitaire);
//			System.out.println(" revetement sol: " + get.pieces.sol.revetements.typeRevetement + "|prix revetement : " + get.pieces.sol.revetements.typeRevetement.prixUnitaire);
//		});

		labelCanvasScale = new Label("x1");

		super.setRight(new VBox(
		    buttonOpenFile,
		    buttonSaveFile,
		    buttonDebug,

		    buttonZoomIn,
		    buttonZoomOut,
		    buttonZoomFit,
		    buttonResetView,

		    buttonMoveLeft,
		    buttonMoveRight,
		    buttonMoveUp,
		    buttonMoveDown,

		    buttonFancy,
		    buttonFancy2,
		    buttonFancy3,

//		    refreshButton,
//		    detailButton,
//		    totalButton,
//		    totrevButton,
//		    totouvButton,

		    labelCanvasScale
		));

		this.canvasContainer = new CanvasContainer(config, this);
		super.setCenter(this.canvasContainer);

		super.minWidthProperty().bind(mainStage.minWidthProperty());
		super.minHeightProperty().bind(mainStage.minHeightProperty());
	}

	public void setLabelCanvasScaleText(double value) {
		// java et vive l'optimisation : round(value * 100) / 100 🥳
		this.labelCanvasScale.setText("x" + ((double)Math.round(value * 100) / 100));
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
