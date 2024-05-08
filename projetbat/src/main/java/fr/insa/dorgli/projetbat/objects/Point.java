package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToStringShort;
import fr.insa.dorgli.projetbat.gui.CanvasContainer;
import fr.insa.dorgli.projetbat.gui.Drawable;
import fr.insa.dorgli.projetbat.gui.DrawingContext;

public class Point implements ToStringShort, Drawable {
	private double x;
	private double y;
	private int niveauId;

	private final int id;

	public Point(int id, double x, double y, int niveauId) {
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setNiveauId(int niveauId) {
		this.niveauId = niveauId;
	}

	public void draw(DrawingContext dcx, boolean isFocused) {
		// TODO: amnesic debug dive
		dcx.tui().debug("Point: drawing " + (isFocused ? "focused " : "") + "point " + this.toStringShort());
		dcx.drawPoint(this, x, y);
	}

	public String toString() {
		return "Point { x: " + x + ", y: " + y + ", niveauId: " + niveauId + " }";
	}

	public String toStringShort() {
		return "#" + id + "";
	}
}