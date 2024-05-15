package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Controller;
import fr.insa.dorgli.projetbat.ui.TUI;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OutilsTop extends HBox {

MenuBar menuBar = new Menubar();
Menu menu1= new Menu("fichier");
Menu menu2= new Menu("affichage");
Menu menu3= new Menu("créer");
Menu menu4= new Menu("options");
Menu menu5= new Menu("aide");


MenuItem buttonOpenFile = new MenuItem("Ouvrir...");
MenuItem buttonSaveFile = new MenuItem("Sauvegarder");

MenuItem buttonZoomIn = new MenuItem("Zoom +");
MenuItem buttonZoomOut = new MenuItem("Zoom -");
MenuItem buttonZoomFit = new MenuItem("Zoom fit");
MenuItem buttonMoveUp = new MenuItem("Move up");
MenuItem buttonMoveDown = new MenuItem("Move down");
MenuItem buttonMoveLeft = new MenuItem("Move left");
MenuItem buttonMoveRight = new MenuItem("Move right");
MenuItem buttonResetView = new MenuItem("Reset");

MenuItem buttonMur = new MenuItem("Mur");
MenuItem buttonPiece = new MenuItem("Piece");
MenuItem buttonNiveau = new MenuItem("Niveau");
MenuItem buttonAppart = new MenuItem("Appart");
MenuItem buttonPlafond = new MenuItem("Plafond");
MenuItem buttonSol = new MenuItem("Sol");
MenuItem buttonTypeMur = new MenuItem("Type Mur");
MenuItem buttonTypeRevetement = new MenuItem("Type Revetement");
MenuItem buttonTypeAppart = new MenuItem("Type Appart");
MenuItem buttonTypeOuvertureNiveau = new MenuItem("Type Ouverture Niveau");
MenuItem buttonTypeOuvertureMur = new MenuItem("Type Ouverture Mur");

MenuItem buttonDebug = new MenuItem("Debug");
MenuItem buttonFancy = new MenuItem("Fancy");
MenuItem buttonFancy2 = new MenuItem("Fancy 2");
MenuItem buttonFancy3 = new MenuItem("Fancy 3");

MenuItem buttonAide = new MenuItem("Aide");
SeparatorMenuItem separator = new SeparatorMenuItem();

menu1.getItems().add(buttonOpenFile);
menu1.getItems().add(separator);
menu1.getItems().add(buttonSaveFile);
menu1.getItems().add(separator);

menu2.getItems().add(buttonZoomIn);
menu2.getItems().add(separator);
menu2.getItems().add(buttonZoomOut);
menu2.getItems().add(separator);
menu2.getItems().add(buttonZoomFit);
menu2.getItems().add(separator);
menu2.getItems().add(buttonMoveUp);
menu2.getItems().add(separator);
menu2.getItems().add(buttonMoveDown);
menu2.getItems().add(separator);
menu2.getItems().add(buttonMoveLeft);
menu2.getItems().add(separator);
menu2.getItems().add(buttonMoveRight);
menu2.getItems().add(separator);
menu2.getItems().add(buttonResetView);
menu2.getItems().add(separator);

menu3.getItems().add(buttonMur);
menu3.getItems().add(separator);
menu3.getItems().add(buttonPiece);
menu3.getItems().add(separator);
menu3.getItems().add(buttonNiveau);
menu3.getItems().add(separator);
menu3.getItems().add(buttonAppart);
menu3.getItems().add(separator);
menu3.getItems().add(buttonPlafond);
menu3.getItems().add(separator);
menu3.getItems().add(buttonSol);
menu3.getItems().add(separator);
menu3.getItems().add(buttonTypeMur);
menu3.getItems().add(separator);
menu3.getItems().add(buttonTypeRevetement);
menu3.getItems().add(separator);
menu3.getItems().add(buttonTypeAppart);
menu3.getItems().add(separator);
menu3.getItems().add(buttonTypeOuvertureMur);
menu3.getItems().add(separator);
menu3.getItems().add(buttonTypeOuvertureNiveau);
menu3.getItems().add(separator);

menu4.getItems().add(buttonDebug);
menu4.getItems().add(separator);
menu4.getItems().add(buttonFancy);
menu4.getItems().add(separator);
menu4.getItems().add(buttonFancy2);
menu4.getItems().add(separator);
menu4.getItems().add(buttonFancy3);
menu4.getItems().add(separator);

menu5.getItems().add(buttonAide);
menu5.getItems().add(separator);

buttonAide.setOnAction(e -> {
        System.out.println("Veuillez poser votre question à l'un des créateurs de ce projet, il aura sûrment réponse à votre question");
        });
menuBar.getMenus().add(menu1);
menuBar.getMenus().add(menu2);
menuBar.getMenus().add(menu3);
menuBar.getMenus().add(menu4);
menuBar.getMenus().add(menu5);

VBox vBox = new VBox(menuBar);
        
Scene scene = new Scene(vBox, 960, 600);
primaryStage.setScene(scene);
primaryStage.show();

}  

  public OutilsTop(Config config, Stage mainStage) {
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

