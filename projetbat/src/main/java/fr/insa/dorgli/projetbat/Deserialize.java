package fr.insa.dorgli.projetbat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Deserialize {
	Config config;
	BufferedReader reader;

	///// Sommaire de la classe Deserialize
	// 
	// - unescapeString et escapeString : pour permettre des \n, des virgules dans la sauvegarde
	// - fonctions d'erreur
	// 
	// - deserializeFile : lire la sauvegarde depuis une fichier dont le chemin est spécifié en arguments
	// 
	// fonctions de désérialisation par type d'objets, dans l'ordre :
	// - Point
	// - TypeRevetement
	// - TypeOuvertureMur
	// - TypeMur
	// - RevetementMur
	// - OuvertureMur
	// - Mur
	// - Point
	// - Point


	// pour faciliter la lecture des regex ci-dessous
	// normalement, java interprète ces variables comme des constantes de compilation
	// pour la suite, flemme de faire des pseudo-optimisations dans java...
	private final String REGEX_int = "[0-9]+";
	private final String REGEX_double = "[0-9]+(\\.[0-9]+)?";
	private final String REGEX_string = "[^,\n\r]+";

	private String unescapeString(String escaped) {
		return escaped
			.replaceAll("&c", ",")
			.replaceAll("&n", "\n")
			.replaceAll("&r", "\r")
			.replaceAll("&&", "&")
		;
	}

	public Deserialize(Config config) {
		this.config = config;
	}

	private void error(String where, String text) {
		config.tui.error("deserialize/" + where + ": " + text);
	}

	private void errorParse(String where, int lineIdx, String text, String errMsg) {
		error(where, ": erreur lors de l'interprétation des valeurs: ligne " + lineIdx + " '" + text + "': " + errMsg);
	}
	private void errorSyntax(String where, int lineIdx, String text) {
		error(where, ": mauvaise syntaxe: ligne " + lineIdx + " '" + text + "'");
	}
	private void errorId(String where, int lineIdx, int objectId) {
		error(where, ": ID déjà pris ou incorrect: ligne " + lineIdx + ", ID " + objectId);
	}

	public void deserializeFile(String path) {
		final String where = "deserializeFile: ";

		Config newConfig = new Config();
		newConfig.tui = config.tui;

		try {
			reader = new BufferedReader(new FileReader(path));

			int sectionCount = 1;
			String line = reader.readLine();
			while (line != null) {
				config.tui.clearErrCounter();
				if (line.startsWith("OBJECTS:")) {
					String objectsKind = line.split(":")[1];

					ArrayList<String> buffer = new ArrayList<>();
					line = reader.readLine();
					while (line != null && ! line.equals("EOS:" + objectsKind)) {
						buffer.add(line);
						line = reader.readLine();
					}
					config.tui.debug(where + "reading objects section '" + objectsKind + "'...");

					HashMap map;
					switch (objectsKind) {
						case "Point" -> 		newConfig.objects.points = pointsFromString(buffer);
						case "TypeRevetement" -> 	newConfig.objects.typesRevetement = typeRevetementsFromString(buffer);
						case "TypeOuvertureMur" -> 	newConfig.objects.typesOuverturesMur = typeOuvertureMursFromString(buffer);
						case "TypeMur" -> 		newConfig.objects.typesMur = typeMursFromString(buffer);
						case "Mur" -> 			newConfig.objects.murs = mursFromString(
						    buffer,
						    newConfig.objects.points,
						    newConfig.objects.typesRevetement,
						    newConfig.objects.typesMur,
						    newConfig.objects.typesOuverturesMur
						);
						default -> error(where, "section d'objects inconnue: '" + objectsKind + "'");
					}

					if (config.tui.getErrCounter() > 0) {
						error(where, "erreur lors de la lecture de la section d'objets " + objectsKind);
					} else {
						config.tui.debug(where + "reading objects section '" + objectsKind + "': success");
					}
				} else if (line.startsWith("FILE:")) {
					config.tui.log(where + "ici, on a une section FILE, mais on sait pas encore la déchiffrer");

					ArrayList<String> buffer = new ArrayList<>();
					line = reader.readLine();
					while (line != null && ! line.equals("EOS")) {
						buffer.add(line);
						line = reader.readLine();
					}
				} else {
					error(where, "section inconnue: '" + line + "'");

					while (line != null && ! line.startsWith("EOS")) {
						line = reader.readLine();
					}
				}

				line = reader.readLine();
				sectionCount++;
			}

			config.tui.log(where + "les objets suivants ont été lus:\n" + newConfig.objects.toString());

		} catch (IOException e) {
			error(where, "erreur lors de la lecture du fichier '" + path + "': " + e.getMessage());
		}
	}

	/// FileStatements
	private void fileStatements(ArrayList<String> lines, Config newConfig) {
		final String where = "fileStatements";
		String[] command;
		for (int i = 0; i < lines.size(); i++) {
			command = lines.get(i).split(":", 2);
			switch (command[0]) {
				case "version" -> {
					try {
						int savefileVersion = Integer.parseInt(command[1]);
						if (savefileVersion < config.minimumSavefileVersion) {
							error(where, "savefile is too old (v" + savefileVersion + " < min " + config.minimumSavefileVersion + ")");
						} else if (savefileVersion > config.minimumSavefileVersion) {
							error(where, "savefile is too recent (v" + savefileVersion + " > max " + config.maximumSavefileVersion + ")");
						} else {
							config.tui.debug("savefile is of correct version " + savefileVersion);
						}
					} catch (NumberFormatException e) {
						errorParse(where, i, lines.get(i), "failed parsing the version as an integer: " + e.getMessage());
					}
				}
				case "projectName" -> newConfig.projectName = command[1];
				case "projectDescription" -> newConfig.projectDescription = command[1];
				default -> errorSyntax(where, i, lines.get(i));
			}
		}
	}

	/// Point
	private HashMap<Integer, Point> pointsFromString(ArrayList<String> lines) {
		final String where = "Points";
		HashMap<Integer, Point> points = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_double, REGEX_double, REGEX_int))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (points.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						double x = Double.parseDouble(splitted[1]);
						double y = Double.parseDouble(splitted[2]);
						int niveauId = Integer.parseInt(splitted[3]);

						Point object = new Point(x, y, niveauId);
						points.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return points;
	}

	/// TypeRevetement
	private HashMap<Integer, TypeRevetement> typeRevetementsFromString(ArrayList<String> lines) {
		final String where = "TypeRevetements";
		HashMap<Integer, TypeRevetement> typeRevetements = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_string, REGEX_string, REGEX_double))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeRevetements.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						double prixUnitaire = Double.parseDouble(splitted[3]);

						TypeRevetement object = new TypeRevetement(unescapeString(splitted[1]), unescapeString(splitted[2]), prixUnitaire);
						typeRevetements.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return typeRevetements;
	}

	/// TypeOuvertureMur
	private HashMap<Integer, TypeOuvertureMur> typeOuvertureMursFromString(ArrayList<String> lines) {
		final String where = "TypeOuvertureMurs";
		HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_string, REGEX_string, REGEX_double, REGEX_double, REGEX_double))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeOuvertureMurs.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						double hauteur = Double.parseDouble(splitted[3]);
						double largeur = Double.parseDouble(splitted[4]);
						double prixUnitaire = Double.parseDouble(splitted[5]);

						TypeOuvertureMur object = new TypeOuvertureMur(unescapeString(splitted[1]), unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
						typeOuvertureMurs.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return typeOuvertureMurs;
	}

	/// TypeMur
	private HashMap<Integer, TypeMur> typeMursFromString(ArrayList<String> lines) {
		final String where = "TypeMurs";
		HashMap<Integer, TypeMur> typeMurs = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_string, REGEX_string, REGEX_double, REGEX_double))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeMurs.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						double epaisseur = Double.parseDouble(splitted[3]);
						double prixU = Double.parseDouble(splitted[4]);

						TypeMur object = new TypeMur(unescapeString(splitted[1]), unescapeString(splitted[2]), epaisseur, prixU);
						typeMurs.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return typeMurs;
	}

	/// RevetementMur
	private HashMap<Integer, RevetementMur> revetementMursFromString(ArrayList<String> lines, HashMap<Integer, TypeRevetement> typeRevetements) {
		final String where = "RevetementMurs";
		HashMap<Integer, RevetementMur> revetementMurs = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_int, REGEX_int, REGEX_int, REGEX_int, REGEX_int))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (revetementMurs.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted[1]));
						int p1l = Integer.parseInt(splitted[2]);
						int p1h = Integer.parseInt(splitted[3]);
						int p2l = Integer.parseInt(splitted[4]);
						int p2h = Integer.parseInt(splitted[5]);

						RevetementMur object = new RevetementMur(tr, p1l, p1h, p2l, p2h);
						revetementMurs.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return revetementMurs;
	}

	/// OuvertureMur
	private HashMap<Integer, OuvertureMur> ouvertureMursFromString(ArrayList<String> lines, HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs) {
		final String where = "OuvertureMurMurs";
		HashMap<Integer, OuvertureMur> ouvertureMurs = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_int, REGEX_int, REGEX_int, REGEX_int, REGEX_int))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (ouvertureMurs.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						TypeOuvertureMur tr = typeOuvertureMurs.get(Integer.parseInt(splitted[1]));
						int p1l = Integer.parseInt(splitted[2]);
						int p1h = Integer.parseInt(splitted[3]);

						OuvertureMur object = new OuvertureMur(tr, p1l, p1h);
						ouvertureMurs.put(id, object);
					} catch (NumberFormatException e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return ouvertureMurs;
	}

	/// Mur
	private HashMap<Integer, Mur> mursFromString
	(
		ArrayList<String> lines,
		HashMap<Integer, Point> points,
		HashMap<Integer, TypeRevetement> typeRevetements,
		HashMap<Integer, TypeMur> typeMurs,
		HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs
	) {
		final String where = "Murs";
		HashMap<Integer, Mur> murs = new HashMap<>();

		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.matches(String.join(",", REGEX_int, REGEX_int, REGEX_int, REGEX_double, REGEX_int))) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (murs.keySet().contains(id)) {
					errorId(where, i, id);
				} else {
					try {
						// lire les propriétés directes du mur
						Point p1 = points.get(Integer.parseInt(splitted[1]));
						Point p2 = points.get(Integer.parseInt(splitted[2]));
						double hauteur = Integer.parseInt(splitted[3]);
						TypeMur typemur = typeMurs.get(Integer.parseInt(splitted[4]));

						// lire les RevetementMurs (côté 1) spécifiés dans les lignes en dessous
						// NB!! la sous-section se termine avec EOS:RevetementMur,1
						ArrayList<String> innerLines = new ArrayList<>();
						i++;
						line = lines.get(i);
						while (i < lines.size() && !line.equals("EOS:RevetementMur,1")) {
							innerLines.add(line);
							i++;
							line = lines.get(i);
						}
						HashMap<Integer, RevetementMur> revetementMurs1 = revetementMursFromString(innerLines, typeRevetements);

						// lire les RevetementMurs (côté 2) spécifiés dans les lignes en dessous
						// NB!! la sous-section se termine avec EOS:RevetementMur,2
						innerLines = new ArrayList<>();
						i++;
						line = lines.get(i);
						while (i < lines.size() && !line.equals("EOS:RevetementMur,2")) {
							innerLines.add(line);
							i++;
							line = lines.get(i);
						}
						HashMap<Integer, RevetementMur> revetementMurs2 = revetementMursFromString(innerLines, typeRevetements);

						// lire les OuvertureMur spécifiés dans les lignes en dessous
						// NB!! la sous-section se termine avec EOS:OuvertureMur
						innerLines = new ArrayList<>();
						i++;
						line = lines.get(i);
						while (i < lines.size() && !line.equals("EOS:OuvertureMur")) {
							innerLines.add(line);
							i++;
							line = lines.get(i);
						}
						HashMap<Integer, OuvertureMur> ouvertureMurs = ouvertureMursFromString(innerLines, typeOuvertureMurs);

						// watch unescapeString !!
						// Mur object = new Mur(splitted[1], splitted[2], epaisseur, prixU);
						// murs.put(id, object);
					} catch (Throwable e) {
						errorParse(where, i, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, i, line);
			}
		}

		return murs;
	}
}
