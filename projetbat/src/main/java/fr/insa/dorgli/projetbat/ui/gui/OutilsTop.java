package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.control.Controller;
import fr.insa.dorgli.projetbat.ui.TUI;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class OutilsTop extends MenuBar {
	private final Config config;
	private final Controller controller;

	// fichier
	private final Menu menuFichier;
	private final MenuItem buttonOpenFile;
	private final MenuItem buttonSaveFile;
	private final MenuItem buttonSaveAs;

	// action
	private final Menu action;

	private final MenuItem buttonBatimentEdit;
	private final MenuItem buttonNiveauEdit;
	private final MenuItem buttonProjectEdit;
	private final MenuItem buttonDeleteSelection;
	private final CheckMenuItem buttonMultiSelect;
	private final MenuItem buttonFocusedToRoot;

	private final MenuItem buttonBatimentCreer;
	private final MenuItem buttonNiveauCreer;
	private final MenuItem buttonAppartCreer;
	private final MenuItem buttonPieceCreer;
	private final MenuItem buttonMurCreer;
	private final SeparatorMenuItem separatorCreer;
	private final MenuItem buttonTypeBatimentCreer;
	private final MenuItem buttonTypeAppartCreer;
	private final MenuItem buttonTypeMurCreer;
	private final MenuItem buttonTypeRevetementCreer;
	private final MenuItem buttonTypeOuvertureNiveauxCreer;
	private final MenuItem buttonTypeOuvertureMurCreer;

	// devis
	private final Menu menuDevis;
	private final MenuItem buttonDevisTotal;
	private final MenuItem buttonDevisSelection;
	private final MenuItem buttonSaveDevisAs;

	// afficher
	private final Menu menuAfficher;
	private final MenuItem buttonZoomIn;
	private final MenuItem buttonZoomOut;
	private final CheckMenuItem buttonZoomFit;
	private final MenuItem buttonZoomZero;
	private final SeparatorMenuItem separatorAfficher1;
	private final MenuItem buttonMoveUp;
	private final MenuItem buttonMoveDown;
	private final MenuItem buttonMoveLeft;
	private final MenuItem buttonMoveRight;
	private final SeparatorMenuItem separatorAfficher2;
	private final MenuItem buttonResetView;

	// options
	private final Menu menuOptions;
	private final MenuItem buttonPreferences;
	private final SeparatorMenuItem separatorOptions1;
	private final MenuItem buttonQuiet;
	private final MenuItem buttonNormal;
	private final MenuItem buttonLog;
	private final MenuItem buttonDebug;
	private final MenuItem buttonTrace;
	private final SeparatorMenuItem separatorOptions2;
	private final MenuItem buttonFancyTotalDrawingRectangleEdge;
	private final MenuItem buttonFancyTotalDrawingRectangleFill;
	private final MenuItem buttonFancyRedraw;

	// aide
	private final Menu menuAide;
	private final MenuItem buttonAide;
	private final SeparatorMenuItem separatorAide;
	private final MenuItem buttonApropos;

	public OutilsTop(Config config) {
		this.config = config;
		this.controller = config.getMainWindow().getController();

		///// Menu Fichier

		buttonOpenFile = new MenuItem("Ouvrir...");
		buttonOpenFile.setOnAction(event -> {
			config.tui.log("clicked: open file");
			controller.openFile(event);
		});

		buttonSaveFile = new MenuItem("Enregistrer");
		buttonSaveFile.setOnAction(event -> {
			config.tui.log("clicked: save file");
			controller.saveFile(event);
		});

		buttonSaveAs = new MenuItem("Enregistrer sous...");
		buttonSaveAs.setOnAction(event -> {
			config.tui.log("clicked: save as");
			controller.saveFile(event);
		});

		menuFichier = new Menu("Fichier", null,
			buttonOpenFile,
			buttonSaveFile,
			buttonSaveAs
		);

		///// Menu Actions

		/// Sous-menu créer

		buttonBatimentCreer = new MenuItem("Batiment");
		buttonBatimentCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Batiment");
		});

		buttonNiveauCreer = new MenuItem("Niveau");
		buttonNiveauCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Niveau");
		});

		buttonAppartCreer = new MenuItem("Appart");
		buttonAppartCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Appart");
		});

		buttonPieceCreer = new MenuItem("Piece");
		buttonPieceCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Piece");
		});

		buttonMurCreer = new MenuItem("Mur");
		buttonMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Mur");
			config.controller.menuButtonCreateMur();
		});

		separatorCreer = new SeparatorMenuItem();

		buttonTypeBatimentCreer = new MenuItem("Type Batiment");
		buttonTypeBatimentCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeBatiment");
		});

		buttonTypeAppartCreer = new MenuItem("Type Appart");
		buttonTypeAppartCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeAppart");
		});

		buttonTypeMurCreer = new MenuItem("Type Mur");
		buttonTypeMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeMur");
		});

		buttonTypeRevetementCreer = new MenuItem("Type Revetement");
		buttonTypeRevetementCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeRevetement");
		});

		buttonTypeOuvertureNiveauxCreer = new MenuItem("Type Ouverture Niveaux");
		buttonTypeOuvertureNiveauxCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeOuvertureNiveaux");
		});

		buttonTypeOuvertureMurCreer = new MenuItem("Type Ouverture Mur");
		buttonTypeOuvertureMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeOuvertureMur");
		});

		/// Sous-menu éditer

		buttonProjectEdit = new MenuItem("Projet");
		buttonProjectEdit.setOnAction(event -> {
			config.tui.log("clicked: edit current project");
		});

		buttonBatimentEdit = new MenuItem("Batiment actuel");
		buttonBatimentEdit.setOnAction(event -> {
			config.tui.log("clicked: edit current Batiment");
		});

		buttonNiveauEdit = new MenuItem("Niveau actuel");
		buttonNiveauEdit.setOnAction(event -> {
			config.tui.log("clicked: edit current Niveau");
		});

		buttonMultiSelect = new CheckMenuItem("Sélections plusieurs objets");
		buttonMultiSelect.setSelected(false);
		buttonMultiSelect.setOnAction(event -> {
			config.tui.log("clicked: multi select");
			config.controller.menuButtonMultiSelect(buttonMultiSelect.isSelected());
		});

		buttonDeleteSelection = new MenuItem("Supprimer la sélection");
		buttonDeleteSelection.setOnAction(event -> {
			config.tui.log("clicked: delete selection");
		});

		buttonFocusedToRoot = new MenuItem("L'objet sélectionné");
		buttonFocusedToRoot.setOnAction(event -> {
			config.tui.log("clicked: focused to root");
		});

