package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.gui.DrawingContext;
import fr.insa.dorgli.projetbat.StructuredToString;

public class Point extends Drawable {
	private double x;
	private double y;
	private int niveauId;

	public Point(int id, double x, double y, int niveauId) {
		super(id);
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

	public void draw(DrawingContext dcx, boolean isFocused) {
		// TODO: amnesic debug dive
		dcx.tui().debug("Point: drawing " + (isFocused ? "focused " : "") + "point " + this.toStringShort());
		dcx.drawPoint(this, x, y);
	}

	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("x", ""+x)
		    .field("y", ""+y)
		    .field("niveauId", ""+niveauId)
            .getValue();
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfPoint(this);
		return String.join(",",
		    String.valueOf(id),
		    String.valueOf(x),
		    String.valueOf(y),
		    String.valueOf(niveauId)
		);
	}
}
