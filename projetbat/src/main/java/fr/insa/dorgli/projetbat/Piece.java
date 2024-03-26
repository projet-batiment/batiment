package main.java.fr.insa.dorgli.projetbat;

import java.util.ArrayList;

public class Piece implements ToString, ToStringShort {
    private String nom;
    private ArrayList<Mur> murs;
    private ArrayList<Point> points;
    private PlafondSol plafond;
    private PlafondSol sol;

    public Piece(ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol, String nom) throws IllegalArgumentException {
        this.points = points;
        this.murs = murs;
        this.plafond = plafond;
        this.sol = sol;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void setMurs(ArrayList<Mur> murs) {
        this.murs = murs;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public PlafondSol getPlafond() {
        return plafond;
    }

    public void setPlafond(PlafondSol plafond) {
        this.plafond = plafond;
    }

    public PlafondSol getSol() {
        return sol;
    }

    public void setSol(PlafondSol sol) {
        this.sol = sol;
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

        String pointsOut = "[ ";
        for (Point each : points) {
            pointsOut += each.toStringShort() + ", ";
        }
        pointsOut += "]";

        String mursOut = "[ ";
        for (Mur each : murs) {
            mursOut += each.toStringShort() + ", ";
        }
        mursOut += "]";

        return "Piece {\n"
                + pfx + "points: " + pointsOut + ",\n"
                + pfx + "murs: " + mursOut + ",\n"
                + pfx + "plafond: " + plafond.toString(depth + 1) + ",\n"
                + pfx + "sol: " + sol.toString(depth + 1) + ",\n"
                + pfx + "nom: " + nom + ",\n"
                + "}";
    }

    public String toStringShort() {
        // TODO -> toStringShort -> afficher l'ID
        return "( #" + nom + " )";
    }
}
