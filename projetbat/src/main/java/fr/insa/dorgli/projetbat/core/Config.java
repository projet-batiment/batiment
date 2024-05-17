package fr.insa.dorgli.projetbat.core;

// Config contient une description exhaustive et détaillée de l'état actuel du programme

import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.ui.gui.MainPane;
import javafx.stage.Stage;

// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	public class GuiConfig {
		public static double MIN_WIDTH = 500;
		public static double MIN_HEIGHT = 400;
	}

	// propriétés statiques
	public static String applicationName = "ProjetBat";

	public static int version = 4;
	public static int minimumSavefileVersion = 4;
	public static int maximumSavefileVersion = 4;

	// propriétés constantes
	// TODO: classe UI (abstraite??) pour gérer les logs tui/gui
	private Stage mainStage;
	public final TUI tui;
	public final Controller controller;
	private MainPane mainPane;

	// propriétés variables
	public Project project;

	public Config() {
		this(TUI.LogLevel.NORMAL, new Project());
	}

	public Config(TUI.LogLevel loglevel, Project project) {
		this.tui = new TUI(loglevel);
 		this.controller = new Controller(this);
		this.project = project;
	}

	public Stage getMainStage() {
		return mainStage;
	}

	public void setMainStage(Stage mainStage) {
		if (mainStage != null)
			this.mainStage = mainStage;
		else
			tui.error("config: tried to reassign the mainStage ! " + this.mainStage + " !-> " + mainStage);
	}

	public MainPane getMainPane() {
		return mainPane;
	}

	public void setMainPane(MainPane mainPane) {
		if (mainPane != null)
			this.mainPane = mainPane;
		else
			tui.error("config: tried to reassign the mainPane ! " + this.mainPane + " !-> " + mainPane);
	}
}