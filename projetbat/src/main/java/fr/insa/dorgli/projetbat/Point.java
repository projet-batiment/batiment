package fr.insa.dorgli.projetbat;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point implements ToStringShort {
	private double x;
	private double y;
	private int niveauId;

	public Point(double x, double y, int niveauId) {
		this.x = x;
		this.y = y;
		this.niveauId = niveauId;
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

	public void draw(GraphicsContext ctxt) {
		ctxt.setFill(Color.RED);
		int radius = 5;
		ctxt.fillOval(x*30 - radius, y*30 - radius, radius, radius);
		System.out.println("INF: draw/Point: drew " + (x - radius) + "," + (y - radius) + "," + radius);
	}

	public String toString() {
		return "Point { x: " + x + ", y: " + y + ", niveauId: " + niveauId + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID au lieu du nom
		return "( #" + "TODO" + " )";
	}
}