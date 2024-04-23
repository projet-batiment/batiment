package fr.insa.dorgli.projetbat;

import java.io.FileNotFoundException;

public class Projetbat {
	Config config = new Config();
	final String where = "main/run";

	private void argVersion() { System.out.println("Batiment - v" + config.version); }
	private void argFile(String path) {
		config.savefilePath = path.replaceFirst("^~", System.getProperty("user.home"));
		config.tui.debug(where, "set savefilePath to '" + config.savefilePath + "'");
	}

	public void run(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.matches("-[^-]+")) {
				for (int shortArgIdx=1; shortArgIdx < arg.length(); shortArgIdx++){
					char shortArg = arg.charAt(shortArgIdx);
					switch (shortArg) {
						case 'v' -> argVersion();
						case 'V' -> config.tui.setLogLevel(TUI.LogLevel.LOG);
						case 'f' -> {
							if (shortArgIdx + 1 == arg.length()) {
								if (i+1 < args.length) {
									i++;
									argFile(args[i]);
									config.tui.debug(where, "read file with indirect short argument");
								} else {
									config.tui.error(where, "expected a filename after argument -f");
								}
							} else {
								argFile(arg.substring(shortArgIdx+1));
								shortArgIdx = arg.length();
								config.tui.debug(where, "read file with direct short argument");
							}
						}
						default -> config.tui.error(where, "unknown CLI short-argument: '" + shortArg + "' in '" + arg + "'");
					}
				}
			} else {
				switch (arg) {
					case "--version" -> argVersion();
					case "--verbose" -> config.tui.setLogLevel(TUI.LogLevel.LOG);
					case "--debug" -> config.tui.setLogLevel(TUI.LogLevel.DEBUG);
					case "--file" -> {
						if (i+1 < args.length) {
							i++;
							argFile(args[i]);
						} else {
							config.tui.error(where, "expected a filename after argument --file");
						}
					}
					default -> config.tui.error(where, "unknown CLI argument: '" + arg + "'");
				}
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