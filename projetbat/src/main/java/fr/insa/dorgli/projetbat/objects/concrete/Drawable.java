package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;

public abstract class Drawable extends BObject {
	public abstract void draw(DrawingContext ctxt, DrawingContext.ObjectState ostate);
	public void draw(DrawingContext ctxt) {
		draw(ctxt, DrawingContext.ObjectState.NORMAL);
	};

	public Drawable(int id) {
		super(id);
	}
	public Drawable() { }
}