package fr.insa.dorgli.projetbat;

import fr.insa.dorgli.projetbat.objects.TypeMur;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Piece;
import fr.insa.dorgli.projetbat.objects.TypeRevetement;
import fr.insa.dorgli.projetbat.objects.Niveau;
import fr.insa.dorgli.projetbat.objects.Appart;
import fr.insa.dorgli.projetbat.objects.Batiment;
import fr.insa.dorgli.projetbat.objects.TypeOuvertureMur;
import fr.insa.dorgli.projetbat.objects.TypeOuvertureNiveau;
import fr.insa.dorgli.projetbat.objects.OuvertureNiveaux;
import fr.insa.dorgli.projetbat.objects.Point;
import fr.insa.dorgli.projetbat.objects.OuvertureMur;
import fr.insa.dorgli.projetbat.objects.PlafondSol;
import fr.insa.dorgli.projetbat.objects.RevetementMur;
import fr.insa.dorgli.projetbat.objects.RevetementPlafondSol;
import fr.insa.dorgli.projetbat.objects.TypeAppart;
import fr.insa.dorgli.projetbat.objects.Mur;
import fr.insa.dorgli.projetbat.objects.TypeBatiment;
import java.io.File;
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
	// - TypesOuvertureNiveau
	// - RevetementsPlafondSol
	// - OuverturesNiveaux
	// - 1_PlafondSol
	// - PlafondSols
	// - Pieces
	//  niveaux
	//  apparts


	// pour faciliter la lecture des regex ci-dessous
	// normalement, java interprète ces variables comme des constantes de compilation
	// pour la suite, flemme de faire des pseudo-optimisations dans java...
	private static final String REGEX_INT = "[0-9]+";
	private static final String REGEX_DOUBLE = "[0-9]+(\\.[0-9]+)?";
	private static final String REGEX_STRING = "[^,]*"; // NB: un String peut être vide, d'où le *

	// pour plus de flexibilité
	private static final String[] ESCAPE_SEQUENCES_REAL = { ",", "\n", "\r", "&" };
	private static final String[] ESCAPE_SEQUENCES_ESCAPED = { "&c", "&n", "&r", "&&" };

	public static String unescapeString(String escaped) {
		for (int i = 0; i < ESCAPE_SEQUENCES_ESCAPED.length; i++)
			escaped = escaped.replaceAll(ESCAPE_SEQUENCES_ESCAPED[i], ESCAPE_SEQUENCES_REAL[i]);

		return escaped;
	}

	public static String escapeString(String incoming) {
		for (int i = 0; i < ESCAPE_SEQUENCES_ESCAPED.length; i++)
			incoming = incoming.replaceAll(ESCAPE_SEQUENCES_REAL[i], ESCAPE_SEQUENCES_ESCAPED[i]);

		return incoming;
	}

	public Deserialize(Config config) {
		this.config = config;
	}

	private void log(String text) {
		config.tui.log(sreader.getLineNumber() + ": " + text);
	}
	private void debug(String text) {
		config.tui.debug(sreader.getLineNumber() + ": " + text);
	}
	private void warn(String text) {
		config.tui.warn(sreader.getLineNumber() + ": " + text);
	}
	private void error(String text) {
		config.tui.error(sreader.getLineNumber() + ": " + text);
	}

	private void errorParse(String text, String errMsg) {
		error("erreur lors de l'interprétation des valeurs: '" + text + "': " + errMsg);
	}
	private void errorSyntax(String text) {
		error("mauvaise syntaxe: '" + text + "'");
	}
	private void errorIdNone(String typeName, int objectId) {
		error("l'ID " + objectId + " n'existe pas en tant que " + typeName );
	}
	private void errorIdAgain(int objectId) {
		error("ID déjà pris ou incorrect: ID " + objectId);
	}

	// Méthode obscure, générique de type T, pour à la fois :
	// 1) convertir le HashMap newMap en un ArrayList, qui est renvoyé à la fin
	// 2) vérifier que les keys du newMap (ie les ID des objets) ne sont pas déjà occupés dans le Hashmap olderMap,
	//    qui contient la liste des objets du type T déjà créés/lus
	// 3) ajouter les paires (id + objet) du newMap au olderMap
	// Cette méthode est utile pour lire les objets qui nécessaitent des objets internes (mur, pièce, plafond, sol, etc), car dans ce
	// cas de figure, on utilise directement les objets sous forme de ArrayList après les avoir lus au lieu de renvoyer un bête HashMap
	private <T> ArrayList<T> manageHashMapToArrayList(HashMap<Integer, T> newMap, HashMap<Integer, T> olderMap) {
		config.tui.diveWhere("manageHashMapToArray");
		ArrayList<T> out = new ArrayList<>();
		for (Integer key: newMap.keySet()) {
			if (olderMap.containsKey(key)) {
				errorIdAgain(key);
			} else {
				out.add(newMap.get(key));
				olderMap.put(key, newMap.get(key));
			}
		}
		config.tui.popWhere();
		return out;
	}

	public Project deserializeFile(String path) throws FileNotFoundException {
		return deserializeFile(new File(path));
	}

	public Project deserializeFile(File file) throws FileNotFoundException {
		config.tui.diveWhere("deserializeFile");
		config.tui.begin();

		Project newProject = new Project();

		sreader = new SmartReader(config.tui, file);

		try {
			for (
				SmartReader.ReadResult result = sreader.readLine();
				result.getState() != SmartReader.ReadState.EOF;
				result = sreader.readLine()
			) {
				//int sectionCount = 1;
				if (result.getState() == SmartReader.ReadState.EOS) {
					error("reached an EOS outside any section !");
				} else {
					String line = result.getText();

					config.tui.clearErrCounter();
					if (line.startsWith("OBJECTS:")) {
						String objectsKind = line.split(":")[1];

						debug("reading objects section " + TUI.blue("'" + objectsKind + "'") + "...");

						switch (objectsKind) {
							case "Batiment" ->		newProject.objects.batiments = batimentsFromString(newProject.objects);
							case "Point" -> 		newProject.objects.points = pointsFromString();
							case "TypeRevetement" -> 	newProject.objects.typesRevetement = typeRevetementsFromString();
							case "TypeOuvertureMur" -> 	newProject.objects.typesOuverturesMur = typeOuvertureMursFromString();
							case "TypeOuvertureNiveau" -> 	newProject.objects.typesOuverturesNiveau = typeOuvertureNiveauxFromString();
							case "TypeMur" -> 		newProject.objects.typesMur = typeMursFromString();
							case "TypeAppart" -> 		newProject.objects.typesAppart = typeAppartsFromString();
							case "TypeBatiment" -> 		newProject.objects.typesBatiment = typeBatimentsFromString();
							case "Mur" -> 			newProject.objects.murs = mursFromString(newProject.objects);
							case "Piece" ->			newProject.objects.pieces = piecesFromString(newProject.objects);
							case "Appart" ->		newProject.objects.apparts = appartsFromString(newProject.objects);
							case "Niveau" ->		newProject.objects.niveaux = niveauxFromString(newProject.objects);
							//case "PlafondSol" -> 		newProject.objects.plafondsSols = plafondSolsFromString(newProject.objects);
							default -> error("section d'objects inconnue: '" + objectsKind + "'");
						}

						if (config.tui.getErrCounter() > 0) {
							error("erreur lors de la lecture de la section d'objets " + objectsKind);
						} else {
							debug("reading objects section '" + objectsKind + "': success");
						}
					} else if (line.startsWith("FILE")) {
						debug("reading " + TUI.blue("FILE") + " statements");
						fileStatements(newProject);
					} else {
						error("section inconnue: '" + line + "'");
					}

					debug("finished reading a section");

					//sectionCount++;
				}

				if (config.tui.getErrCounter() > 0) {
					warn(TUI.red("triggering fake EOF because of " + config.tui.getErrCounter() + " errors"));
					break;
				}
			}

		} catch (IOException e) {
			error("erreur d'entrée/sortie lors de la lecture du fichier '" + file.getPath() + "': " + e.getMessage());
		}

		if (config.tui.getErrCounter() > 0) {
			config.tui.ended(TUI.red(config.tui.getErrCounter() + " errors"));
		} else {
			config.tui.ended(TUI.green("success"));
		}
		debug("Les objets suivants ont été lus:\n" + newProject.objects.toString());

		config.tui.popWhere();
		return newProject;
	}

	/// FileStatements
	private void fileStatements(Project newProject) throws IOException {
		config.tui.diveWhere("fileStatements");
		config.tui.begin();
		String[] command;

		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			debug(result.toString());
			String line = result.getText();
			command = line.split(":", 2);
			switch (command[0]) {
				case "version" -> {
					try {
						int savefileVersion = Integer.parseInt(command[1]);
						if (savefileVersion < config.minimumSavefileVersion) {
							error("savefile is too old (v" + savefileVersion + " < min " + config.minimumSavefileVersion + ")");
						} else if (savefileVersion > config.maximumSavefileVersion) {
							error("savefile is too recent (v" + savefileVersion + " > max " + config.maximumSavefileVersion + ")");
						} else {
							debug("savefile is of correct version " + savefileVersion);
						}
					} catch (NumberFormatException e) {
						errorParse(line, "failed parsing the version as an integer: " + e.getMessage());
					}
				}

				case "projectName" -> {
					String unescaped = unescapeString(command[1]);
					newProject.projectName = unescaped;
					debug("set projectName = '" + unescaped + "'");
				}
				case "projectDescription" -> {
					String unescaped = unescapeString(command[1]);
					newProject.projectDescription = unescaped;
					debug("set projectDescription = '" + unescaped + "'");
				}

				case "default Batiment" -> {
					try {
						int batimentId = Integer.parseInt(command[1]);
						Batiment batiment = newProject.objects.batiments.get(batimentId);
						if (batiment == null) {
							errorIdNone("Batiment", batimentId);
						} else {
							debug("default Batiment read: " + batimentId);
						}
					} catch (NumberFormatException e) {
						errorParse(line, e.getMessage());
					}
				}
				case "default Niveau" -> {
					try {
						int niveauId = Integer.parseInt(command[1]);
						Niveau niveau = newProject.objects.niveaux.get(niveauId);
						if (niveau == null) {
							errorIdNone("Niveau", niveauId);
						} else {
							debug("default Niveau read: " + niveauId);
						}
					} catch (NumberFormatException e) {
						errorParse(line, e.getMessage());
					}
				}

				default -> errorSyntax(line);
			}
		}
		config.tui.ended();
		config.tui.popWhere();
	}

	/// Point
	private HashMap<Integer, Point> pointsFromString() throws IOException {
		config.tui.diveWhere("Points");
		config.tui.begin();
		HashMap<Integer, Point> points = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_INT);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					double x = Double.parseDouble(splitted[1]);
					double y = Double.parseDouble(splitted[2]);
					int niveauId = Integer.parseInt(splitted[3]);

					Point object = new Point(id, x, y, niveauId);
					points.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return points;
	}

	/// TypeRevetement
	private HashMap<Integer, TypeRevetement> typeRevetementsFromString() throws IOException {
		config.tui.diveWhere("TypeRevetements");
		config.tui.begin();
		HashMap<Integer, TypeRevetement> typeRevetements = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					double prixUnitaire = Double.parseDouble(splitted[3]);

					TypeRevetement object = new TypeRevetement(id, unescapeString(splitted[1]), unescapeString(splitted[2]), prixUnitaire);
					typeRevetements.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeRevetements;
	}

	/// TypeOuvertureMur
	private HashMap<Integer, TypeOuvertureMur> typeOuvertureMursFromString() throws IOException {
		config.tui.diveWhere("TypeOuvertureMurs");
		config.tui.begin();
		HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					double hauteur = Double.parseDouble(splitted[3]);
					double largeur = Double.parseDouble(splitted[4]);
					double prixUnitaire = Double.parseDouble(splitted[5]);

					TypeOuvertureMur object = new TypeOuvertureMur(id, unescapeString(splitted[1]), unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
					typeOuvertureMurs.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeOuvertureMurs;
	}

	/// TypeMur
	private HashMap<Integer, TypeMur> typeMursFromString() throws IOException {
		config.tui.diveWhere("TypeMurs");
		config.tui.begin();
		HashMap<Integer, TypeMur> typeMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					double epaisseur = Double.parseDouble(splitted[3]);
					double prixU = Double.parseDouble(splitted[4]);

					TypeMur object = new TypeMur(id, unescapeString(splitted[1]), unescapeString(splitted[2]), epaisseur, prixU);
					typeMurs.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeMurs;
	}

	/// RevetementMur
	private HashMap<Integer, RevetementMur> revetementMursFromString(HashMap<Integer, TypeRevetement> typeRevetements) throws IOException {
		config.tui.diveWhere("RevetementMurs");
		config.tui.begin();
		HashMap<Integer, RevetementMur> revetementMurs = new HashMap<>();

			final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE);
			debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted[1]));
					double p1l = Double.parseDouble(splitted[2]);
					double p1h = Double.parseDouble(splitted[3]);
					double p2l = Double.parseDouble(splitted[4]);
					double p2h = Double.parseDouble(splitted[5]);

					RevetementMur object = new RevetementMur(id, tr, p1l, p1h, p2l, p2h);
					revetementMurs.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return revetementMurs;
	}

	/// OuvertureMur
	private HashMap<Integer, OuvertureMur> ouvertureMursFromString(HashMap<Integer, TypeOuvertureMur> typeOuvertureMurs) throws IOException {
		config.tui.diveWhere("OuvertureMurs");
		config.tui.begin();
		HashMap<Integer, OuvertureMur> ouvertureMurs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_DOUBLE, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					TypeOuvertureMur tr = typeOuvertureMurs.get(Integer.parseInt(splitted[1]));
					double p1l = Double.parseDouble(splitted[2]);
					double p1h = Double.parseDouble(splitted[3]);

					OuvertureMur object = new OuvertureMur(id, tr, p1l, p1h);
					ouvertureMurs.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return ouvertureMurs;
	}

	/// Mur
	private HashMap<Integer, Mur> mursFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Murs");
		config.tui.begin();
		HashMap<Integer, Mur> murs = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_INT, REGEX_DOUBLE, REGEX_INT);
		debug("regex: '" + regex + "'");
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
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du mur
					Point p1 = objects.points.get(Integer.parseInt(splitted[1]));
					Point p2 = objects.points.get(Integer.parseInt(splitted[2]));
					double hauteur = Double.parseDouble(splitted[3]);
					TypeMur typeMur = objects.typesMur.get(Integer.parseInt(splitted[4]));

					// lire les RevetementMur des 2 côtés et les OuvertureMur
					HashMap<Integer, RevetementMur> r1;
					ArrayList<RevetementMur> r1_list = new ArrayList<>();
					HashMap<Integer, RevetementMur> r2;
					ArrayList<RevetementMur> r2_list = new ArrayList<>();
					HashMap<Integer, OuvertureMur> o;
					ArrayList<OuvertureMur> o_list = new ArrayList<>();
					config.tui.diveWhere("props");
					for (
						SmartReader.ReadResult propResult = sreader.readLine();
						propResult.getState() == SmartReader.ReadState.LINE;
						propResult = sreader.readLine()
					) {
						String propType = propResult.getText().replaceFirst("PROP:", "");
						debug("reading mur prop '" + TUI.blue(propType) + "'");
						switch (propType) {
							case "RevetementMur:1" -> {
								r1 = revetementMursFromString(objects.typesRevetement);
								r1_list = manageHashMapToArrayList(r1, objects.revetementsMur);
							}
							case "RevetementMur:2" -> {
								r2 = revetementMursFromString(objects.typesRevetement);
								r2_list = manageHashMapToArrayList(r2, objects.revetementsMur);
							}
							case "OuvertureMur" -> {
								o = ouvertureMursFromString(objects.typesOuverturesMur);
								o_list = manageHashMapToArrayList(o, objects.ouverturesMur);
							}
							default -> error("propriété du mur inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					Mur object = new Mur(id, p1, p2, hauteur, typeMur, r1_list, r2_list, o_list);
					murs.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return murs;
	}

	/// TypeOuvertureNiveau
	private HashMap<Integer, TypeOuvertureNiveau> typeOuvertureNiveauxFromString() throws IOException {
		config.tui.diveWhere("TypeOuvertureNiveaux");
		config.tui.begin();
		HashMap<Integer, TypeOuvertureNiveau> typeOuvertureNiveaux = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeOuvertureNiveaux.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					double hauteur = Double.parseDouble(splitted[3]);
					double largeur = Double.parseDouble(splitted[4]);
					double prixUnitaire = Double.parseDouble(splitted[5]);

					TypeOuvertureNiveau object = new TypeOuvertureNiveau(id, unescapeString(splitted[1]), unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
					typeOuvertureNiveaux.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeOuvertureNiveaux;
	}

	/// RevetementPlafondSol
	private HashMap<Integer, RevetementPlafondSol> revetementPlafondSolsFromString(HashMap<Integer, TypeRevetement> typeRevetements) throws IOException {
		config.tui.diveWhere("RevetementPlafondSols");
		config.tui.begin();
		HashMap<Integer, RevetementPlafondSol> revetementPlafondSols = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (revetementPlafondSols.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted[1]));
					double p1l = Double.parseDouble(splitted[2]);
					double p1h = Double.parseDouble(splitted[3]);
					double p2l = Double.parseDouble(splitted[4]);
					double p2h = Double.parseDouble(splitted[5]);

					RevetementPlafondSol object = new RevetementPlafondSol(id, tr, p1l, p1h, p2l, p2h);
					revetementPlafondSols.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return revetementPlafondSols;
	}

	/// OuvertureNiveaux
	private HashMap<Integer, OuvertureNiveaux> ouvertureNiveauxFromString(HashMap<Integer, TypeOuvertureNiveau> typeOuvertureNiveaux) throws IOException {
		config.tui.diveWhere("OuvertureNiveaux");
		config.tui.begin();
		HashMap<Integer, OuvertureNiveaux> ouvertureNiveaux = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_INT, REGEX_INT, REGEX_INT);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (ouvertureNiveaux.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					TypeOuvertureNiveau tr = typeOuvertureNiveaux.get(Integer.parseInt(splitted[1]));
					int p1l = Integer.parseInt(splitted[2]);
					int p1h = Integer.parseInt(splitted[3]);

					OuvertureNiveaux object = new OuvertureNiveaux(id, tr, p1l, p1h);
					ouvertureNiveaux.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return ouvertureNiveaux;
	}

	/// PlafondSol
	private HashMap<Integer, PlafondSol> plafondSolsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("PlafondSols");
		config.tui.begin();
		HashMap<Integer, PlafondSol> plafondSols = new HashMap<>();

		final String regex = String.join(",", REGEX_INT);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				try {
					int id = Integer.parseInt(splitted[0]);

					if (plafondSols.keySet().contains(id)) {
						errorIdAgain(id);
					}

					// lire les RevetementPlafondSol et les OuvertureNiveaux
					HashMap<Integer, RevetementPlafondSol> r;
					ArrayList<RevetementPlafondSol> r_list = new ArrayList<>();
					HashMap<Integer, OuvertureNiveaux> o;
					ArrayList<OuvertureNiveaux> o_list = new ArrayList<>();
					config.tui.diveWhere("props");
					for (
						SmartReader.ReadResult propResult = sreader.readLine();
						propResult.getState() == SmartReader.ReadState.LINE;
						propResult = sreader.readLine()
					) {
						String propType = propResult.getText().replaceFirst("PROP:", "");
						debug("reading plafondSol prop '" + TUI.blue(propType) + "'");
						switch (propType) {
							case "RevetementPlafondSol" -> {
								r = revetementPlafondSolsFromString(objects.typesRevetement);
								r_list = manageHashMapToArrayList(r, objects.revetementsPlafondSol);
							}
							case "OuvertureNiveaux" -> {
								o = ouvertureNiveauxFromString(objects.typesOuverturesNiveau);
								o_list = manageHashMapToArrayList(o, objects.ouverturesNiveaux);
							}
							default -> error("propriété du plafondSol inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					PlafondSol object = new PlafondSol(id, r_list, o_list);
					plafondSols.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return plafondSols;
	}

	/// Piece
	private HashMap<Integer, Piece> piecesFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Pieces");
		config.tui.begin();
		HashMap<Integer, Piece> pieces = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (pieces.keySet().contains(id)) {
					errorIdAgain(id);
				}

				// lire les points, murs, le plafond et le sol
				ArrayList<Point> points = new ArrayList<>();
				ArrayList<Mur> murs = new ArrayList<>();

				HashMap<Integer, PlafondSol> plafonds;
				ArrayList<PlafondSol> plafonds_list;
				HashMap<Integer, PlafondSol> sols;
				ArrayList<PlafondSol> sols_list;

				// TODO: plusieurs plafonds/sols
				PlafondSol plafond = null;
				PlafondSol sol = null;

				config.tui.diveWhere("props");
				for (
					SmartReader.ReadResult propResult = sreader.readLine();
					propResult.getState() == SmartReader.ReadState.LINE;
					propResult = sreader.readLine()
				) {
					String propType = propResult.getText().replaceFirst("PROP:", "");
					debug("reading piece prop '" + TUI.blue(propType) + "'");
					switch (propType) {
						case "points" -> {
							SmartReader.ReadResult pointsResult = sreader.readLine();
							if (pointsResult.getState() == SmartReader.ReadState.LINE) {
								String text = pointsResult.getText();
								if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
									String[] pointsIds = text.split(",");
									for (String each: pointsIds) {
										try {
											int pointId = Integer.parseInt(each);
											Point point = objects.points.get(pointId);
											if (point == null) {
												errorIdNone("Point", pointId);
											} else {
												points.add(point);
											}
										} catch (NumberFormatException e) {
											errorParse(line, e.getMessage());
										}
									}
								} else {
									errorSyntax(text);
								}
							} else {
								error("LINE expected but received " + result.getState() + " when reading points for Piece");
							}
						}
						case "murs" -> {
							SmartReader.ReadResult mursResult = sreader.readLine();
							if (mursResult.getState() == SmartReader.ReadState.LINE) {
								String text = mursResult.getText();
								if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
									String[] mursIds = text.split(",");
									for (String each: mursIds) {
										try {
											int murId = Integer.parseInt(each);
											Mur mur = objects.murs.get(murId);
											if (mur == null) {
												errorIdNone("Mur", murId);
											} else {
												murs.add(mur);
											}
										} catch (NumberFormatException e) {
											errorParse(line, e.getMessage());
										}
									}
								} else {
									errorSyntax(text);
								}
							} else {
								error("LINE expected but received " + result.getState() + " when reading murs for Piece");
							}
						}
						case "plafond" -> {
							plafonds = plafondSolsFromString(objects);
							plafonds_list = manageHashMapToArrayList(plafonds, objects.plafondsSols);

							// TODO: plusieurs plafonds/sols
							if (!plafonds_list.isEmpty()) {
								plafond = plafonds_list.getFirst();
								debug("using first plafond " + plafond.toString());
							} else {
								debug("plafonds is empty !!!");
							}
						}
						case "sol" -> {
							sols = plafondSolsFromString(objects);
							sols_list = manageHashMapToArrayList(sols, objects.plafondsSols);

							// TODO: plusieurs plafonds/sols
							if (!sols_list.isEmpty()) {
								sol = sols_list.getFirst();
								debug("using first sol " + sol.toString());
							} else {
								debug("sols is empty !!!");
							}
						}
						default -> error("propriété de la piece inconnue: '" + propResult.getText() + "'");
					}
				}

				Piece object = new Piece(id, unescapeString(splitted[1]), unescapeString(splitted[2]), points, murs, plafond, sol);
				pieces.put(id, object);
				debug("read " + object);
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return pieces;
	}

	/// TypeAppart
	private HashMap<Integer, TypeAppart> typeAppartsFromString() throws IOException {
		config.tui.diveWhere("TypeApparts");
		config.tui.begin();
		HashMap<Integer, TypeAppart> typeApparts = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeApparts.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					TypeAppart object = new TypeAppart(id, unescapeString(splitted[1]), unescapeString(splitted[2]));
					typeApparts.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeApparts;
	}

	/// Appart
	private HashMap<Integer, Appart> appartsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Apparts");
		config.tui.begin();
		HashMap<Integer, Appart> apparts = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_INT);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (apparts.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du appart
					TypeAppart ta = objects.typesAppart.get(Integer.parseInt(splitted[3]));

					// lire les Pieces
					ArrayList<Piece> pieces = new ArrayList<>();
					config.tui.diveWhere("props");
					for (
						SmartReader.ReadResult propResult = sreader.readLine();
						propResult.getState() == SmartReader.ReadState.LINE;
						propResult = sreader.readLine()
					) {
						String propType = propResult.getText().replaceFirst("PROP:", "");
						debug("reading appart prop '" + TUI.blue(propType) + "'");
						switch (propType) {
							case "pieces" -> {
								SmartReader.ReadResult piecesResult = sreader.readLine();
								if (piecesResult.getState() == SmartReader.ReadState.LINE) {
									String text = piecesResult.getText();
									if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
										String[] piecesIds = text.split(",");
										for (String each: piecesIds) {
											try {
												int pieceId = Integer.parseInt(each);
												Piece piece = objects.pieces.get(pieceId);
												if (piece == null) {
													errorIdNone("Piece", pieceId);
												} else {
													pieces.add(piece);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e.getMessage());
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading pieces for Appart");
								}
							}
							default -> error("propriété du appart inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					Appart object = new Appart(id, unescapeString(splitted[1]), unescapeString(splitted[2]), pieces, ta);
					apparts.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return apparts;
	}

	/// Niveau
	private HashMap<Integer, Niveau> niveauxFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Niveaux");
		config.tui.begin();
		HashMap<Integer, Niveau> niveaux = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_DOUBLE);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (niveaux.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du niveau
					double hauteur = Double.parseDouble(splitted[3]);

					// lire les Pieces et les Apparts
					ArrayList<Piece> pieces = new ArrayList<>();
					ArrayList<Appart> apparts = new ArrayList<>();
					config.tui.diveWhere("props");
					for (
						SmartReader.ReadResult propResult = sreader.readLine();
						propResult.getState() == SmartReader.ReadState.LINE;
						propResult = sreader.readLine()
					) {
						String propType = propResult.getText().replaceFirst("PROP:", "");
						debug("reading niveau prop '" + TUI.blue(propType) + "'");
						switch (propType) {
							case "apparts" -> {
								SmartReader.ReadResult appartsResult = sreader.readLine();
								if (appartsResult.getState() == SmartReader.ReadState.LINE) {
									String text = appartsResult.getText();
									if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
										String[] appartsIds = text.split(",");
										for (String each: appartsIds) {
											try {
												int appartId = Integer.parseInt(each);
												Appart appart = objects.apparts.get(appartId);
												if (appart == null) {
													errorIdNone("Appart", appartId);
												} else {
													apparts.add(appart);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e.getMessage());
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading apparts for Niveau");
								}
							}
							case "pieces" -> {
								SmartReader.ReadResult piecesResult = sreader.readLine();
								if (piecesResult.getState() == SmartReader.ReadState.LINE) {
									String text = piecesResult.getText();
									if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
										String[] piecesIds = text.split(",");
										for (String each: piecesIds) {
											try {
												int pieceId = Integer.parseInt(each);
												Piece piece = objects.pieces.get(pieceId);
												if (piece == null) {
													errorIdNone("Piece", pieceId);
												} else {
													pieces.add(piece);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e.getMessage());
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading pieces for Appart");
								}
							}
							default -> error("propriété du niveau inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					Niveau object = new Niveau(id, unescapeString(splitted[1]), unescapeString(splitted[2]), hauteur, pieces, apparts);
					niveaux.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return niveaux;
	}

	/// TypeBatiment
	private HashMap<Integer, TypeBatiment> typeBatimentsFromString() throws IOException {
		config.tui.diveWhere("TypeBatiments");
		config.tui.begin();
		HashMap<Integer, TypeBatiment> typeBatiments = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING);
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (typeBatiments.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					TypeBatiment object = new TypeBatiment(id, unescapeString(splitted[1]), unescapeString(splitted[2]));
					typeBatiments.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return typeBatiments;
	}

	/// Batiment
	private HashMap<Integer, Batiment> batimentsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Batiments");
		config.tui.begin();
		HashMap<Integer, Batiment> batiments = new HashMap<>();

		final String regex = String.join(",", REGEX_INT, REGEX_STRING, REGEX_STRING, REGEX_STRING); // TODO: typeBatiment (VALUE->id)
		debug("regex: '" + regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (line.matches(regex)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (batiments.keySet().contains(id)) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du batiment
					TypeBatiment tb = objects.typesBatiment.get(Integer.parseInt(splitted[3]));

					ArrayList<Appart> apparts = new ArrayList<>();
					ArrayList<Niveau> niveaux = new ArrayList<>();
					config.tui.diveWhere("props");
					for (
						SmartReader.ReadResult propResult = sreader.readLine();
						propResult.getState() == SmartReader.ReadState.LINE;
						propResult = sreader.readLine()
					) {
						String propType = propResult.getText().replaceFirst("PROP:", "");
						debug("reading batiment prop '" + TUI.blue(propType) + "'");
						switch (propType) {
							case "apparts" -> {
								SmartReader.ReadResult appartsResult = sreader.readLine();
								if (appartsResult.getState() == SmartReader.ReadState.LINE) {
									String text = appartsResult.getText();
									if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
										String[] appartsIds = text.split(",");
										for (String each: appartsIds) {
											try {
												int appartId = Integer.parseInt(each);
												Appart appart = objects.apparts.get(appartId);
												if (appart == null) {
													errorIdNone("Appart", appartId);
												} else {
													apparts.add(appart);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e.getMessage());
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading apparts for Batiment");
								}
							}
							case "niveaux" -> {
								SmartReader.ReadResult niveauxResult = sreader.readLine();
								if (niveauxResult.getState() == SmartReader.ReadState.LINE) {
									String text = niveauxResult.getText();
									if (text.matches(REGEX_INT + "(," + REGEX_INT + ")*")) {
										String[] niveauxIds = text.split(",");
										for (String each: niveauxIds) {
											try {
												int niveauId = Integer.parseInt(each);
												Niveau niveau = objects.niveaux.get(niveauId);
												if (niveau == null) {
													errorIdNone("Niveau", niveauId);
												} else {
													niveaux.add(niveau);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e.getMessage());
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading niveaux for Batiment");
								}
							}
							default -> error("propriété du batiment inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					Batiment object = new Batiment(id, unescapeString(splitted[1]), unescapeString(splitted[2]), tb, niveaux, apparts);
					batiments.put(id, object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e.getMessage());
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return batiments;
	}
}