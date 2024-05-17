package fr.insa.dorgli.projetbat;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.ui.gui.MainPane;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Projetbat extends Application {
	Config config = new Config();

//	private void argVersion() { System.out.println("Batiment - v" + config.version); }
//	private void argFile(String path) {
//		config.savefilePath = path.replaceFirst("^~", System.getProperty("user.home"));
//		config.tui.debug("set savefilePath to '" + config.savefilePath + "'");
//	}

	@Override
	public void start(Stage mainStage) {
		config.tui.diveWhere("Run");
		config.setMainStage(mainStage);

		mainStage.setMinWidth(Config.GuiConfig.MIN_WIDTH);
		mainStage.setMinHeight(Config.GuiConfig.MIN_HEIGHT);

		Scene scene = new Scene(new MainPane(config, mainStage));
		mainStage.setScene(scene);
		mainStage.setTitle("Nouveau");
		mainStage.show();

//		if (config.savefilePath != null) {
//			try {
//				Deserialize deserializer = new Deserialize(config);
//				deserializer.deserializeFile(config.savefilePath);
//			} catch (FileNotFoundException ex) {
//				config.tui.error("le fichier de sauvegarde '" + config.savefilePath + "' n'existe pas: étape ignorée");
//			}
//		}
//
//		if (config.tui.getErrCounter() > 0) {
//			config.tui.warn("abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture du fichier de sauvegarde");
//			System.exit(-1);
//		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.warn(config.tui.getErrCounter() + " erreurs ont eu lieu lors de l'exécution");
			System.exit(-1);
		}

		config.tui.ended();
	}

	@Override
	public void init() {
		config.tui.diveWhere("Init");

		if (!getParameters().getRaw().isEmpty()) {
			config.tui.warn("Got CLI arguments that aren't processed ! the arguments are:");
			for (String each: getParameters().getRaw()) {
				config.tui.warn("'" + each + "'");
			}
		}

//		for (int i = 0; i < args.length; i++) {
//			String arg = args[i];
//			if (arg.matches("-[^-]+")) {
//				for (int shortArgIdx=1; shortArgIdx < arg.length(); shortArgIdx++){
//					char shortArg = arg.charAt(shortArgIdx);
//					switch (shortArg) {
//						case 'v' -> argVersion();
//						case 'V' -> config.tui.setLogLevel(TUI.LogLevel.LOG);
//						case 'f' -> {
//							if (shortArgIdx + 1 == arg.length()) {
//								if (i+1 < args.length) {
//									i++;
//									argFile(args[i]);
//									config.tui.debug("read file with indirect short argument");
//								} else {
//									config.tui.error("expected a filename after argument -f");
//								}
//							} else {
//								argFile(arg.substring(shortArgIdx+1));
//								shortArgIdx = arg.length();
//								config.tui.debug("read file with direct short argument");
//							}
//						}
//						default -> config.tui.error("unknown CLI short-argument: '" + shortArg + "' in '" + arg + "'");
//					}
//				}
//			} else {
//				switch (arg) {
//					case "--version" -> argVersion();
//					case "--verbose" -> config.tui.setLogLevel(TUI.LogLevel.LOG);
//					case "--debug" -> config.tui.setLogLevel(TUI.LogLevel.DEBUG);
//					case "--trace" -> config.tui.setLogLevel(TUI.LogLevel.TRACE);
//					case "--file" -> {
//						if (i+1 < args.length) {
//							i++;
//							argFile(args[i]);
//						} else {
//							config.tui.error("expected a filename after argument --file");
//						}
//					}
//					default -> config.tui.error("unknown CLI argument: '" + arg + "'");
//				}
//			}
//		}
//
//		if (config.tui.getErrCounter() > 0) {
//			config.tui.warn("abortion de l'exécution à cause de " + config.tui.getErrCounter() + " erreurs lors de la lecture des arguments CLI");
//			System.exit(-1);
//		}

		config.tui.popWhere();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}