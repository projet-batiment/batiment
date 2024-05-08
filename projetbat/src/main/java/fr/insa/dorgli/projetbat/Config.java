package fr.insa.dorgli.projetbat;

// Config contient une description exhaustive et détaillée de l'état actuel du programme

import fr.insa.dorgli.projetbat.gui.MainPane;

// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	public class GuiConfig {
		public static double MIN_WIDTH = 500;
		public static double MIN_HEIGHT = 400;
	}

	final int version = 4;
	final int minimumSavefileVersion = 2;
	final int maximumSavefileVersion = 3;

	// TODO: classe UI (abstraite??) pour gérer les logs tui/gui
	public TUI tui = new TUI(TUI.LogLevel.DEBUG);

	public Project project = new Project();
	private MainPane mainPane = null;

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