package fr.insa.dorgli.projetbat.core;

// Config contient une description exhaustive et détaillée de l'état actuel du programme

import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.ui.gui.MainPane;

// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	public class GuiConfig {
		public static double MIN_WIDTH = 500;
		public static double MIN_HEIGHT = 400;
	}

	public final int version = 4;
	public final int minimumSavefileVersion = 4;
	public final int maximumSavefileVersion = 4;

	// TODO: classe UI (abstraite??) pour gérer les logs tui/gui
	public final TUI tui = new TUI(TUI.LogLevel.DEBUG);
	public final State state = new State();
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