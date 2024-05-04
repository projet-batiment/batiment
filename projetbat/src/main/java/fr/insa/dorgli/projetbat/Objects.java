package fr.insa.dorgli.projetbat;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Objects {
	// liste des objets
	HashMap<Integer, Batiment> batiments = new HashMap<>();
	HashMap<Integer, Niveau> niveaux = new HashMap<>();
	HashMap<Integer, Appart> apparts = new HashMap<>();
	HashMap<Integer, Piece> pieces = new HashMap<>();
	HashMap<Integer, Mur> murs = new HashMap<>();
	HashMap<Integer, PlafondSol> plafondsSols = new HashMap<>();
	HashMap<Integer, Point> points = new HashMap<>();

	HashMap<Integer, OuvertureMur> ouverturesMur = new HashMap<>();
	HashMap<Integer, OuvertureNiveaux> ouverturesNiveaux = new HashMap<>();

	HashMap<Integer, RevetementMur> revetementsMur = new HashMap<>();
	HashMap<Integer, RevetementPlafondSol> revetementsPlafondSol = new HashMap<>();

	HashMap<Integer, TypeMur> typesMur = new HashMap<>();
	HashMap<Integer, TypeOuvertureMur> typesOuverturesMur = new HashMap<>();
	HashMap<Integer, TypeOuvertureNiveau> typesOuverturesNiveau = new HashMap<>();
	HashMap<Integer, TypeRevetement> typesRevetement = new HashMap<>();
	HashMap<Integer, TypeAppart> typesAppart = new HashMap<>();

	private int idCounter = 0;

	///// idCounter generator + setter

	public int generateId() {
		idCounter++;
		return idCounter;
	}

	public void setIdCounter(int newIdCounter) {
		idCounter = newIdCounter;
	}

	///// create objects (with ID & into matching HashMap)

	public Point createPoint(double x, double y, int niveauId) {
		Point point = new Point(x, y, niveauId);
		int id = generateId();
		points.put(id, point);
		return point;
	}

	public Appart createAppart(String nom, String description, ArrayList<Piece> pieces, TypeAppart typeAppart) {
		Appart appart = new Appart(nom, description, pieces, typeAppart);
		int id = generateId();
		apparts.put(id, appart);
		return appart;
	}

	public Mur createMur(Point pointDebut, Point pointFin, double hauteur, TypeMur typeMur,
			ArrayList<RevetementMur> revetements1, ArrayList<RevetementMur> revetements2,
			ArrayList<OuvertureMur> ouvertures) {
		Mur mur = new Mur(pointDebut, pointFin, hauteur, typeMur, revetements1, revetements2, ouvertures);
		int id = generateId();
		murs.put(id, mur);
		return mur;
	}

	public Batiment createBatiment(String nom, String description, TypeBatiment typeBatiment, ArrayList<Niveau> niveaux, ArrayList<Appart> apparts) {
		Batiment batiment = new Batiment(nom, description, typeBatiment, niveaux, apparts);
		int id = generateId();
		batiments.put(id, batiment);
		return batiment;
	}

	public Niveau createNiveau(String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts) {
		Niveau niveau = new Niveau(nom, description, hauteur, pieces, apparts);
		int id = generateId();
		niveaux.put(id, niveau);
		return niveau;
	}

	public OuvertureMur createOuvertureMur(TypeOuvertureMur typeOuverture, double posL, double posH) {
		OuvertureMur ouvertureMur = new OuvertureMur(typeOuverture, posL, posH);
		int id = generateId();
		ouverturesMur.put(id, ouvertureMur);
		return ouvertureMur;
	}

	public OuvertureNiveaux createOuvertureNiveaux(TypeOuvertureNiveau typeOuvertureNiveau, double posL, double posH) {
		OuvertureNiveaux ouvertureNiveaux = new OuvertureNiveaux(typeOuvertureNiveau, posL, posH);
		int id = generateId();
		ouverturesNiveaux.put(id, ouvertureNiveaux);
		return ouvertureNiveaux;
	}

	public Piece createPiece(String nom, String description, ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol) {
		Piece piece = new Piece(nom, description, points, murs, plafond, sol);
		int id = generateId();
		pieces.put(id, piece);
		return piece;
	}

	public PlafondSol createPlafondSol(ArrayList<RevetementPlafondSol> revetements, ArrayList<OuvertureNiveaux> ouvertures) {
		PlafondSol plafondSol = new PlafondSol(revetements, ouvertures);
		int id = generateId();
		plafondsSols.put(id, plafondSol);
		return plafondSol;
	}

	public RevetementMur createRevetementMur(TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		RevetementMur revetementMur = new RevetementMur(typeRevetement, pos1L, pos1H, pos2L, pos2H);
		int id = generateId();
		revetementsMur.put(id, revetementMur);
		return revetementMur;
	}

	public RevetementPlafondSol createRevetementPlafondSol(TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		RevetementPlafondSol revetementPlafondSol = new RevetementPlafondSol(typeRevetement, pos1L, pos1H, pos2L, pos2H);
		int id = generateId();
		revetementsPlafondSol.put(id, revetementPlafondSol);
		return revetementPlafondSol;
	}

	public TypeAppart createTypeAppart(String name, String description) {
		TypeAppart typeAppart = new TypeAppart(name, description);
		int id = generateId();
		typesAppart.put(id, typeAppart);
		return typeAppart;
	}

	public TypeMur createTypeMur(String nom, String description, double epaisseur, double prixUnitaire) {
		TypeMur typeMur = new TypeMur(nom, description, epaisseur, prixUnitaire);
		int id = generateId();
		typesMur.put(id, typeMur);
		return typeMur;
	}

	public TypeOuvertureMur createTypeOuvertureMur(String nom, String description, double hauteur, double largeur, double prixOuverture) {
		TypeOuvertureMur typeOuvertureMur = new TypeOuvertureMur(nom, description, hauteur, largeur, prixOuverture);
		int id = generateId();
		typesOuverturesMur.put(id, typeOuvertureMur);
		return typeOuvertureMur;
	}

	public TypeOuvertureNiveau createTypeOuvertureNiveau(String nom, String description, double hauteur, double largeur, double prixOuverture) {
		TypeOuvertureNiveau typeOuvertureNiveau = new TypeOuvertureNiveau(nom, description, hauteur, largeur, prixOuverture);
		int id = generateId();
		typesOuverturesNiveau.put(id, typeOuvertureNiveau);
		return typeOuvertureNiveau;
	}

	public TypeRevetement createTypeRevetement(String nom, String description, double prixUnitaire) {
		TypeRevetement typeRevetement = new TypeRevetement(nom, description, prixUnitaire);
		int id = generateId();
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

	///// get ID of object, return -1 if not found

	public int getIdOfMur(Mur murRecherche){
		for (int i: murs.keySet()) {
			if (murs.get(i) == murRecherche) {
				return i;
			}
		}
		return -1;
	}

	public int getIdOfBatiment(Batiment batimentRecherche){
		for (int i: batiments.keySet()){
			if (batiments.get(i) == batimentRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfNiveau(Niveau niveauRecherche){
		for (int i: niveaux.keySet()){
			if (niveaux.get(i) == niveauRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfAppart(Appart appartRecherche){
		for (int i: apparts.keySet()){
			if (apparts.get(i) == appartRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfPiece(Piece pieceRecherche){
		for (int i: pieces.keySet()){
			if (pieces.get(i) == pieceRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfPlafondSol(PlafondSol plafondSolRecherche){
		for (int i: plafondsSols.keySet()){
			if (plafondsSols.get(i) == plafondSolRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfPoint(Point pointRecherche){
		for (int i: points.keySet()){
			if (points.get(i) == pointRecherche){
				return i;
			}
		}
		return -1;
	}


	public int getIdOfOuvertureMur(OuvertureMur ouvertureMurRecherche){
		for (int i: ouverturesMur.keySet()){
			if (ouverturesMur.get(i) == ouvertureMurRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfOuvertureNiveaux(OuvertureNiveaux ouvertureNiveauxRecherche){
		for (int i: ouverturesNiveaux.keySet()){
			if (ouverturesNiveaux.get(i) == ouvertureNiveauxRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfTypeRevetement(TypeRevetement typeRevetementRecherche){
	for ( int i : typesRevetement.keySet() ){
		if (typesRevetement.get(i) == typeRevetementRecherche){
			return i;
		}
	}
	return -1;
	}

	public int getIdOfTypeOuvertureMur(TypeOuvertureMur typeouvertureMurRecherche){
		for ( int i : typesOuverturesMur.keySet() ){
			if (typesOuverturesMur.get(i) == typeouvertureMurRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfTypeOuvertureNiveau(TypeOuvertureNiveau typeouvertureNiveauRecherche){
		for ( int i : typesOuverturesNiveau.keySet() ){
			if (typesOuverturesNiveau.get(i) == typeouvertureNiveauRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfTypeMur(TypeMur typemurRecherche){
		for ( int i : typesMur.keySet() ){
			if (typesMur.get(i) == typemurRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfRevetementPlafondSol(RevetementPlafondSol revetementPlafondSolRecherche){
		for ( int i : revetementsPlafondSol.keySet() ){
			if (revetementsPlafondSol.get(i) == revetementPlafondSolRecherche){
				return i;
			}
		}
		return -1;
	}

	public int getIdOfRevetementMur (RevetementMur revetementMurRecherche){
		for ( int i : revetementsMur.keySet() ){
			if (revetementsMur.get(i) == revetementMurRecherche){
				return i;
			}
		}
		return -1;
	}

	///// toString custom implementation

	private String entrySetCustomString(Map.Entry entry) {
		return TUI.green("" + entry.getKey()) + ": " + entry.getValue();
	}

	@Override
	public String toString() {
		String out = "Objects {\n";

		// éléments principaux
		out += "    " + TUI.blue("batiments") + " " + batiments.size() + " {\n";
		for (Map.Entry each: batiments.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("niveaux") + " " + niveaux.size() + " {\n";
		for (Map.Entry each: niveaux.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("apparts") + " " + apparts.size() + " {\n";
		for (Map.Entry each: apparts.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("pieces") + " " + pieces.size() + " {\n";
		for (Map.Entry each: pieces.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("murs") + " " + murs.size() + " {\n";
		for (Map.Entry each: murs.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("plafondsSols") + " " + plafondsSols.size() + " {\n";
		for (Map.Entry each: plafondsSols.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("points") + " " + points.size() + " {\n";
		for (Map.Entry each: points.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";
		out += "\n";

		// ouvertures
		out += "    " + TUI.blue("ouverturesMur") + " " + ouverturesMur.size() + " {\n";
		for (Map.Entry each: ouverturesMur.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("ouverturesNiveaux") + " " + ouverturesNiveaux.size() + " {\n";
		for (Map.Entry each: ouverturesNiveaux.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";
		out += "\n";

		// revetements
		out += "    " + TUI.blue("revetementsMur") + " " + revetementsMur.size() + " {\n";
		for (Map.Entry each: revetementsMur.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("revetementsPlafondSol") + " " + revetementsPlafondSol.size() + " {\n";
		for (Map.Entry each: revetementsPlafondSol.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";
		out += "\n";

		// types
		out += "    " + TUI.blue("typesMur") + " " + typesMur.size() + " {\n";
		for (Map.Entry each: typesMur.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("typesOuverturesMur") + " " + typesOuverturesMur.size() + " {\n";
		for (Map.Entry each: typesOuverturesMur.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("typesOuverturesNiveau") + " " + typesOuverturesNiveau.size() + " {\n";
		for (Map.Entry each: typesOuverturesNiveau.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";
		out += "\n";

		out += "    " + TUI.blue("typesRevetement") + " " + typesRevetement.size() + " {\n";
		for (Map.Entry each: typesRevetement.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		out += "    " + TUI.blue("typesAppart") + " " + typesAppart.size() + " {\n";
		for (Map.Entry each: typesAppart.entrySet()) {
			out += "        " + entrySetCustomString(each) + ",\n";
		}
		out += "    },\n";

		return out + "}";
	}
}
