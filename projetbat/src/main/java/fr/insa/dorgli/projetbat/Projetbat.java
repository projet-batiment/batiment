package fr.insa.dorgli.projetbat;

public class Projetbat {
	Config config = new Config();

	public void run(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
				case "--version":
				case "-v":
					System.out.println("Batiment  v" + config.version);
					break;

				case "--verbose":
				case "-V":
					config.tui.setLogLevel(TUI.LogLevel.LOG);
					break;

				case "--debug":
					config.tui.setLogLevel(TUI.LogLevel.DEBUG);
					break;

				case "--file":
				case "-f":
					if (i+1 < args.length) {
						i++;
						config.savefilePath = args[i];
						config.tui.debug("main/run: set savefilePath to '" + args[i] + "'");
					}
					break;

				default:
					config.tui.error("main/run: unknown CLI argument: '" + arg + "'");
			}
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.println("main/run: abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture des arguments CLI");
			System.exit(-1);
		}

		if (config.savefilePath != null) {
			Deserialize deserializer = new Deserialize(config);
			deserializer.deserializeFile(config.savefilePath);
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.println("main/run: abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture du fichier de sauvegarde");
			System.exit(-1);
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.println("main/run: " + config.tui.getErrCounter() + " erreurs ont eu lieu lors de l'exécution");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		Projetbat app = new Projetbat();
		app.run(args);
	}
}