//		////////////// TODO!!!!!!!!!!!!!!!! ne marche pas: initialisé à vide
//		Menu menuNiveaux = new Menu("Niveau...");
//		for (Niveau niveau: config.project.objects.niveaux.values()) {
//			String niveauNom = niveau.getNom();
//			if (niveauNom.length() > 18) {
//				niveauNom = niveauNom.substring(0, 15) + "...";
//			}
//			MenuItem niveauButton = new MenuItem(niveauNom);
//			niveauButton.setOnAction((ActionEvent eh) -> {
//				config.tui.debug("clicked: focus niveau " + niveau);
//				config.controller.state.viewRootElement = niveau;
//				config.getMainWindow().getCanvasContainer().redraw();
//			});
//			menuNiveaux.getItems().add(niveauButton);
//		}
//
//		////////////// TODO!!!!!!!!!!!!!!!! ne marche pas: initialisé à vide
//		Menu menuBatiments = new Menu("Batiment...");
//		for (Batiment batiment: config.project.objects.batiments.values()) {
//			String batimentNom = batiment.getNom();
//			if (batimentNom.length() > 18) {
//				batimentNom = batimentNom.substring(0, 15) + "...";
//			}
//			MenuItem batimentButton = new MenuItem(batimentNom);
//			batimentButton.setOnAction((ActionEvent eh) -> {
//				config.tui.debug("clicked: focus batiment " + batiment);
//				config.controller.state.viewRootElement = batiment;
//				config.getMainWindow().getCanvasContainer().redraw();
//			});
//			menuBatiments.getItems().add(batimentButton);
//		}

		action = new Menu("Actions", null,
			new Menu("Créer...", null,
				buttonBatimentCreer,
				buttonNiveauCreer,
				buttonAppartCreer,
				buttonPieceCreer,
				buttonMurCreer,
				separatorCreer,
				buttonTypeBatimentCreer,
				buttonTypeAppartCreer,
				buttonTypeMurCreer,
				buttonTypeRevetementCreer,
				buttonTypeOuvertureNiveauxCreer,
				buttonTypeOuvertureMurCreer
			),
			new Menu("Éditer...", null,
				buttonProjectEdit,
				buttonBatimentEdit,
				buttonNiveauEdit
			),
			new Menu("Afficher...", null,
				buttonFocusedToRoot
//				menuNiveaux,
//				menuBatiments
			),
			buttonMultiSelect,
			buttonDeleteSelection
		);

		///// Menu Devis

		buttonDevisTotal = new MenuItem("Calculer le devis total");
		buttonDevisTotal.setOnAction(event -> {
			config.tui.log("clicked: devis total");
			controller.devisTotal();
		});

		buttonDevisSelection = new MenuItem("Calculer le devis de la sélection");
		buttonDevisSelection.setOnAction(event -> {
			config.tui.log("clicked: devis selection");
			controller.devisSelection();
		});

		buttonSaveDevisAs = new MenuItem("Enregistrer le devis actuel sous...");
		buttonSaveDevisAs.setOnAction(event -> {
			config.tui.log("clicked: save devis as");
			controller.saveDevis();
		});

		menuDevis = new Menu("Devis", null,
			buttonDevisTotal,
			buttonDevisSelection,
			buttonSaveDevisAs
		);

		///// Menu Afficher

		buttonZoomIn = new MenuItem("Zoom avant");
		buttonZoomIn.setOnAction(event -> {
			config.tui.log("clicked: zoom in");
			controller.moveCanvasView(Direction.FORWARDS);
		});

		buttonZoomOut = new MenuItem("Zoom arrière");
		buttonZoomOut.setOnAction(event -> {
			config.tui.log("clicked: zoom out");
			controller.moveCanvasView(Direction.BACKWARDS);
		});

		buttonZoomFit = new CheckMenuItem("Zoom adaptatif");
		buttonZoomFit.setSelected(false);
		buttonZoomFit.setOnAction(event -> {
			config.tui.log("clicked: zoom fit");
			controller.moveCanvasView(Direction.FIT);
		});

		buttonZoomZero = new MenuItem("Réinitialiser le zoom");
		buttonZoomZero.setOnAction(event -> {
			config.tui.log("clicked: zoom zero");
			controller.moveCanvasView(Direction.ZOOMZERO);
		});

		separatorAfficher1 = new SeparatorMenuItem();

		buttonMoveUp = new MenuItem("Déplacer vers le haut");
		buttonMoveUp.setOnAction(event -> {
			config.tui.log("clicked: move up");
			controller.moveCanvasView(Direction.UP);
		});

		buttonMoveDown = new MenuItem("Déplacer vers le bas");
		buttonMoveDown.setOnAction(event -> {
			config.tui.log("clicked: move down");
			controller.moveCanvasView(Direction.DOWN);
		});

		buttonMoveLeft = new MenuItem("Déplacer vers la gauche");
		buttonMoveLeft.setOnAction(event -> {
			config.tui.log("clicked: move left");
			controller.moveCanvasView(Direction.LEFT);
		});

		buttonMoveRight = new MenuItem("Déplacer vers la droite");
		buttonMoveRight.setOnAction(event -> {
			config.tui.log("clicked: move right");
			controller.moveCanvasView(Direction.RIGHT);
		});

		separatorAfficher2 = new SeparatorMenuItem();

		buttonResetView = new MenuItem("Réinitialiser la vue");
		buttonResetView.setOnAction(event -> {
			config.tui.log("clicked: reset view");
			controller.moveCanvasView(Direction.RESET);
		});

		menuAfficher = new Menu("Affichage", null,
			buttonZoomIn,
			buttonZoomOut,
			buttonZoomFit,
			buttonZoomZero,
			separatorAfficher1,
			buttonMoveUp,
			buttonMoveDown,
			buttonMoveLeft,
			buttonMoveRight,
			separatorAfficher2,
			buttonResetView
		);

		///// Menu Options

		buttonPreferences = new MenuItem("Préférences");
		buttonPreferences.setOnAction(event -> {
			config.tui.log("clicked: preferences");
		});

		separatorOptions1 = new SeparatorMenuItem();

		buttonQuiet = new MenuItem("Quiet");
		buttonQuiet.setOnAction(event -> {
			controller.setLogLevel(TUI.LogLevel.QUIET);
			config.tui.log("clicked: quiet");
		});

		buttonNormal = new MenuItem("Normal");
		buttonNormal.setOnAction(event -> {
			controller.setLogLevel(TUI.LogLevel.NORMAL);
			config.tui.log("clicked: normal");
		});

		buttonLog = new MenuItem("Verbose");
		buttonLog.setOnAction(event -> {
			controller.setLogLevel(TUI.LogLevel.LOG);
			config.tui.log("clicked: log");
		});

		buttonDebug = new MenuItem("Debug");
		buttonDebug.setOnAction(event -> {
			controller.setLogLevel(TUI.LogLevel.DEBUG);
			config.tui.log("clicked: debug");
		});

		buttonTrace = new MenuItem("Trace");
		buttonTrace.setOnAction(event -> {
			controller.setLogLevel(TUI.LogLevel.TRACE);
			config.tui.log("clicked: trace");
		});

		separatorOptions2 = new SeparatorMenuItem();

		buttonFancyTotalDrawingRectangleEdge = new MenuItem("TotalDrawingRectangle edge");
		buttonFancyTotalDrawingRectangleEdge.setOnAction(event -> {
			config.tui.log("clicked: TotalDrawingRectangle edge");
			config.getMainWindow().getCanvasContainer().logTotalDrawingRectangle();
		});

		buttonFancyTotalDrawingRectangleFill = new MenuItem("TotalDrawingRectangle fill");
		buttonFancyTotalDrawingRectangleFill.setOnAction(event -> {
			config.tui.log("clicked: TotalDrawingRectangle fill");
			config.getMainWindow().getCanvasContainer().clearFancy();
		});

		buttonFancyRedraw = new MenuItem("Redraw");
		buttonFancyRedraw.setOnAction(event -> {
			config.tui.log("clicked: redraw");
			config.getMainWindow().getCanvasContainer().redraw();
		});

		menuOptions = new Menu("Options", null,
			buttonPreferences,
			separatorOptions1,

			new Menu("LogLevel", null,
				buttonQuiet,
				buttonNormal,
				buttonLog,
				buttonDebug,
				buttonTrace
			),

			new Menu("Fancy", null,
				buttonFancyTotalDrawingRectangleEdge,
				buttonFancyTotalDrawingRectangleFill,
				buttonFancyRedraw
			)
		);

		///// Menu Aide

		buttonAide = new MenuItem("Afficher l'aide");
		buttonAide.setOnAction(e -> {
			config.tui.log("clicked: help");
			controller.menuClickedHelp();
		});

		buttonApropos = new MenuItem("À propos de " + Config.applicationName);
		buttonApropos.setOnAction(e -> {
			config.tui.log("clicked: apropos");
		});

		separatorAide = new SeparatorMenuItem();

		menuAide = new Menu("Aide", null,
			buttonAide,
			separatorAide,
			buttonApropos
		);

		///// MenuBar

		super.getMenus().addAll(menuFichier,
			action,
			menuDevis,
			menuAfficher,
			menuOptions,
			menuAide
		);
	}

	public void setButtonZoomFitSelected(boolean value) {
		buttonZoomFit.setSelected(value);
	}

