package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.types.*;
import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Project;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableRoot;
import fr.insa.dorgli.projetbat.utils.CsvRegexMatcher;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import fr.insa.dorgli.projetbat.utils.SmartReader;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Deserialize {
	Config config;
	SmartReader sreader;
	ArrayList<String> errorMessages = new ArrayList<>();

	///// Sommaire de la classe Deserialize
	// 
	// - fonctions d'erreur
	//
	// - une classe PseudoPoint pour lire temporairement les points et ensuite leur attribuer un réel niveau
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

	public Deserialize(Config config) {
		this.config = config;
	}

	static private CsvRegexMatcher idCsvRegexMatcher(String order) {
		return new CsvRegexMatcher("i" + order);
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
		errorMessages.add(text);
	}

	private void errorParse(String text, Exception e) {
		error("erreur lors de l'interprétation des valeurs: '" + text + "': ");
		e.printStackTrace(System.out);
	}
	private void errorSyntax(String text) {
		error("mauvaise syntaxe: '" + text + "'");
	}
	private void errorIdNone(String typeName, int objectId) {
		error("l'ID " + objectId + " n'existe pas (" + typeName  + ")");
	}
	private void errorIdAgain(int objectId) {
		error("ID déjà pris ou incorrect: ID " + objectId);
	}
	private void errorIdWrongType(String typeName, int objectId) {
		error("l'ID " + objectId + " est occupé par un objet qui n'est PAS de type " + typeName);
	}

	private class PseudoPoint {
		Point point;
		int niveauId;

		public PseudoPoint(int id, double x, double y, int niveauId) {
			this.point = new Point(id, x, y, null);
			this.niveauId = niveauId;
		}

		public Point getPoint() {
			return point;
		}

 		public void setNiveau(Objects objects) {
			SelectableId propObject = objects.get(niveauId);
			if (propObject instanceof Niveau niveau) {
				point.setNiveau(niveau);
				debug("pseudoPoint: set niveau of point " + point.toString());
			} else if (propObject == null) {
				errorIdNone("Niveau", niveauId);
			} else {
				debug("le point " + point.toStringShort() + " pointe un niveau inexistant (id " + niveauId + ")");
				errorIdWrongType("Niveau", niveauId);
			}
		}

		@Override
		public String toString() {
			return new StructuredToString.OfFancyToStrings(getClass().getSimpleName())
			    .field("niveauId", String.valueOf(niveauId))
			    .field("point", point.toString(1))
			    .getValue();
		}
	}

	public class LoadedProject extends Project {
		public Batiment currentBatiment; 
		public Niveau currentNiveau; 
		public DrawableRoot viewRootElement; 
	}

	public class Result {
		public enum Status {
			SUCCESS,
			FILE_NOT_FOUND,
			PARSE_ERROR,
			UNEXPECTED_ERROR,
		}

		public final Status status;
		public final LoadedProject project;
		public final Exception exception;
		public final String[] messages;

		// bare OK
		public Result(LoadedProject project) {
			this(Status.SUCCESS, project, null, null);
		}
		// bare ERROR
		public Result(Status status) {
			this(status, null, null, null);
		}
		// ERROR with messages
		public Result(Status status, String[] messages) {
			this(status, null, messages, null);
		}
		// ERROR with exception
		public Result(Status status, Exception exception) {
			this(status, null, null, exception);
		}
		// private all-in-one
		private Result(Status status, LoadedProject project, String[] messages, Exception exception) {
			this.status = status;
			this.project = project;
			this.messages = messages;
			this.exception = exception;
		}
	}

	public Result deserializeFile(String path) {
		return deserializeFile(new File(path));
	}

	public Result deserializeFile(File file) {
		config.tui.diveWhere("deserializeFile");
		config.tui.begin();

		LoadedProject newLoadedProject = new LoadedProject();
		ArrayList<PseudoPoint> pseudoPoints = new ArrayList<>();

		try {
			sreader = new SmartReader(config.tui, file);
		} catch (FileNotFoundException e) {
			return config.tui.popWhere( new Result(Result.Status.FILE_NOT_FOUND) );
		}

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
							case "Batiment" ->		batimentsFromString(newLoadedProject.objects);
							case "Point" -> 		pseudoPoints = pointsFromString(newLoadedProject.objects);
							case "TypeRevetement" -> 	typeRevetementsFromString(newLoadedProject.objects);
							case "TypeOuvertureMur" -> 	typeOuvertureMursFromString(newLoadedProject.objects);
							case "TypeOuvertureNiveau" -> 	typeOuvertureNiveauxFromString(newLoadedProject.objects);
							case "TypeMur" -> 		typeMursFromString(newLoadedProject.objects);
							case "TypeAppart" -> 		typeAppartsFromString(newLoadedProject.objects);
							case "TypeBatiment" -> 		typeBatimentsFromString(newLoadedProject.objects);
							case "Mur" -> 			mursFromString(newLoadedProject.objects);
							case "Piece" ->			piecesFromString(newLoadedProject.objects);
							case "Appart" ->		appartsFromString(newLoadedProject.objects);
							case "Niveau" ->		niveauxFromString(newLoadedProject.objects);
							//case "PlafondSol" -> 		newLoadedProject.objects.plafondsSols = plafondSolsFromString(newLoadedProject.objects);
							default -> error("section d'objects inconnue: '" + objectsKind + "'");
						}

						if (config.tui.getErrCounter() > 0) {
							error("erreur lors de la lecture de la section d'objets " + objectsKind);
						} else {
							debug("reading objects section '" + objectsKind + "': success");
						}
					} else if (line.startsWith("FILE")) {
						debug("reading " + TUI.blue("FILE") + " statements");
						fileStatements(newLoadedProject);
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

			for (PseudoPoint p: pseudoPoints) {
				p.setNiveau(newLoadedProject.objects);
			}

		} catch (Exception e) {
			error("erreur inattendue lors de la lecture du fichier '" + file.getPath() + "': " + e.getMessage());
			return config.tui.popWhere( new Result(Result.Status.UNEXPECTED_ERROR, e) );
		}

		debug("Les objets suivants ont été lus:\n" + newLoadedProject.objects.toString());

		if (config.tui.getErrCounter() > 0) {
			config.tui.ended(TUI.red(config.tui.getErrCounter() + " errors"));
			return config.tui.popWhere( new Result(Result.Status.PARSE_ERROR, errorMessages.toArray(String[]::new)) );
		} else {
			config.tui.ended(TUI.green("success"));
	}

		return config.tui.popWhere( new Result(newLoadedProject) );
	}

	/// FileStatements
	private void fileStatements(LoadedProject newLoadedProject) throws IOException {
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
						if (savefileVersion < Config.minimumSavefileVersion) {
							error("savefile is too old (v" + savefileVersion + " < min " + Config.minimumSavefileVersion + ")");
						} else if (savefileVersion > Config.maximumSavefileVersion) {
							error("savefile is too recent (v" + savefileVersion + " > max " + Config.maximumSavefileVersion + ")");
						} else {
							debug("savefile is of correct version " + savefileVersion);
						}
					} catch (NumberFormatException e) {
						errorParse(line, e);
					}
				}

				case "projectName" -> {
					String unescaped = EscapeStrings.unescapeString(command[1]);
					newLoadedProject.projectName = unescaped;
					debug("set projectName = '" + unescaped + "'");
				}

				case "projectDescription" -> {
					String unescaped = EscapeStrings.unescapeString(command[1]);
					newLoadedProject.projectDescription = unescaped;
					debug("set projectDescription = '" + unescaped + "'");
				}

				case "last viewRootElement" -> {
					try {
						String[] splitted = command[1].split(":");
						int batimentId = Integer.parseInt(splitted[0]);
						int rootId = Integer.parseInt(splitted[1]);

						SelectableId batimentObject = newLoadedProject.objects.get(batimentId);
						if (batimentObject instanceof Batiment batiment) {
							debug("last batiment read: " + batiment);
							newLoadedProject.currentBatiment = batiment;
						} else if (batimentObject == null) {
							errorIdNone("Batiment", batimentId);
						} else {
							errorIdWrongType("Batiment", batimentId);
						}

						SelectableId rootObject = newLoadedProject.objects.get(rootId);
						if (rootObject instanceof DrawableRoot drawable) {
							debug("last viewRootElement read: " + drawable);
							newLoadedProject.viewRootElement = drawable;
						} else if (rootObject == null) {
							errorIdNone("DrawableRoot", rootId);
						} else {
							errorIdWrongType("DrawableRoot", rootId);
						}
					} catch (NumberFormatException e) {
						errorParse(line, e);
					}
				}

				default -> errorSyntax(line);
			}
		}
		config.tui.ended();
		config.tui.popWhere();
	}

	/// Point
	private ArrayList<PseudoPoint> pointsFromString(Objects objects) throws IOException {
		config.tui.diveWhere("Points");
		config.tui.begin();
		ArrayList<PseudoPoint> pseudoPoints = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ddi");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					double x = Double.parseDouble(splitted[1]);
					double y = Double.parseDouble(splitted[2]);
					int niveauId = Integer.parseInt(splitted[3]);

					PseudoPoint pseudoObject = new PseudoPoint(id, x, y, niveauId);
					pseudoPoints.add(pseudoObject);
					objects.put(pseudoObject.getPoint());
					debug("read pseudo object " + pseudoObject);
				} catch (NumberFormatException e) {
					errorParse(line, e);
				}
			} else {
				errorSyntax(line);
			}
		}

		config.tui.ended();
		config.tui.popWhere();
		return pseudoPoints;
	}

	/// TypeRevetement
	private ArrayList<TypeRevetement> typeRevetementsFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeRevetements");
		config.tui.begin();
		ArrayList<TypeRevetement> typeRevetements = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					double prixUnitaire = Double.parseDouble(splitted[3]);

					TypeRevetement object = new TypeRevetement(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), prixUnitaire);
					typeRevetements.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<TypeOuvertureMur> typeOuvertureMursFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeOuvertureMurs");
		config.tui.begin();
		ArrayList<TypeOuvertureMur> typeOuvertureMurs = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssddd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					double hauteur = Double.parseDouble(splitted[3]);
					double largeur = Double.parseDouble(splitted[4]);
					double prixUnitaire = Double.parseDouble(splitted[5]);

					TypeOuvertureMur object = new TypeOuvertureMur(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
					typeOuvertureMurs.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<TypeMur> typeMursFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeMurs");
		config.tui.begin();
		ArrayList<TypeMur> typeMurs = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssdd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					double epaisseur = Double.parseDouble(splitted[3]);
					double prixU = Double.parseDouble(splitted[4]);

					TypeMur object = new TypeMur(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), epaisseur, prixU);
					typeMurs.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<RevetementMur> revetementMursFromString(Objects objects) throws IOException {
		config.tui.diveWhere("RevetementMurs");
		config.tui.begin();
		ArrayList<RevetementMur> revetementMurs = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("idddd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					int trId = Integer.parseInt(splitted[1]);
					SelectableId propObject = objects.get(trId);
					if (propObject instanceof TypeRevetement tr) {
						double p1l = Double.parseDouble(splitted[2]);
						double p1h = Double.parseDouble(splitted[3]);
						double p2l = Double.parseDouble(splitted[4]);
						double p2h = Double.parseDouble(splitted[5]);

						RevetementMur object = new RevetementMur(id, tr, p1l, p1h, p2l, p2h);
						revetementMurs.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (propObject == null) {
						errorIdNone("TypeOuvertureNiveau", trId);
					} else {
						errorIdWrongType("TypeOuvertureNiveau", trId);
					}
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<OuvertureMur> ouvertureMursFromString(Objects objects) throws IOException {
		config.tui.diveWhere("OuvertureMurs");
		config.tui.begin();
		ArrayList<OuvertureMur> ouvertureMurs = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("idd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					int toId = Integer.parseInt(splitted[1]);
					SelectableId propObject = objects.get(toId);
					if (propObject instanceof TypeOuvertureMur to) {
						double p1l = Double.parseDouble(splitted[2]);
						double p1h = Double.parseDouble(splitted[3]);

						OuvertureMur object = new OuvertureMur(id, to, p1l, p1h);
						ouvertureMurs.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (propObject == null) {
						errorIdNone("TypeOuvertureNiveau", toId);
					} else {
						errorIdWrongType("TypeOuvertureNiveau", toId);
					}
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<Mur> mursFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Murs");
		config.tui.begin();
		ArrayList<Mur> murs = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("iidi");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du mur
					Point p1 = null;
					Point p2 = null;
					double hauteur = Double.parseDouble(splitted[3]);
					TypeMur tm = null;

					int point1Id = Integer.parseInt(splitted[1]);
					SelectableId point1Object = objects.get(point1Id);
					if (point1Object instanceof Point point) {
						p1 = point;
					} else if (point1Object == null) {
						errorIdNone("Point", point1Id);
					} else {
						errorIdWrongType("Point", point1Id);
					}

					int point2Id = Integer.parseInt(splitted[2]);
					SelectableId point2Object = objects.get(point2Id);
					if (point2Object instanceof Point point) {
						p2 = point;
					} else if (point2Object == null) {
						errorIdNone("Point", point2Id);
					} else {
						errorIdWrongType("Point", point2Id);
					}

					int tmId = Integer.parseInt(splitted[4]);
					SelectableId tmObject = objects.get(tmId);
					if (tmObject instanceof TypeMur typeMur) {
						tm = typeMur;
					} else if (tmObject == null) {
						errorIdNone("Point", tmId);
					} else {
						errorIdWrongType("Point", tmId);
					}

					if (p1 != null && p2 != null && tm != null) {
						// lire les RevetementMur des 2 côtés et les OuvertureMur
						ArrayList<RevetementMur> r1_list = new ArrayList<>();
						ArrayList<RevetementMur> r2_list = new ArrayList<>();
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
									r1_list = revetementMursFromString(objects);
								}
								case "RevetementMur:2" -> {
									r2_list = revetementMursFromString(objects);
								}
								case "OuvertureMur" -> {
									o_list = ouvertureMursFromString(objects);
								}
								default -> error("propriété du mur inconnue: '" + propResult.getText() + "'");
							}
						}
						config.tui.popWhere();

						Mur object = new Mur(id, p1, p2, hauteur, tm, r1_list, r2_list, o_list);
						murs.add(object);
						objects.put(object);
						debug("read " + object);
					}
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<TypeOuvertureNiveau> typeOuvertureNiveauxFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeOuvertureNiveaux");
		config.tui.begin();
		ArrayList<TypeOuvertureNiveau> typeOuvertureNiveaux = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssddd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					double hauteur = Double.parseDouble(splitted[3]);
					double largeur = Double.parseDouble(splitted[4]);
					double prixUnitaire = Double.parseDouble(splitted[5]);

					TypeOuvertureNiveau object = new TypeOuvertureNiveau(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), hauteur, largeur, prixUnitaire);
					typeOuvertureNiveaux.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<RevetementPlafondSol> revetementPlafondSolsFromString(Objects objects) throws IOException {
		config.tui.diveWhere("RevetementPlafondSols");
		config.tui.begin();
		ArrayList<RevetementPlafondSol> revetementPlafondSols = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("idddd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					int trId = Integer.parseInt(splitted[1]);
					SelectableId propObject = objects.get(trId);
					if (propObject instanceof TypeRevetement tr) {
						double p1l = Double.parseDouble(splitted[2]);
						double p1h = Double.parseDouble(splitted[3]);
						double p2l = Double.parseDouble(splitted[4]);
						double p2h = Double.parseDouble(splitted[5]);

						RevetementPlafondSol object = new RevetementPlafondSol(id, tr, p1l, p1h, p2l, p2h);
						revetementPlafondSols.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (propObject == null) {
						errorIdNone("TypeOuvertureNiveau", trId);
					} else {
						errorIdWrongType("TypeOuvertureNiveau", trId);
					}
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<OuvertureNiveaux> ouvertureNiveauxFromString(Objects objects) throws IOException {
		config.tui.diveWhere("OuvertureNiveaux");
		config.tui.begin();
		ArrayList<OuvertureNiveaux> ouvertureNiveaux = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("iii");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					int toId = Integer.parseInt(splitted[1]);
					SelectableId propObject = objects.get(toId);
					if (propObject instanceof TypeOuvertureNiveau to) {
						double p1l = Double.parseDouble(splitted[2]);
						double p1h = Double.parseDouble(splitted[3]);

						OuvertureNiveaux object = new OuvertureNiveaux(id, to, p1l, p1h);
						ouvertureNiveaux.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (propObject == null) {
						errorIdNone("TypeOuvertureNiveau", toId);
					} else {
						errorIdWrongType("TypeOuvertureNiveau", toId);
					}
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<PlafondSol> plafondSolsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("PlafondSols");
		config.tui.begin();
		ArrayList<PlafondSol> plafondSols = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				try {
					int id = Integer.parseInt(splitted[0]);

					if (objects.get(id) != null) {
						errorIdAgain(id);
					}

					// lire les RevetementPlafondSol et les OuvertureNiveaux
					ArrayList<RevetementPlafondSol> r_list = new ArrayList<>();
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
								r_list = revetementPlafondSolsFromString(objects);
							}
							case "OuvertureNiveaux" -> {
								o_list = ouvertureNiveauxFromString(objects);
							}
							default -> error("propriété du plafondSol inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					PlafondSol object = new PlafondSol(id, r_list, o_list);
					plafondSols.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<Piece> piecesFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Pieces");
		config.tui.begin();
		ArrayList<Piece> pieces = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ss");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				// lire les points, murs, le plafond et le sol
				ArrayList<Point> points = new ArrayList<>();
				ArrayList<Mur> murs = new ArrayList<>();

				ArrayList<PlafondSol> plafonds_list;
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
								CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
								debug("points: regex: '" + propMatcher.regex + "'");
								if (propMatcher.matches(text)) {
									String[] pointsIds = text.split(",");
									for (String each: pointsIds) {
										try {
											int pointId = Integer.parseInt(each);
											SelectableId propObject = objects.get(pointId);
											if (propObject instanceof Point point) {
												points.add(point);
											} else if (propObject == null) {
												errorIdNone("Point", pointId);
											} else {
												errorIdWrongType("Point", pointId);
											}
										} catch (NumberFormatException e) {
											errorParse(line, e);
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
								CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
								debug("murs: regex: '" + propMatcher.regex + "'");
								if (propMatcher.matches(text)) {
									String[] mursIds = text.split(",");
									for (String each: mursIds) {
										try {
											int murId = Integer.parseInt(each);
											SelectableId propObject = objects.get(murId);
											if (propObject instanceof Mur mur) {
												murs.add(mur);
											} else if (propObject == null) {
												errorIdNone("Mur", murId);
											} else {
												errorIdWrongType("Mur", murId);
											}
										} catch (NumberFormatException e) {
											errorParse(line, e);
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
							plafonds_list = plafondSolsFromString(objects);

							// TODO: plusieurs plafonds/sols
							if (!plafonds_list.isEmpty()) {
								plafond = plafonds_list.getFirst();
								debug("using first plafond " + plafond.toString());
							} else {
								debug("plafonds is empty !!!");
							}
						}
						case "sol" -> {
							sols_list = plafondSolsFromString(objects);

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
				config.tui.popWhere();

				Piece object = new Piece(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), points, murs, plafond, sol);
				pieces.add(object);
				objects.put(object);
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
	private ArrayList<TypeAppart> typeAppartsFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeApparts");
		config.tui.begin();
		ArrayList<TypeAppart> typeApparts = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ss");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					TypeAppart object = new TypeAppart(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]));
					typeApparts.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<Appart> appartsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Apparts");
		config.tui.begin();
		ArrayList<Appart> apparts = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssi");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du appart
					int taId = Integer.parseInt(splitted[3]);
					SelectableId taObject = objects.get(taId);
					if (taObject instanceof TypeAppart ta) {
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
										CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
										debug("pieces: regex: '" + propMatcher.regex + "'");
										if (propMatcher.matches(text)) {
											String[] piecesIds = text.split(",");
											for (String each: piecesIds) {
												try {
													int pieceId = Integer.parseInt(each);
													SelectableId propObject = objects.get(pieceId);
													if (propObject instanceof Piece piece) {
														pieces.add(piece);
													} else if (propObject == null) {
														errorIdNone("Piece", pieceId);
													} else {
														errorIdWrongType("Piece", pieceId);
													}
												} catch (NumberFormatException e) {
													errorParse(line, e);
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

						Appart object = new Appart(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), pieces, ta);
						apparts.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (taObject == null) {
						errorIdNone("TypeAppart", taId);
					} else {
						errorIdWrongType("TypeAppart", taId);
					}

					config.tui.popWhere();
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<Niveau> niveauxFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Niveaux");
		config.tui.begin();
		ArrayList<Niveau> niveaux = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssd");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du niveau
					double hauteur = Double.parseDouble(splitted[3]);

					// lire les Pieces, Apparts et orpheanMurs
					ArrayList<Piece> pieces = new ArrayList<>();
					ArrayList<Appart> apparts = new ArrayList<>();
					ArrayList<Drawable> orpheans = new ArrayList<>();
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
									CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
									debug("apparts: regex: '" + propMatcher.regex + "'");
									if (propMatcher.matches(text)) {
										String[] appartsIds = text.split(",");
										for (String each: appartsIds) {
											try {
												int appartId = Integer.parseInt(each);
												SelectableId propObject = objects.get(appartId);
												if (propObject instanceof Appart appart) {
													apparts.add(appart);
												} else if (propObject == null) {
													errorIdNone("Appart", appartId);
												} else {
													errorIdWrongType("Appart", appartId);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e);
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
									CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
									debug("pieces: regex: '" + propMatcher.regex + "'");
									if (propMatcher.matches(text)) {
										String[] piecesIds = text.split(",");
										for (String each: piecesIds) {
											try {
												int pieceId = Integer.parseInt(each);
												SelectableId propObject = objects.get(pieceId);
												if (propObject instanceof Piece piece) {
													pieces.add(piece);
												} else if (propObject == null) {
													errorIdNone("Piece", pieceId);
												} else {
													errorIdWrongType("Piece", pieceId);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e);
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading pieces for Appart");
								}
							}
							case "orpheans" -> {
								SmartReader.ReadResult orpheansResult = sreader.readLine();
								if (orpheansResult.getState() == SmartReader.ReadState.LINE) {
									String text = orpheansResult.getText();
									CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
									debug("orpheans: regex: '" + propMatcher.regex + "'");
									if (propMatcher.matches(text)) {
										String[] orpheansIds = text.split(",");
										for (String each: orpheansIds) {
											try {
												int orpheanId = Integer.parseInt(each);
												SelectableId propObject = objects.get(orpheanId);
												if (propObject instanceof Drawable orphean) {
													orpheans.add(orphean);
												} else if (propObject == null) {
													errorIdNone("Drawable", orpheanId);
												} else {
													errorIdWrongType("Drawable", orpheanId);
												}
											} catch (NumberFormatException e) {
												errorParse(line, e);
											}
										}
									} else {
										errorSyntax(text);
									}
								} else {
									error("LINE expected but received " + result.getState() + " when reading orpheanMurs for Appart");
								}
							}
							default -> error("propriété du niveau inconnue: '" + propResult.getText() + "'");
						}
					}
					config.tui.popWhere();

					Niveau object = new Niveau(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), hauteur, pieces, apparts, orpheans);
					niveaux.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<TypeBatiment> typeBatimentsFromString(Objects objects) throws IOException {
		config.tui.diveWhere("TypeBatiments");
		config.tui.begin();
		ArrayList<TypeBatiment> typeBatiments = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ss");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					TypeBatiment object = new TypeBatiment(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]));
					typeBatiments.add(object);
					objects.put(object);
					debug("read " + object);
				} catch (NumberFormatException e) {
					errorParse(line, e);
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
	private ArrayList<Batiment> batimentsFromString (Objects objects) throws IOException {
		config.tui.diveWhere("Batiments");
		config.tui.begin();
		ArrayList<Batiment> batiments = new ArrayList<>();

		CsvRegexMatcher matcher = idCsvRegexMatcher("ssi");
		debug("regex: '" + matcher.regex + "'");
		for (
			SmartReader.ReadResult result = sreader.readLine();
			result.getState() == SmartReader.ReadState.LINE;
			result = sreader.readLine()
		) {
			String line = result.getText();
			if (matcher.matches(line)) {
				String[] splitted = line.split(",");

				int id = Integer.parseInt(splitted[0]);

				if (objects.get(id) != null) {
					errorIdAgain(id);
				}

				try {
					// lire les propriétés directes du batiment
					int tbId = Integer.parseInt(splitted[3]);
					SelectableId tbObject = objects.get(tbId);
					if (tbObject instanceof TypeBatiment tb) {

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
										CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
										debug("apparts: regex: '" + propMatcher.regex + "'");
										if (propMatcher.matches(text)) {
											String[] appartsIds = text.split(",");
											for (String each: appartsIds) {
												try {
													int appartId = Integer.parseInt(each);
													SelectableId propObject = objects.get(appartId);
													if (propObject instanceof Appart appart) {
														apparts.add(appart);
													} else if (propObject == null) {
														errorIdNone("Appart", appartId);
													} else {
														errorIdWrongType("Appart", appartId);
													}
												} catch (NumberFormatException e) {
													errorParse(line, e);
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
										CsvRegexMatcher propMatcher = new CsvRegexMatcher("I");
										debug("niveaux: regex: '" + propMatcher.regex + "'");
										if (propMatcher.matches(text)) {
											String[] niveauxIds = text.split(",");
											for (String each: niveauxIds) {
												try {
													int niveauId = Integer.parseInt(each);
													SelectableId propObject = objects.get(niveauId);
													if (propObject instanceof Niveau niveau) {
														niveaux.add(niveau);
													} else if (propObject == null) {
														errorIdNone("Niveau", niveauId);
													} else {
														errorIdWrongType("Niveau", niveauId);
													}
												} catch (NumberFormatException e) {
													errorParse(line, e);
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

						Batiment object = new Batiment(id, EscapeStrings.unescapeString(splitted[1]), EscapeStrings.unescapeString(splitted[2]), tb, niveaux, apparts);
						batiments.add(object);
						objects.put(object);
						debug("read " + object);
					} else if (tbObject == null) {
						errorIdNone("TypeBatiment", tbId);
					} else {
						errorIdWrongType("TypeBatiment", tbId);
					}

					config.tui.popWhere();
				} catch (NumberFormatException e) {
					errorParse(line, e);
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