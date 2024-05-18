package fr.insa.dorgli.projetbat.objects;

import java.awt.geom.Line2D;

public abstract class DrawableLine extends Drawable {
	private Line2D.Double lineCanvas = null; // the line in CANVAS standards

	public DrawableLine(int id) {
		super(id);
	}

	public Line2D.Double getLineCanvas() {
		return lineCanvas;
	}

	public void setLineCanvas(Line2D.Double lineCanvas) {
		this.lineCanvas = lineCanvas;
	}
}
