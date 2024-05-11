package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.TUI;
import fr.insa.dorgli.projetbat.gui.CanvasContainer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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

	public void drawAll(CanvasContainer canvasContainer) {
//		System.out.println("INF: draw/objects: murs (" + murs.size() + "): " + murs.toString());
//		for (Entry<Integer, Mur> p: points.entrySet()) {
//			p.getValue().draw(canvasContainer);
//		}

		System.out.println("INF: draw/objects: points (" + points.size() + "): " + points.toString());
		for (Entry<Integer, Point> p: points.entrySet()) {
			p.getValue().draw(canvasContainer);
		}
	}

	private int idCounter = 0;

	///// serialize

	public String serialize() {
		String out = new String();

		out += "OBJECTS:Point\n\n";
		for (Point each: points.values())
			out += each.serialize(this) + "\n";
		out += "EOS:Point\n\n";

		out += "OBJECTS:TypeRevetemnt\n\n";
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

	public int getIdOfTypeAppart(TypeAppart typeAppartRecherche){
		for (int i: typesAppart.keySet()){
			if (typesAppart.get(i) == typeAppartRecherche){
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
