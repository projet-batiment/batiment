package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.utils.StructuredToString;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

public class Objects {
	// liste des objets
	public HashMap<Integer, Batiment> batiments = new HashMap<>();
	public HashMap<Integer, Niveau> niveaux = new HashMap<>();
	public HashMap<Integer, Appart> apparts = new HashMap<>();
	public HashMap<Integer, Piece> pieces = new HashMap<>();
	public HashMap<Integer, Mur> murs = new HashMap<>();
	public HashMap<Integer, PlafondSol> plafondsSols = new HashMap<>();
	public HashMap<Integer, Point> points = new HashMap<>();

	public HashMap<Integer, OuvertureMur> ouverturesMur = new HashMap<>();
	public HashMap<Integer, OuvertureNiveaux> ouverturesNiveaux = new HashMap<>();

	public HashMap<Integer, RevetementMur> revetementsMur = new HashMap<>();
	public HashMap<Integer, RevetementPlafondSol> revetementsPlafondSol = new HashMap<>();

	public HashMap<Integer, TypeMur> typesMur = new HashMap<>();
	public HashMap<Integer, TypeOuvertureMur> typesOuverturesMur = new HashMap<>();
	public HashMap<Integer, TypeOuvertureNiveau> typesOuverturesNiveau = new HashMap<>();
	public HashMap<Integer, TypeRevetement> typesRevetement = new HashMap<>();
	public HashMap<Integer, TypeAppart> typesAppart = new HashMap<>();
	public HashMap<Integer, TypeBatiment> typesBatiment = new HashMap<>();

//	public void drawAll(CanvasContainer canvasContainer) {
////		System.out.println("INF: draw/objects: murs (" + murs.size() + "): " + murs.toString());
////		for (Entry<Integer, Mur> p: points.entrySet()) {
////			p.getValue().draw(canvasContainer);
////		}
//
//		System.out.println("INF: draw/objects: points (" + points.size() + "): " + points.toString());
//		for (Entry<Integer, Point> p: points.entrySet()) {
//			p.getValue().draw(canvasContainer);
//		}
//	}

	private int idCounter = 0;

	///// serialize

