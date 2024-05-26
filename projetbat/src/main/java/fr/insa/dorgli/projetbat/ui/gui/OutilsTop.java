package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.control.Controller;
import fr.insa.dorgli.projetbat.core.control.State;
import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.types.*;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.util.Set;
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
	private final Menu menuSelectionner;

	private final MenuItem buttonCurrentBatimentEdit;
	private final MenuItem buttonCurrentNiveauEdit;
	private final MenuItem buttonCurrentProjectEdit;
	private final MenuItem buttonDeleteSelection;
	private final CheckMenuItem buttonMultiSelect;
//	private final MenuItem buttonFocusedToRoot;

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

	private final MenuItem buttonBatimentEdit;
	private final MenuItem buttonNiveauEdit;
	private final MenuItem buttonAppartEdit;
	private final MenuItem buttonPieceEdit;
	private final MenuItem buttonMurEdit;
	private final MenuItem buttonTypeBatimentEdit;
	private final MenuItem buttonTypeAppartEdit;
	private final MenuItem buttonTypeMurEdit;
	private final MenuItem buttonTypeRevetementEdit;
	private final MenuItem buttonTypeOuvertureNiveauxEdit;
	private final MenuItem buttonTypeOuvertureMurEdit;

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
		this.controller = config.controller;

		///// Menu Fichier

		MenuItem buttonNewProject = new MenuItem("Nouveau...");
		buttonNewProject.setOnAction(event -> {
			config.tui.log("clicked: new project");
			controller.newProject();
		});

		buttonOpenFile = new MenuItem("Ouvrir...");
		buttonOpenFile.setOnAction(event -> {
			config.tui.log("clicked: open file");
			controller.openFile();
		});

		buttonSaveFile = new MenuItem("Enregistrer");
		buttonSaveFile.setOnAction(event -> {
			config.tui.log("clicked: save file");
			controller.saveFile();
		});

		buttonSaveAs = new MenuItem("Enregistrer sous...");
		buttonSaveAs.setOnAction(event -> {
			config.tui.log("clicked: save as");
			controller.saveFileAs();
		});

		menuFichier = new Menu("Fichier", null,
			buttonNewProject,
			buttonOpenFile,
			buttonSaveFile,
			buttonSaveAs
		);

		///// Menu Créer

		buttonBatimentCreer = new MenuItem("Un Batiment...");
		buttonBatimentCreer.setOnAction(event -> {
			Batiment batiment = new Batiment();
			config.controller.menuButtonCreateOne(new Batiment());
		});

		buttonNiveauCreer = new MenuItem("Un Niveau...");
		buttonNiveauCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Niveau");
			config.controller.menuButtonCreateOne(new Niveau());
		});

		buttonAppartCreer = new MenuItem("Un Appartement...");
		buttonAppartCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Appart");
			config.controller.menuButtonCreateOne(new Appart());
		});

		buttonPieceCreer = new MenuItem("Une Pièce...");
		buttonPieceCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Piece");
			config.controller.menuButtonCreateOne(new Piece());
		});

		buttonMurCreer = new MenuItem("Un Mur...");
		buttonMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Mur");
			config.controller.menuButtonCreateOne(new Mur());
		});

		MenuItem buttonMurCreerSerie = new MenuItem("Plusieurs murs...");
		buttonMurCreerSerie.setOnAction(event -> {
			config.tui.log("clicked: create: Mur serie");
			config.controller.menuButtonCreateSerie(new Mur());
		});

		MenuItem buttonPointCreer = new MenuItem("Un Point...");
		buttonPointCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new Point");
			config.controller.menuButtonCreateOne(new Point());
		});

		MenuItem buttonPointCreerSerie = new MenuItem("Plusieurs points...");
		buttonPointCreerSerie.setOnAction(event -> {
			config.tui.log("clicked: create: Point serie");
			config.controller.menuButtonCreateSerie(new Point());
		});

		separatorCreer = new SeparatorMenuItem();

		buttonTypeBatimentCreer = new MenuItem("Un Type Batiment...");
		buttonTypeBatimentCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeBatiment");
			config.controller.menuButtonCreateOne(new TypeBatiment());
		});

		buttonTypeAppartCreer = new MenuItem("Un Type Appart...");
		buttonTypeAppartCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeAppart");
			config.controller.menuButtonCreateOne(new TypeAppart());
		});

		buttonTypeMurCreer = new MenuItem("Un Type Mur...");
		buttonTypeMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeMur");
			config.controller.menuButtonCreateOne(new TypeMur());
		});

		buttonTypeRevetementCreer = new MenuItem("Un Type Revetement...");
		buttonTypeRevetementCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeRevetement...");
			config.controller.menuButtonCreateOne(new TypeRevetement());
		});

		buttonTypeOuvertureNiveauxCreer = new MenuItem("Un Type Ouverture Niveau...");
		buttonTypeOuvertureNiveauxCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeOuvertureNiveau");
			config.controller.menuButtonCreateOne(new TypeOuvertureNiveaux());
		});

		buttonTypeOuvertureMurCreer = new MenuItem("Un Type Ouverture Mur...");
		buttonTypeOuvertureMurCreer.setOnAction(event -> {
			config.tui.log("clicked: create: new TypeOuvertureMur");
			config.controller.menuButtonCreateOne(new TypeOuvertureMur());
		});

		Menu menuCreer = new Menu("Créer", null,
			buttonBatimentCreer,
			buttonNiveauCreer,
			buttonAppartCreer,
			buttonPieceCreer,
			buttonMurCreer,
			buttonMurCreerSerie,
			buttonPointCreer,
			buttonPointCreerSerie,
			separatorCreer,
			buttonTypeBatimentCreer,
			buttonTypeAppartCreer,
			buttonTypeMurCreer,
			buttonTypeRevetementCreer,
			buttonTypeOuvertureNiveauxCreer,
			buttonTypeOuvertureMurCreer
		);

		///// Menu Éditer

		buttonCurrentProjectEdit = new MenuItem("Projet actuel");
		buttonCurrentProjectEdit.setOnAction(event -> {
			config.controller.menuButtonSelectCurrentProject();
		});

		buttonCurrentBatimentEdit = new MenuItem("Batiment actuel");
		buttonCurrentBatimentEdit.setOnAction(event -> {
			config.controller.menuButtonSelectCurrentBatiment();
		});

		buttonCurrentNiveauEdit = new MenuItem("Niveau actuel");
		buttonCurrentNiveauEdit.setOnAction(event -> {
			config.controller.menuButtonSelectCurrentNiveau();
		});

		SeparatorMenuItem separatorEdit1 = new SeparatorMenuItem();

		buttonBatimentEdit = new MenuItem("Un Batiment...");
		buttonBatimentEdit.setOnAction(event -> {
			Set batiments = config.project.objects.getBatiments();
			config.controller.selectFromList("batiment", batiments);
		});

		buttonNiveauEdit = new MenuItem("Un Niveau...");
		buttonNiveauEdit.setOnAction(event -> {
			Set niveaux = config.project.objects.getNiveaux();
			config.controller.selectFromList("niveau", niveaux);
		});

		buttonAppartEdit = new MenuItem("Un Appartement...");
		buttonAppartEdit.setOnAction(event -> {
			Set objects = config.project.objects.getApparts();
			config.controller.selectFromList("appartement", objects);
		});

		buttonPieceEdit = new MenuItem("Une Pièce...");
		buttonPieceEdit.setOnAction(event -> {
			Set objects = config.project.objects.getPieces();
			config.controller.selectFromList("pièce", objects);
		});

		buttonMurEdit = new MenuItem("Un Mur...");
		buttonMurEdit.setOnAction(event -> {
			Set objects = config.project.objects.getMurs();
			config.controller.selectFromList("mur", objects);
		});

		SeparatorMenuItem separatorEdit2 = new SeparatorMenuItem();

		buttonTypeBatimentEdit = new MenuItem("Un Type Batiment...");
		buttonTypeBatimentEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesBatiment();
			config.controller.selectFromList("type de bâtiment", objects);
		});

		buttonTypeAppartEdit = new MenuItem("Un Type Appart...");
		buttonTypeAppartEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesAppart();
			config.controller.selectFromList("type d'appartement", objects);
		});

		buttonTypeMurEdit = new MenuItem("Un Type Mur...");
		buttonTypeMurEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesMur();
			config.controller.selectFromList("type de mur", objects);
		});

		buttonTypeRevetementEdit = new MenuItem("Un Type Revetement...");
		buttonTypeRevetementEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesRevetement();
			config.controller.selectFromList("type de revêtement", objects);
		});

		buttonTypeOuvertureNiveauxEdit = new MenuItem("Un Type Ouverture Niveaux...");
		buttonTypeOuvertureNiveauxEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesOuverturesNiveau();
			config.controller.selectFromList("type d'ouverture-niveau", objects);
		});

		buttonTypeOuvertureMurEdit = new MenuItem("Un Type Ouverture Mur...");
		buttonTypeOuvertureMurEdit.setOnAction(event -> {
			Set objects = config.project.objects.getTypesOuverturesMur();
			config.controller.selectFromList("type d'ouverture-mur", objects);
		});

		Menu menuEditer = new Menu("Éditer", null,
			buttonCurrentProjectEdit,
			buttonCurrentBatimentEdit,
			buttonCurrentNiveauEdit,
			separatorEdit1,
			buttonBatimentEdit,
			buttonNiveauEdit,
			buttonAppartEdit,
			buttonPieceEdit,
			buttonMurEdit,
			separatorEdit2,
			buttonTypeBatimentEdit,
			buttonTypeAppartEdit,
			buttonTypeMurEdit,
			buttonTypeRevetementEdit,
			buttonTypeOuvertureNiveauxEdit,
			buttonTypeOuvertureMurEdit
		);
		
		///// Menu Sélectionner

		MenuItem buttonSelect = new MenuItem("Sélection (outil par défaut)");
		buttonSelect.setOnAction(event -> {
			config.tui.log("clicked: select");
			config.controller.state.setActionState(State.ActionState.DEFAULT);
		});

		buttonMultiSelect = new CheckMenuItem("Sélection de plusieurs objets");
		buttonMultiSelect.setSelected(false);
		buttonMultiSelect.setOnAction(event -> {
			config.tui.log("clicked: multi select");
			config.controller.menuButtonMultiSelect(buttonMultiSelect.isSelected());
		});

		buttonDeleteSelection = new MenuItem("Supprimer la sélection");
		buttonDeleteSelection.setOnAction(event -> {
			config.tui.log("clicked: delete selection");
		});

