package fr.insa.dorgli.projetbat;

import java.util.HashMap;

public class Projetbat {
    HashMap<Integer, Niveau> niveaux = new HashMap<Integer, Niveau>();
    HashMap<Integer, Appart> apparts = new HashMap<Integer, Appart>();
    HashMap<Integer, Piece> pieces = new HashMap<Integer, Piece>();
    HashMap<Integer, PlafondSol> plafondsSols = new HashMap<Integer, PlafondSol>();
    HashMap<Integer, Point> points = new HashMap<Integer, Point>();

    HashMap<Integer, OuvertureMur> ouverturesMur = new HashMap<Integer, OuvertureMur>();
    HashMap<Integer, OuvertureNiveaux> ouverturesNiveaux = new HashMap<Integer, OuvertureNiveaux>();

    HashMap<Integer, RevetementMur> revetementsMur = new HashMap<Integer, RevetementMur>();
    HashMap<Integer, RevetementPlafondSol> revetementsPlafondSol = new HashMap<Integer, RevetementPlafondSol>();

    HashMap<Integer, TypeMur> typesMur = new HashMap<Integer, TypeMur>();
    HashMap<Integer, TypeOuvertureMur> typesOuverturesMur = new HashMap<Integer, TypesOuverturesMur>();
    HashMap<Integer, TypeOuvertureNiveau> typesOuverturesNiveau = new HashMap<Integer, TypesOuverturesNiveau>();
    HashMap<Integer, TypeRevetement> typesRevetement = new HashMap<Integer, TypesRevetement>();

    HashMap<Integer, Mur> murs= new HashMap<>();

public Mur getMurById(int id){
	return murs.get(id);
}

public Piece getPieceById(int id){
	return pieces.get(id);
}

public Appart getAppartById(int id){
	return apparts.get(id);
}

public Niveau getNiveauById(int id){
	return niveaux.get(id);
}

public OuvertureMur getOuvertureMurById(int id){
	return ouverturesMur.get(id);
}

public OuvertureNiveau getOuvertureNiveauById(int id){
	return ouverturesNiveaux.get(id);
}

public RevetementMur getRevetementMurById(int id){
	return revetementsMur.get(id);
}

public PlafondSol getSolPlafondById(int id){
	return plafondsSols.get(id);
}

public TypeMur getTypeMurById(int id){
	return typesMur.get(id);
}

public Point getPointById(int id){
	return points.get(id);
}

public TypeOuvertureNiveau getTypeOuvertureNiveauById(int id){
	return typesOuverturesNiveau.get(id);
}

public TypeOuvertureMur getTypeOuvertureMurById(int id){
	return typesOuverturesMur.get(id);
}

public TypeRevetement getTypeRevetementById(int id){
	return typesRevetement.get(id);
}

public RevetementPlafondSol getRevetementPlafondSolById(int id){
	return revetementsPlafondSol.get(id);
}

    
    public static void main(String[] args) {
        Deserialize.main(args); // pour l'instant

        Batiment batiment = new Batiment();
    }
}
