package fr.insa.dorgli.projetbat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Deserialize {
	Config config;
	SmartReader sreader;

	///// Sommaire de la classe Deserialize
	// 
	// - unescapeString et escapeString : pour permettre des \n, des virgules dans la sauvegarde
	// - fonctions d'erreur
	// - une méthode générique
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
	private static final String REGEX_INT = "[0-9]+";
	private static final String REGEX_DOUBLE = "[0-9]+(\\.[0-9]+)?";
	private static final String REGEX_STRING = "[^,]*"; // NB: un String peut être vide, d'où le *

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

	private void log(String where, String text) {
		config.tui.log("deserialize/" + where, sreader.getLineNumber() + ": " + text);
	}
	private void debug(String where, String text) {
		config.tui.debug("deserialize/" + where, sreader.getLineNumber() + ": " + text);
	}
	private void error(String where, String text) {
		config.tui.error("deserialize/" + where, sreader.getLineNumber() + ": " + text);
	}

	private void errorParse(String where, String text, String errMsg) {
		error(where, "erreur lors de l'interprétation des valeurs: '" + text + "': " + errMsg);
	}
	private void errorSyntax(String where, String text) {
		error(where, "mauvaise syntaxe: '" + text + "'");
	}
	private void errorId(String where, int objectId) {
		error(where, "ID déjà pris ou incorrect: ID " + objectId);
	}

	// Méthode obscure, générique de type T, pour à la fois :
	// 1) convertir le HashMap newMap en un ArrayList, qui est renvoyé à la fin
	// 2) vérifier que les keys du newMap (ie les ID des objets) ne sont pas déjà occupés dans le Hashmap olderMap,
	//    qui contient la liste des objets du type T déjà créés/lus
	// 3) ajouter les paires (id + objet) du newMap au olderMap
	// Cette méthode est utile pour lire les objets qui nécessaitent des objets internes (mur, pièce, plafond, sol, etc), car dans ce
	// cas de figure, on utilise directement les objets sous forme de ArrayList après les avoir lus au lieu de renvoyer un bête HashMap
	private <T> ArrayList<T> manageHashMapToArrayList(String where, HashMap<Integer, T> newMap, HashMap<Integer, T> olderMap) {
		ArrayList<T> out = new ArrayList<>();
		for (Integer key: newMap.keySet()) {
			if (olderMap.containsKey(key)) {
				errorId(where + "/manageHashMapToArrayList", key);
			} else {
				out.add(newMap.get(key));
				olderMap.put(key, newMap.get(key));
			}
		}
		return out;
	}

	public void deserializeFile(String path) throws FileNotFoundException {
		final String where = "deserializeFile";
		config.tui.begin(where);

		Config newConfig = new Config();
		newConfig.tui = config.tui;

		sreader = new SmartReader(config.tui, path);

		try {
			for (
				SmartReader.ReadResult result = sreader.readLine();
				result.getState() != SmartReader.ReadState.EOF;
				result = sreader.readLine()
			) {
				//int sectionCount = 1;
				if (result.getState() == SmartReader.ReadState.EOS) {
					error(where, "reached an EOS outside any section !");
				} else {
					String line = result.getText();

					config.tui.clearErrCounter();
					if (line.startsWith("OBJECTS:")) {
						String objectsKind = line.split(":")[1];

						debug(where, "reading objects section " + TUI.blue("'" + objectsKind + "'") + "...");

						HashMap map;
						switch (objectsKind) {
							case "Point" -> 		newConfig.objects.points = pointsFromString();
							case "TypeRevetement" -> 	newConfig.objects.typesRevetement = typeRevetementsFromString();
							case "TypeOuvertureMur" -> 	newConfig.objects.typesOuverturesMur = typeOuvertureMursFromString();
							case "TypeMur" -> 		newConfig.objects.typesMur = typeMursFromString();
							case "Mur" -> 			newConfig.objects.murs = mursFromString(newConfig.objects);
							default -> error(where, "section d'objects inconnue: '" + objectsKind + "'");
						}

						if (config.tui.getErrCounter() > 0) {
							error(where, "erreur lors de la lecture de la section d'objets " + objectsKind);
						} else {
							debug(where, "reading objects section '" + objectsKind + "': success");
						}
					} else if (line.startsWith("FILE")) {
						debug(where, "reading " + TUI.blue("FILE") + " statements");
						fileStatements(newConfig);
					} else {
						error(where, "section inconnue: '" + line + "'");
					}

					debug(where, "finished reading a section");

					//sectionCount++;
				}
			}

			debug(where, "reached EOF");
			log(where, "les objets suivants ont été lus:\n" + newConfig.objects.toString());

		} catch (IOException e) {
			error(where, "erreur d'entrée/sortie lors de la lecture du fichier '" + path + "': " + e.getMessage());
		}

		config.tui.ended(where);
	}

	/// FileStatements
	private void fileStatements(Config newConfig) throws IOException {
		final String where = "fileStatements";
		String[] command;

		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			debug(where, result.toString());
			String line = result.getText();
			command = line.split(":", 2);
			switch (command[0]) {
				case "version" -> {
					try {
						int savefileVersion = Integer.parseInt(command[1]);
						if (savefileVersion < config.minimumSavefileVersion) {
							error(where, "savefile is too old (v" + savefileVersion + " < min " + config.minimumSavefileVersion + ")");
						} else if (savefileVersion > config.minimumSavefileVersion) {
							error(where, "savefile is too recent (v" + savefileVersion + " > max " + config.maximumSavefileVersion + ")");
						} else {
							debug(where, "savefile is of correct version " + savefileVersion);
						}
					} catch (NumberFormatException e) {
						errorParse(where, line, "failed parsing the version as an integer: " + e.getMessage());
					}
				}
				case "projectName" -> {
					String unescaped = unescapeString(command[1]);
					newConfig.projectName = unescaped;
					log(where, "set projectName = '" + unescaped + "'");
				}
				case "projectDescription" -> {
					String unescaped = unescapeString(command[1]);
					newConfig.projectDescription = unescaped;
					log(where, "set projectDescription = '" + unescaped + "'");
				}
				default -> errorSyntax(where, line);
			}
		}
	}

	/// Point
	private HashMap<Integer, Point> pointsFromString() throws IOException {
		final String where = "Points";
		HashMap<Integer, Point> points = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_INT);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (points.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						double x = Double.parseDouble(splitted[1]);
						double y = Double.parseDouble(splitted[2]);
						int niveauId = Integer.parseInt(splitted[3]);

						Point object = new Point(x, y, niveauId);
						points.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return points;
	}

	/// TypeRevetement
	private HashMap<Integer, TypeRevetement> typeRevetementsFromString() throws IOException {
		final String where = "TypeRevetements";
		HashMap<Integer, TypeRevetement> typeRevetements = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeRevetements.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						double prixUnitaire = Double.parseDouble(splitted[3]);

						TypeRevetement object = new TypeRevetement(unescapeString(splitted[1]), unescapeString(splitted[2]), prixUnitaire);
						typeRevetements.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return typeRevetements;
	}

	/// TypeOuvertureMur
	private HashMap<Integer, TypeOuvertureMur> typeOuvertureMursFromString() throws IOException {
		final String where = "TypeOuvertureMurs";
		HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeOuvertureMurs.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						double hauteur = Double.parseDouble(splitted[3]);
						double largeur = Double.parseDouble(splitted[4]);
						double prixUnitaire = Double.parseDouble(splitted[5]);

						TypeOuvertureMur object = new TypeOuvertureMur(unescapeString(splitted[1]), unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
						typeOuvertureMurs.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return typeOuvertureMurs;
	}

	/// TypeMur
	private HashMap<Integer, TypeMur> typeMursFromString() throws IOException {
		final String where = "TypeMurs";
		HashMap<Integer, TypeMur> typeMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE, REGEX_DOUBLE);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeMurs.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						double epaisseur = Double.parseDouble(splitted[3]);
						double prixU = Double.parseDouble(splitted[4]);

						TypeMur object = new TypeMur(unescapeString(splitted[1]), unescapeString(splitted[2]), epaisseur, prixU);
						typeMurs.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return typeMurs;
	}

	/// RevetementMur
	private HashMap<Integer, RevetementMur> revetementMursFromString(HashMap<Integer, TypeRevetement> typeRevetements) throws IOException {
		final String where = "RevetementMurs";
		HashMap<Integer, RevetementMur> revetementMurs = new HashMap<>();

			final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_INT, REGEX_INT, REGEX_INT, REGEX_INT);
			debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (revetementMurs.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted[1]));
						int p1l = Integer.parseInt(splitted[2]);
						int p1h = Integer.parseInt(splitted[3]);
						int p2l = Integer.parseInt(splitted[4]);
						int p2h = Integer.parseInt(splitted[5]);

						RevetementMur object = new RevetementMur(tr, p1l, p1h, p2l, p2h);
						revetementMurs.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return revetementMurs;
	}

	/// OuvertureMur
	private HashMap<Integer, OuvertureMur> ouvertureMursFromString(HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs) throws IOException {
		final String where = "OuvertureMurs";
		HashMap<Integer, OuvertureMur> ouvertureMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_INT, REGEX_INT);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (ouvertureMurs.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						TypeOuvertureMur tr = typeOuvertureMurs.get(Integer.parseInt(splitted[1]));
						int p1l = Integer.parseInt(splitted[2]);
						int p1h = Integer.parseInt(splitted[3]);

						OuvertureMur object = new OuvertureMur(tr, p1l, p1h);
						ouvertureMurs.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return ouvertureMurs;
	}

	/// Mur
	private HashMap<Integer, Mur> mursFromString (Objects objects) throws IOException {
		final String where = "Murs";
		HashMap<Integer, Mur> murs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_INT, REGEX_DOUBLE, REGEX_INT);
		debug(where, "regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (murs.keySet().contains(id)) {
					errorId(where, id);
				} else {
					try {
						// lire les propriétés directes du mur
						Point p1 = objects.points.get(Integer.parseInt(splitted[1]));
						Point p2 = objects.points.get(Integer.parseInt(splitted[2]));
						double hauteur = Double.parseDouble(splitted[3]);
						TypeMur typeMur = objects.typesMur.get(Integer.parseInt(splitted[4]));

						// lire les RevetementMur des 2 côtés et les OuvertureMur
						HashMap<Integer, RevetementMur> r1 = new HashMap<>();
						ArrayList<RevetementMur> r1_list = new ArrayList<>();
						HashMap<Integer, RevetementMur> r2 = new HashMap<>();
						ArrayList<RevetementMur> r2_list = new ArrayList<>();
						HashMap<Integer, OuvertureMur> o = new HashMap<>();
						ArrayList<OuvertureMur> o_list = new ArrayList<>();
						for (
							SmartReader.ReadResult propResult = sreader.readLine();
							propResult.getState() == SmartReader.ReadState.LINE;
							propResult = sreader.readLine()
						) {
							switch (propResult.getText()) {
								case "PROP:RevetementMur:1" -> {
									debug(where, "reading mur prop " + TUI.blue("revetements [1]"));
									r1 = revetementMursFromString(objects.typesRevetement);
									r1_list = manageHashMapToArrayList(where, r1, objects.revetementsMur);
								}
								case "PROP:RevetementMur:2" -> {
									debug(where, "reading mur prop " + TUI.blue("revetements [2]"));
									r2 = revetementMursFromString(objects.typesRevetement);
									r2_list = manageHashMapToArrayList(where, r2, objects.revetementsMur);
								}
								case "PROP:OuvertureMur" -> {
									debug(where, "reading mur prop " + TUI.blue("ouvertures"));
									o = ouvertureMursFromString(objects.typesOuverturesMur);
									o_list = manageHashMapToArrayList(where, o, objects.ouverturesMur);
								}
								default -> error(where, "propriété du mur inconnue: '" + propResult.getText() + "'");
							}
						}

						Mur object = new Mur(p1, p2, hauteur, typeMur, r1_list, r2_list, o_list);
						murs.put(id, object);
						debug(where, "read " + object);
					} catch (NumberFormatException e) {
						errorParse(where, line, e.getMessage());
					}
				}
			} else {
				errorSyntax(where, line);
			}
		}

		return murs;
	}
}
