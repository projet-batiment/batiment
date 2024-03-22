package fr.insa.dorgli.projetbat;

public class Point {
    private int id;
    private double x;
    private double y;
    private int niveauId;

    public Point(int id, double x, double y, int niveauId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.niveauId = niveauId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getNiveauId() {
        return niveauId;
    }

    public void setNiveauId(int niveauId) {
        this.niveauId = niveauId;
    }
}
