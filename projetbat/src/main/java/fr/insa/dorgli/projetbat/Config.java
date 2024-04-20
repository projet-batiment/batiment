package fr.insa.dorgli.projetbat;

// Config contient une description exhaustive et détaillée de l'état actuel du programme
// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	TUI tui = new TUI();

	final int version = 3;
	final int minimumSavefileVersion = 2;
	final int maximumSavefileVersion = 2;

	String projectName;
	String projectDescription;

	String savefilePath;

	Objects objects = new Objects();
}