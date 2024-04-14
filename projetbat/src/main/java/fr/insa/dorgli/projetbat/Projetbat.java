package fr.insa.dorgli.projetbat;

public class Projetbat {
	Config config = new Config();

	public void run(String[] args) {
		config.tui.println("Hello world!");
	}

	public static void main(String[] args) {
		Projetbat app = new Projetbat();
		app.run(args);
	}
}