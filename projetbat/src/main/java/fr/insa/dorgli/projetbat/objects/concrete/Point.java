package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.awt.geom.Point2D;
import javafx.scene.paint.Color;

public class Point extends DrawablePoint {
	private final Point2D.Double point;
	private Niveau niveau;

	public Point() {
		this.point = new Point2D.Double();
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
	public void draw(DrawingContext dcx, DrawingContext.ObjectState ostate) {
		switch (ostate) {
			case NORMAL -> dcx.drawPoint(this, 7.5, Color.LIGHTSKYBLUE, false);
			case SELECTED -> dcx.drawPoint(this, 10, Color.web("8AA5E4"), true);
			case MEMBER -> dcx.drawPoint(this, 7.5, Color.web("ccb18a"), true);
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
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .field("x", String.valueOf(point.getX()))
		    .field("y", String.valueOf(point.getY()))
		    .field("niveau", niveau) 
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId(),
		    point.getX(),
		    point.getY(),
		    niveau.getId()
		);
	}

	@Override
	public void clearChildren() { }

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Point.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Point.removeChildren()");
	}
}
