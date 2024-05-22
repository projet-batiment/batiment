package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;

public abstract class DrawableRoot extends BObject {
	public abstract void drawRoot(DrawingContext ctxt);

	public DrawableRoot(int id) {
		super(id);
	}
	public DrawableRoot() { }
}
