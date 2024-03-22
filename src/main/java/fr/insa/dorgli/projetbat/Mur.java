package main.java.fr.insa.dorgli.projetbat;

import java.util.ArrayList;

public class Mur {
    private Point pointDebut;
    private Point pointFin;
    private int hauteur;
    private TypeMur typeMur;
    private ArrayList<RevetementMur> revetements1;
    private ArrayList<RevetementMur> revetements2;
    private ArrayList<OuvertureMur> ouvertures;

    public Mur(Point pointDebut, Point pointFin, int hauteur) {
        this.pointDebut = pointDebut;
        this.pointFin = pointFin;
        this.hauteur = hauteur;
        this.revetements1 = new ArrayList<RevetementMur>();
        this.revetements2 = new ArrayList<RevetementMur>();
    }

    public Point getPointDebut() {
        return this.pointDebut;
    }

    public void setPointDebut(Point pointDebut) {
        this.pointDebut = pointDebut;
    }

    public Point getPointFin() {
        return this.pointFin;
    }

    public void setPointFin(Point pointFin) {
        this.pointFin = pointFin;
    }

    public int getHauteur() {
        return this.hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public typeMur getTypeMur() {
        return this.typeMur;
    }

    public void setTypeMur(typeMur typeMur) {
        this.typeMur = typeMur;
    }

    public ArrayList<RevetementMur> getRevetements1() {
        return this.revetements1;
    }

    public ArrayList<RevetementMur> getRevetements2() {
        return this.revetements2;
    }

    public ArrayList<OuvertureMur> getOuvertures() {
        return this.ouvertures;
    }
}
