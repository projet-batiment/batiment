package main.java.fr.insa.dorgli.projetbat;

import java.util.ArrayList;

public class Niveau implements ToString, ToStringShort {
    private double hauteur;
    private ArrayList<Piece> pieces;

    public Niveau(double hauteur, ArrayList<Piece> pieces) {
        this.hauteur = hauteur;
        this.pieces = pieces;
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int depth) {
        String pfx = "";
        for (int i = 0; i <= depth; i++) {
            pfx += "  ";
        }
        int nextDepth = depth + 1;

        String piecesOut = "[ ";
        for (Piece each : pieces) {
            piecesOut += each.toStringShort() + ", ";
        }
        piecesOut += "]";

        return "Niveau {\n"
                + pfx + "hauteur: " + hauteur + ",\n"
                + pfx + "pieces: " + piecesOut + ",\n"
                + "}";
    }

    public String toStringShort() {
        // TODO -> toStringShort -> afficher l'ID
        return "( #" + "TODO" + " )";
    }
}
