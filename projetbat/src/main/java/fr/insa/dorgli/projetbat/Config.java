package fr.insa.dorgli.projetbat;

// Config contient une description exhaustive et détaillée de l'état actuel du programme
// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	TUI tui = new TUI();
	final String version = "0.0.1";

	String savefilePath;

	Objects objects = new Objects();
}