package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.TUI;
import fr.insa.dorgli.projetbat.gui.CanvasContainer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Objects {
	// liste des objets
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

	private String entrySetCustomString(Map.Entry entry) {
		return TUI.green("" + entry.getKey()) + ": " + entry.getValue();
	}

	@Override
	public String toString() {
		String out = "Objects {\n";

		// éléments principaux
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

	public Mur getMurById(int id) {
		return murs.get(id);
	}

	public Piece getPieceById(int id) {
		return pieces.get(id);
	}

	public Appart getAppartById(int id) {
		return apparts.get(id);
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

//	public static void main(String[] args) {
//		Deserialize.main(args); // pour l'instant
//	}
	
	public int getIdOfMur(Mur murRecherche){
		for (int i: murs.keySet()) {
			if (murs.get(i) == murRecherche) {
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
}