//		refreshButton = new Button("actualiser");
//			refreshButton.setOnAction(evt -> {
//			System.out.println(Piece.prix);
//		});
//
//		totalButton = new Button ("Devis total");
//			totalButton.setOnAction(evt -> {
//			controller.devisTotal();
//		});
//
//			detailButton = new Button ("DetailPieces");
//			detailButton.setOnAction(evt -> {
//			message.setText("DETAIL PIECES");
//			System.out.println(get.appart.TypeAppart + " n°: " + get.appart.nom);
//			System.out.println("piece : " + get.pieces.nom + "| prix : " + get.pieces.prix );
//			System.out.println("prix mur : " + get.pieces.murs.prixMur );
//			System.out.println("prix plafond : " + get.pieces.plafond.prixPlafondSol );
//			System.out.println("prix sol : " + get.pieces.sol.prixPlafondSol );
//		});
//
//			totouvButton = new Button ("TotalOuverture");
//			totouvButton.setOnAction(evt -> {
//			message.setText("TOTAL OUVERTURE");
//			System.out.println("ouvertures du plafond : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau.prixOuverture);
//			System.out.println("ouvertures du plafond : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau.prixOuverture);
//			System.out.println("ouvertures des murs : " + get.pieces.murs.ouvertures.TypeOuvertureMur + "| prix ouverture : " + get.pieces.murs.ouvertures.TypeOuvertureMur.prixUnitaire);
//		});
//
//			totrevButton = new Button ("TotalRevetement");
//   		totrevButton.setOnAction(evt -> {
//			message.setText(" TOTAL REVETEMENT");
//			System.out.println(" revetement mur: " + get.pieces.murs.revetements1.typeRevetement + "| prix revetement : " + get.pieces.murs.revetement1.typeRevetement.prixUnitaire);
//			System.out.println(" revetement plafond: " + get.pieces.plafond.revetements.typeRevetement + "|prix revetement : " + get.pieces.plafond.revetements.typeRevetement.prixUnitaire);
//			System.out.println(" revetement sol: " + get.pieces.sol.revetements.typeRevetement + "|prix revetement : " + get.pieces.sol.revetements.typeRevetement.prixUnitaire);
//		});
}

