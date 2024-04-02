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
    public static void main(String[] args) {
        Deserialize.main(args); // pour l'instant

        Batiment batiment = new Batiment();
    }
}
