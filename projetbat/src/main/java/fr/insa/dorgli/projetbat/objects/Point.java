package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.awt.geom.Point2D;
import javafx.scene.paint.Color;

public class Point extends DrawablePoint {
	private final Point2D.Double point;
	private Niveau niveau;

	public Point(double x, double y) {
		this.point = new Point2D.Double(x, y);
	}

	public Point(int id, double x, double y, Niveau niveau) {
		super(id);
		this.point = new Point2D.Double(x, y);
		this.niveau = niveau;
	}

	public Point2D.Double getPoint() {
		return point;
	}

	public double getX() {
		return point.getX();
	}

	public void setX(double x) {
		point.setLocation(x, point.getY());
	}

	public double getY() {
		return point.getY();
	}

	public void setY(double y) {
		point.setLocation(point.getX(), y);
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
		if (isFocused) {
			dcx.drawPoint(this, 10, Color.NAVAJOWHITE, true);
		} else {
			dcx.drawPoint(this, 7.5, Color.LIGHTSKYBLUE, false);
		}
	}

	@Override
	public boolean ready() {
		return (
			niveau != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .field("x", String.valueOf(point.getX()))
		    .field("y", String.valueOf(point.getY()))
		    .field("niveau", niveau == null ? "(null)" : niveau.toString(depth + 1))
        	    .getValue();
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(point.getX()),
		    String.valueOf(point.getY()),
		    String.valueOf(niveau.getId())
		);
	}
}