	public String serialize() {
		String out = new String();

		out += "OBJECTS:Point\n\n";
		for (Point each: points.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Point\n\n";

		out += "OBJECTS:TypeRevetement\n\n";
		for (TypeRevetement each: typesRevetement.values())
			out += each.serialize(this) + "\n";
		out += "EOS:TypeRevetement\n\n";

		out += "OBJECTS:TypeOuvertureMur\n\n";
		for (TypeOuvertureMur each: typesOuverturesMur.values())
			out += each.serialize(this) + "\n";
		out += "EOS:TypeOuvertureMur\n\n";

		out += "OBJECTS:TypeOuvertureNiveau\n\n";
		for (TypeOuvertureNiveau each: typesOuverturesNiveau.values())
			out += each.serialize(this) + "\n";
		out += "EOS:TypeOuvertureNiveau\n\n";

		out += "OBJECTS:TypeMur\n\n";
		for (TypeMur each: typesMur.values())
			out += each.serialize(this) + "\n";
		out += "EOS:TypeMur\n\n";

		out += "OBJECTS:TypeAppart\n\n";
		for (TypeAppart each: typesAppart.values())
			out += each.serialize(this) + "\n";
		out += "EOS:TypeAppart\n\n";

		out += "OBJECTS:Mur\n\n";
		for (Mur each: murs.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Mur\n\n";

		out += "OBJECTS:Piece\n\n";
		for (Piece each: pieces.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Piece\n\n";

		out += "OBJECTS:Appart\n\n";
		for (Appart each: apparts.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Appart\n\n";

		out += "OBJECTS:Niveau\n\n";
		for (Niveau each: niveaux.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Niveau\n\n";

		out += "OBJECTS:Batiment\n\n";
		for (Batiment each: batiments.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Batiment\n\n";

		return out;
	}

	///// idCounter generator + setter

	public int generateId() {
		idCounter++;
		return idCounter;
	}

	public void setIdCounter(int newIdCounter) {
		idCounter = newIdCounter;
	}

	///// create objects (with ID & into matching HashMap)

	public Point createPoint(double x, double y, Niveau niveau) {
		int id = generateId();
		Point point = new Point(id, x, y, niveau);
		points.put(id, point);
		return point;
	}

	public Appart createAppart(String nom, String description, ArrayList<Piece> pieces, TypeAppart typeAppart) {
		int id = generateId();
		Appart appart = new Appart(id, nom, description, pieces, typeAppart);
		apparts.put(id, appart);
		return appart;
	}

	public Mur createMur(Point pointDebut, Point pointFin, double hauteur, TypeMur typeMur,
			ArrayList<RevetementMur> revetements1, ArrayList<RevetementMur> revetements2,
			ArrayList<OuvertureMur> ouvertures) {
		int id = generateId();
		Mur mur = new Mur(id, pointDebut, pointFin, hauteur, typeMur, revetements1, revetements2, ouvertures);
		murs.put(id, mur);
		return mur;
	}

	public Batiment createBatiment(String nom, String description, TypeBatiment typeBatiment, ArrayList<Niveau> niveaux, ArrayList<Appart> apparts) {
		int id = generateId();
		Batiment batiment = new Batiment(id, nom, description, typeBatiment, niveaux, apparts);
		batiments.put(id, batiment);
		return batiment;
	}

	public Niveau createNiveau(String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts, ArrayList<Mur> orpheanMurs) {
		int id = generateId();
		Niveau niveau = new Niveau(id, nom, description, hauteur, pieces, apparts, orpheanMurs);
		niveaux.put(id, niveau);
		return niveau;
	}

	public OuvertureMur createOuvertureMur(TypeOuvertureMur typeOuverture, double posL, double posH) {
		int id = generateId();
		OuvertureMur ouvertureMur = new OuvertureMur(id, typeOuverture, posL, posH);
		ouverturesMur.put(id, ouvertureMur);
		return ouvertureMur;
	}

	public OuvertureNiveaux createOuvertureNiveaux(TypeOuvertureNiveau typeOuvertureNiveau, double posL, double posH) {
		int id = generateId();
		OuvertureNiveaux ouvertureNiveaux = new OuvertureNiveaux(id, typeOuvertureNiveau, posL, posH);
		ouverturesNiveaux.put(id, ouvertureNiveaux);
		return ouvertureNiveaux;
	}

	public Piece createPiece(String nom, String description, ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol) {
		int id = generateId();
		Piece piece = new Piece(id, nom, description, points, murs, plafond, sol);
		pieces.put(id, piece);
		return piece;
	}

	public PlafondSol createPlafondSol(ArrayList<RevetementPlafondSol> revetements, ArrayList<OuvertureNiveaux> ouvertures) {
		int id = generateId();
		PlafondSol plafondSol = new PlafondSol(id, revetements, ouvertures);
		plafondsSols.put(id, plafondSol);
		return plafondSol;
	}

	public RevetementMur createRevetementMur(TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		int id = generateId();
		RevetementMur revetementMur = new RevetementMur(id, typeRevetement, pos1L, pos1H, pos2L, pos2H);
		revetementsMur.put(id, revetementMur);
		return revetementMur;
	}

	public RevetementPlafondSol createRevetementPlafondSol(TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		int id = generateId();
		RevetementPlafondSol revetementPlafondSol = new RevetementPlafondSol(id, typeRevetement, pos1L, pos1H, pos2L, pos2H);
		revetementsPlafondSol.put(id, revetementPlafondSol);
		return revetementPlafondSol;
	}

	public TypeBatiment createTypeBatiment(String name, String description) {
		int id = generateId();
		TypeBatiment typeBatiment = new TypeBatiment(id, name, description);
		typesBatiment.put(id, typeBatiment);
		return typeBatiment;
	}

	public TypeAppart createTypeAppart(String name, String description) {
		int id = generateId();
		TypeAppart typeAppart = new TypeAppart(id, name, description);
		typesAppart.put(id, typeAppart);
		return typeAppart;
	}

	public TypeMur createTypeMur(String nom, String description, double epaisseur, double prixUnitaire) {
		int id = generateId();
		TypeMur typeMur = new TypeMur(id, nom, description, epaisseur, prixUnitaire);
		typesMur.put(id, typeMur);
		return typeMur;
	}

	public TypeOuvertureMur createTypeOuvertureMur(String nom, String description, double hauteur, double largeur, double prixOuverture) {
		int id = generateId();
		TypeOuvertureMur typeOuvertureMur = new TypeOuvertureMur(id, nom, description, hauteur, largeur, prixOuverture);
		typesOuverturesMur.put(id, typeOuvertureMur);
		return typeOuvertureMur;
	}

	public TypeOuvertureNiveau createTypeOuvertureNiveau(String nom, String description, double hauteur, double largeur, double prixOuverture) {
		int id = generateId();
		TypeOuvertureNiveau typeOuvertureNiveau = new TypeOuvertureNiveau(id, nom, description, hauteur, largeur, prixOuverture);
		typesOuverturesNiveau.put(id, typeOuvertureNiveau);
		return typeOuvertureNiveau;
	}

	public TypeRevetement createTypeRevetement(String nom, String description, double prixUnitaire) {
		int id = generateId();
		TypeRevetement typeRevetement = new TypeRevetement(id, nom, description, prixUnitaire);
		typesRevetement.put(id, typeRevetement);
		return typeRevetement;
	}

	///// get object according to ID, return null (HashMap.get) if not found

	public Mur getMurById(int id) {
		return murs.get(id);
	}

	public Piece getPieceById(int id) {
		return pieces.get(id);
	}

	public Appart getAppartById(int id) {
		return apparts.get(id);
	}

	public Batiment getBatimentById(int id) {
		return batiments.get(id);
	}

	public Niveau getNiveauById(int id) {
		return niveaux.get(id);
	}

	public OuvertureMur getOuvertureMurById(int id) {
		return ouverturesMur.get(id);
	}

	public OuvertureNiveaux getOuvertureNiveauById(int id) {
		return ouverturesNiveaux.get(id);
	}

	public RevetementMur getRevetementMurById(int id) {
		return revetementsMur.get(id);
	}

	public PlafondSol getSolPlafondById(int id) {
		return plafondsSols.get(id);
	}

	public TypeMur getTypeMurById(int id) {
		return typesMur.get(id);
	}

	public Point getPointById(int id) {
		return points.get(id);
	}

	public TypeOuvertureNiveau getTypeOuvertureNiveauById(int id) {
		return typesOuverturesNiveau.get(id);
	}

	public TypeOuvertureMur getTypeOuvertureMurById(int id) {
		return typesOuverturesMur.get(id);
	}

	public TypeRevetement getTypeRevetementById(int id) {
		return typesRevetement.get(id);
	}

	public RevetementPlafondSol getRevetementPlafondSolById(int id) {
		return revetementsPlafondSol.get(id);
	}

	///// toString custom implementation

	private String hashSetIntoString(String name, Collection<BObject> set) {
		String out = TUI.blue(name) + " (" + set.size() + ") {\n";
		for (BObject each: set) {
			out += each.toString(2) + ",\n";
		}
		return out;
	}

	@Override
	public String toString() {
		return new StructuredToString.OfBObject(0, "Objects")
		    .field("typesBatiment", (Collection<BObject>)((Collection<?>)typesBatiment.values()) )
		    .field("typesAppart", (Collection<BObject>)((Collection<?>)typesAppart.values()) )
		    .field("typesMur", (Collection<BObject>)((Collection<?>)typesMur.values()) )
		    .field("typesOuverturesMur", (Collection<BObject>)((Collection<?>)typesOuverturesMur.values()) )
		    .field("typesOuverturesNiveau", (Collection<BObject>)((Collection<?>)typesOuverturesNiveau.values()) )
		    .field("typesRevetement", (Collection<BObject>)((Collection<?>)typesRevetement.values()) )

		    .field("OuverturesMur", (Collection<BObject>)((Collection<?>)ouverturesMur.values()) )
		    .field("OuverturesNiveaux", (Collection<BObject>)((Collection<?>)ouverturesNiveaux.values()) )
		    .field("RevetementMur", (Collection<BObject>)((Collection<?>)revetementsMur.values()) )
		    .field("RevetementPlafondSol", (Collection<BObject>)((Collection<?>)revetementsPlafondSol.values()) )

		    .field("batiments", (Collection<BObject>)((Collection<?>)batiments.values()) )
		    .field("niveaux", (Collection<BObject>)((Collection<?>)niveaux.values()) )
		    .field("apparts", (Collection<BObject>)((Collection<?>)apparts.values()) )
		    .field("pieces", (Collection<BObject>)((Collection<?>)pieces.values()) )
		    .field("plafondSols", (Collection<BObject>)((Collection<?>)murs.values()) )
		    .field("murs", (Collection<BObject>)((Collection<?>)murs.values()) )
		    .field("points", (Collection<BObject>)((Collection<?>)points.values()) )

		    .getValue()
		;
	}
}