//		buttonFocusedToRoot = new MenuItem("L'objet sélectionné");
//		buttonFocusedToRoot.setOnAction(event -> {
//			config.tui.log("clicked: focused to root");
//		});

		MenuItem menuChooseNiveauToRoot = new MenuItem("Un niveau...");
		menuChooseNiveauToRoot.setOnAction(event -> {
			config.controller.menuButtonNiveauToRoot();
		});

		MenuItem menuChooseBatiment = new MenuItem("Un batiment...");
		menuChooseBatiment.setOnAction(event -> {
			config.controller.menuButtonChooseBatiment();
		});

		menuSelectionner = new Menu("Sélectionner", null,
			new Menu("Afficher dans le plan...", null,
//				buttonFocusedToRoot,
				menuChooseNiveauToRoot,
				menuChooseBatiment
			),
			buttonSelect,
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

		MenuItem buttonDevisEdit = new MenuItem("Éditer un devis...");
		buttonDevisEdit.setOnAction(event -> {
			Set objects = config.project.objects.getDevis();
			config.controller.selectFromList("devis", objects);
		});

		menuDevis = new Menu("Devis", null,
			buttonDevisTotal,
			buttonDevisSelection,
			buttonSaveDevisAs,
			buttonDevisEdit
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

		buttonFancyRedraw = new MenuItem("Force redraw");
		buttonFancyRedraw.setOnAction(event -> {
			config.tui.log("clicked: redraw");
			config.controller.refreshUI();
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
			controller.menuClickedHelp();
		});

		MenuItem buttonTutor = new MenuItem("Mini-tutoriel");
		buttonTutor.setOnAction(e -> {
			controller.menuClickedTutor();
		});

		buttonApropos = new MenuItem("À propos de " + Config.applicationName);
		buttonApropos.setOnAction(e -> {
			controller.menuClickedApropos();
		});

		separatorAide = new SeparatorMenuItem();

		menuAide = new Menu("Aide", null,
			buttonAide,
			buttonTutor,
			separatorAide,
			buttonApropos
		);

		///// MenuBar

		super.getMenus().addAll(
			menuFichier,
			menuCreer,
			menuEditer,
			menuSelectionner,
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

