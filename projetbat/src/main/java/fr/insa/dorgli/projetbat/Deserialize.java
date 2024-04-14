package fr.insa.dorgli.projetbat;

import java.util.ArrayList;

// crée interactivement une hiérarchie d'objets
// avec un environnement très *très* rustique
// pour l'instant, fait tous les objets inférieurs au Mur
// il y a aussi deux-trois fonctions supplémentaires car flemme oblige
public class Deserialize {
	static boolean log = false; // debug -> cli argument "-V"

	private static ArrayList<String> read_lines(String message) {
		ArrayList<String> arr = new ArrayList<>();
		System.out.println(message);

		String incoming = Lire.S();
		while (!incoming.equals("EOF")) {
			arr.add(incoming);
			incoming = Lire.S();
		}

		return arr;
	}

	private static void errorSyntax(String where, String message) {
		System.err.println(where + ": mauvaise syntaxe: " + message);
		System.exit(-1);
	}
	private static void errorParams(String where, String message) {
		System.err.println(where + ": erreur lors de la compréhension des paramètres: " + message);
		System.exit(-1);
	}

	private static void log(String where, String message) {
		if (log) System.err.println(where + ": " + message);
	}
	private static void successRead(String where, String objet) {
		log(where, "un objet a été lu avec succès: " + objet);
	}

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("-V")) {
			log = true;
			System.err.println("logging");
		}

		ArrayList<String> buf = new ArrayList<>();

		System.out.println("Création des objets: créez un objet par ligne et terminez la saisie avec la ligne EOF");
		System.out.println("L'objet à lire ainsi que la syntaxe et les valeurs attendues de la ligne sont renseignés ci-dessous:");

		/// Point
		buf = read_lines("- Lecture des Points, syntaxe: double_x,double_y,int_niveauId");
		ArrayList<Point> points = new ArrayList<>();
		for (String line: buf) {
			if (line.matches("[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+")) {
				try {
					String[] splitted = line.split(",");

					double x = Double.parseDouble(splitted[0]);
					double y = Double.parseDouble(splitted[1]);
					int niveauId = Integer.parseInt(splitted[2]);

					Point point = new Point(x, y, niveauId);
					successRead("Point", "points[" + points.size() + "]: " + point.toString());
					points.add(point);
				} catch (Throwable e) {
					errorParams("Point", e.getMessage());
				}
			} else {
				errorSyntax("Point", "'" + line + "'");
			}
		}

		/// TypeRevetement
		buf = read_lines("- Lecture des TypeRevetements, syntaxe: String_nom,String_description,double_prixUnitaire");
		ArrayList<TypeRevetement> typeRevetements = new ArrayList<>();
		for (String line: buf) {
			if (line.matches("[a-zA-Z0-9\\-_ ]+,[a-zA-Z0-9\\-_ ]+,[0-9]+(\\.[0-9]+)?")) {
				try {
					String[] splitted = line.split(",");

					double pu = Double.parseDouble(splitted[2]);

					TypeRevetement t = new TypeRevetement(splitted[0], splitted[1], pu);
					successRead("TypeRevetement", "typeRevetements[" + typeRevetements.size() + "]: " + t.toString());
					typeRevetements.add(t);
				} catch (Throwable e) {
					errorParams("TypeRevetement", e.getMessage());
				}
			} else {
				errorSyntax("TypeRevetement", "'" + line + "'");
			}
		}

		/// TypeOuvertureMur
		buf = read_lines("- Lecture des TypeOuvertureMurs, syntaxe: double_prixUnitaire,double_hauteur,double_largeur,String_nom");
		ArrayList<TypeOuvertureMur> typeOuvertureMurs = new ArrayList<>();
		for (String line: buf) {
			if (line.matches("[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[a-zA-Z0-9\\-_ ]+")) {
				try {
					String[] splitted = line.split(",");

					double pu = Double.parseDouble(splitted[0]);
					double h = Double.parseDouble(splitted[1]);
					double l = Double.parseDouble(splitted[2]);

					TypeOuvertureMur t = new TypeOuvertureMur(pu, h, l, splitted[3]);
					successRead("TypeOuvertureMur", "typeOuvertureMurs[" + typeOuvertureMurs.size() + "]: " + t.toString());
					typeOuvertureMurs.add(t);
				} catch (Throwable e) {
					errorParams("TypeOuvertureMur", e.getMessage());
				}
			} else {
				errorSyntax("TypeOuvertureMur", "'" + line + "'");
			}
		}

		/// TypeMur
		buf = read_lines("- Lecture des TypeMurs, syntaxe: double_epaisseur,double_prixUnitaire,String_nom");
		ArrayList<TypeMur> typeMurs = new ArrayList<>();
		for (String line: buf) {
			if (line.matches("[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[a-zA-Z0-9\\-_ ]+")) {
				try {
					String[] splitted = line.split(",");

					double ep = Double.parseDouble(splitted[0]);
					double pu = Double.parseDouble(splitted[1]);

					TypeMur t = new TypeMur(ep, pu, splitted[2]);
					successRead("TypeMur", "typeMurs[" + typeMurs.size() + "]: " + t.toString());
					typeMurs.add(t);
				} catch (Throwable e) {
					errorParams("TypeMur", e.getMessage());
				}
			} else {
				errorSyntax("TypeMur", "'" + line + "'");
			}
		}

		/// Mur
		buf = read_lines("- Lecture des Murs, syntaxe: int_pointDebutId,int_pointFinId,int_hauteur,int_typeMurId ; les revetements et ouverture de chaque seront demandés lors de la saisie");
		ArrayList<Mur> murs = new ArrayList<>();
		for (String line: buf) {
			if (line.matches("[0-9]+,[0-9]+,[0-9]+(\\.[0-9]+)?,[0-9]+")){
				try {
					String[] splitted = line.split(",");

					Point p1 = points.get(Integer.parseInt(splitted[0]));
					Point p2 = points.get(Integer.parseInt(splitted[1]));
					double hauteur = Integer.parseInt(splitted[2]);
					TypeMur typemur = typeMurs.get(Integer.parseInt(splitted[3]));

					ArrayList<String> buf_mur = new ArrayList<>();

					/// RevetementMur
					buf_mur = read_lines("- Lecture des RevetementMurs du côté 1 du Mur actuel, syntaxe: int_typeRevetementId,int_pos1L,int_pos1H,int_pos2L,int_pos2H");
					ArrayList<RevetementMur> rev1 = new ArrayList<>();
					for (String line_rev1: buf_mur) {
						if (line_rev1.matches("[0-9]+,[0-9]+,[0-9]+,[0-9]+,[0-9]+")){
							try {
								String[] splitted_rev1 = line_rev1.split(",");

								TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted_rev1[0]));
								int p1l = Integer.parseInt(splitted_rev1[1]);
								int p1h = Integer.parseInt(splitted_rev1[2]);
								int p2l = Integer.parseInt(splitted_rev1[3]);
								int p2h = Integer.parseInt(splitted_rev1[4]);

								RevetementMur t = new RevetementMur(tr, p1l, p1h, p2l, p2h);
								successRead("Mur -> RevetementMur", "mur -> rev1[" + rev1.size() + "]: " + t.toString());
								rev1.add(t);
							} catch (Throwable e) {
								errorParams("Mur -> RevetementMur", e.getMessage());
							}
						} else {
							errorSyntax("Mur -> RevetementMur", "'" + line_rev1 + "'");
						}
					}

					/// RevetementMur
					buf_mur = read_lines("- Lecture des RevetementMurs du côté 2 du Mur actuel, syntaxe: int_typeRevetementId,int_pos1L,int_pos1H,int_pos2L,int_pos2H");
					ArrayList<RevetementMur> rev2 = new ArrayList<>();
					for (String line_rev2: buf_mur) {
						if (line_rev2.matches("[0-9]+,[0-9]+,[0-9]+,[0-9]+,[0-9]+")){
							try {
								String[] splitted_rev2 = line_rev2.split(",");

								TypeRevetement tr = typeRevetements.get(Integer.parseInt(splitted_rev2[0]));
								int p1l = Integer.parseInt(splitted_rev2[1]);
								int p1h = Integer.parseInt(splitted_rev2[2]);
								int p2l = Integer.parseInt(splitted_rev2[3]);
								int p2h = Integer.parseInt(splitted_rev2[4]);

								RevetementMur t = new RevetementMur(tr, p1l, p1h, p2l, p2h);
								successRead("Mur -> RevetementMur", "mur -> rev2[" + rev2.size() + "]: " + t.toString());
								rev2.add(t);
							} catch (Throwable e) {
								errorParams("Mur -> RevetementMur", e.getMessage());
							}
						} else {
							errorSyntax("Mur -> RevetementMur", "'" + line_rev2 + "'");
						}
					}

					/// OuvertureMur
					buf_mur = read_lines("- Lecture des OuvertureMurs du Mur actuel, syntaxe: int_typeOuvertureMurId,int_posL,int_posH");
					ArrayList<OuvertureMur> ouv = new ArrayList<>();
					for (String line_ouv: buf_mur) {
						if (line_ouv.matches("[0-9]+,[0-9]+,[0-9]+")){
							try {
								String[] splitted_ouv = line_ouv.split(",");

								TypeOuvertureMur tom = typeOuvertureMurs.get(Integer.parseInt(splitted_ouv[0]));
								int l = Integer.parseInt(splitted_ouv[1]);
								int h = Integer.parseInt(splitted_ouv[2]);

								OuvertureMur o = new OuvertureMur(tom, l, h);
								successRead("Mur -> OuvertureMur", "mur -> ouv[" + ouv.size() + "]: " + o.toString());
								ouv.add(o);
							} catch (Throwable e) {
								errorParams("Mur -> OuvertureMur", e.getMessage());
							}
						} else {
							errorSyntax("Mur -> OuvertureMur", "'" + line_ouv + "'");
						}
					}

					Mur t = new Mur(p1, p2, hauteur, typemur, rev1, rev2, ouv);
					successRead("Mur", "murs[" + murs.size() + "]: " + t.toString());
					murs.add(t);
				} catch (Throwable e) {
					errorParams("Mur", e.getMessage());
				}
			} else {
				errorSyntax("Mur", "'" + line + "'");
			}
		}

		System.out.println("La lecture s'est effectuée avec succès");
	}
}
