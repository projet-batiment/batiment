package fr.insa.dorgli.projetbat.objects.concrete;

import java.awt.geom.Path2D;

public abstract class DrawablePath extends Drawable {
	private Path2D.Double pathCanvas = null; // the path in CANVAS standards
	
	public DrawablePath(int id) {
		super(id);
	}
	public DrawablePath() { }

	public Path2D.Double getPathCanvas() {
		return pathCanvas;
	}

	public void setPathCanvas(Path2D.Double pathCanvas) {
		this.pathCanvas = pathCanvas;
	}
}
