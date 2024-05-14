package fr.insa.dorgli.projetbat;

// Config contient une description exhaustive et détaillée de l'état actuel du programme
// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	public class GuiConfig {
		public static double MIN_WIDTH = 500;
		public static double MIN_HEIGHT = 400;
	}

	final int version = 4;
	final int minimumSavefileVersion = 4;
	final int maximumSavefileVersion = 4;

	// TODO: classe UI (abstraite??) pour gérer les logs tui/gui
	public TUI tui = new TUI(TUI.LogLevel.DEBUG);

	public Project project = new Project();
}