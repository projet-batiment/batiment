package fr.insa.dorgli.projetbat.core;

// Config contient une description exhaustive et détaillée de l'état actuel du programme

import fr.insa.dorgli.projetbat.core.control.Controller;
import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.ui.gui.MainWindow;
import javafx.stage.Stage;

// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	public class GuiConfig {
		public static double MIN_WIDTH = 750;
		public static double MIN_HEIGHT = 450;
	}

	// propriétés statiques
	public static String applicationName = "ProjetBat";

	public static int version = 5;
	public static int minimumSavefileVersion = 6;
	public static int maximumSavefileVersion = 6;

	// propriétés constantes
	// TODO: classe UI (abstraite??) pour gérer les logs tui/gui
	private Stage mainStage;
	public final TUI tui;
	public final Controller controller;
	private MainWindow mainWindow;

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

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		if (mainWindow != null)
			this.mainWindow = mainWindow;
		else
			tui.error("config: tried to reassign the mainWindow ! " + this.mainWindow + " !-> " + mainWindow);
	}
}