package fr.insa.dorgli.projetbat.objects.concrete;

import java.awt.geom.Point2D;

public abstract class DrawablePoint extends Drawable {
	private Point2D.Double pointCanvas = null; // the point in CANVAS standards

	public DrawablePoint(int id) {
		super(id);
	}
	public DrawablePoint() { }

	public Point2D.Double getPointCanvas() {
		return pointCanvas;
	}

	public void setPointCanvas(Point2D.Double pointCanvas) {
		this.pointCanvas = pointCanvas;
	}
}
