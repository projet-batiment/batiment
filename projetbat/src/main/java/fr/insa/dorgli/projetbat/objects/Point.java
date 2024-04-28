package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToStringShort;
import fr.insa.dorgli.projetbat.gui.CanvasContainer;
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

	public void draw(CanvasContainer canvasContainer) {
		// NOTE: (( !!! FAUX !!! )) on passe ici de double(mètres) en int(CENTIMÈTRES) car java.awt ne marche qu'avec des int 🤦‍️
		canvasContainer.drawPoint(x, y);
	}

	public String toString() {
		return "Point { x: " + x + ", y: " + y + ", niveauId: " + niveauId + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID au lieu du nom
		return "( #" + "TODO" + " )";
	}
}