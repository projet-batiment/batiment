package fr.insa.dorgli.projetbat;

import java.io.FileNotFoundException;

public class Projetbat {
	Config config = new Config();

	public void run(String[] args) {
		final String where = "main/run";

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
				case "--version", "-v" -> System.out.println("Batiment - v" + config.version);
				case "--verbose", "-V", "--log" -> config.tui.setLogLevel(TUI.LogLevel.LOG);

				case "--debug" -> config.tui.setLogLevel(TUI.LogLevel.DEBUG);
				case "--file", "-f" -> {
					if (i+1 < args.length) {
						i++;
						config.savefilePath = args[i];
						config.tui.debug(where, "set savefilePath to '" + args[i] + "'");
					}
				}

				default -> config.tui.error(where, "unknown CLI argument: '" + arg + "'");
			}
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.log(where, "abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture des arguments CLI");
			System.exit(-1);
		}

		if (config.savefilePath != null) {
			try {
				Deserialize deserializer = new Deserialize(config);
				deserializer.deserializeFile(config.savefilePath);
			} catch (FileNotFoundException ex) {
				config.tui.error(where, "le fichier de sauvegarde '" + config.savefilePath + "' n'existe pas: étape ignorée");
			}
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.log(where, "abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture du fichier de sauvegarde");
			System.exit(-1);
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.log(where, config.tui.getErrCounter() + " erreurs ont eu lieu lors de l'exécution");
			System.exit(-1);
		}

		config.tui.ended(where);
	}

	public static void main(String[] args) {
		Projetbat app = new Projetbat();
		app.run(args);
	}
}