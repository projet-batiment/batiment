package fr.insa.dorgli.projetbat;

import java.util.HashMap;

// Config contient une description exhaustive et détaillée de l'état actuel du programme
// Cette classe contient donc tous les paramètres de l'application (verbeux, etc)
// Ainsi qu'un annuaire de tous les objets créés lors de l'exécution (dans des hashmap)
public class Config {
	TUI tui = new TUI();

	// liste des objets
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

	public static void main(String[] args) {
		Deserialize.main(args); // pour l'instant
	}
	
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