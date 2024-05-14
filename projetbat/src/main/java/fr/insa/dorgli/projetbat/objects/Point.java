package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Point extends Drawable {
	private double x;
	private double y;
	private Niveau niveau;

	public Point(int id, double x, double y) {
		this(id, x, y, null);
	}

	public Point(int id, double x, double y, Niveau niveau) {
		super(id);
		this.x = x;
		this.y = y;
		this.niveau = niveau;
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

	public Niveau getNiveau() {
		return niveau;
	}

	public void setNiveau(Niveau niveau) {
		this.niveau = niveau;
	}

	@Override
	public void draw(DrawingContext dcx, boolean isFocused) {
		// TODO: amnesic debug dive
		dcx.tui().debug("Point: drawing " + (isFocused ? "focused " : "") + "point " + this.toStringShort());
		dcx.drawPoint(this, x, y);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .field("x", String.valueOf(x))
		    .field("y", String.valueOf(y))
		    .field("niveau", niveau == null ? "(null)" : niveau.toString(depth + 1))
        	    .getValue();
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(x),
		    String.valueOf(y),
		    String.valueOf(niveau.getId())
		);
	}
}
