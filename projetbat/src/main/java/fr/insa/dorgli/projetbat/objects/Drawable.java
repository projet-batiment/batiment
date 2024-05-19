package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;

public abstract class Drawable extends BObject {
	public abstract void draw(DrawingContext ctxt, boolean isFocused);
	public void draw(DrawingContext ctxt) {
		draw(ctxt, false);
	};

	public Drawable(int id) {
		super(id);
	}
	public Drawable() { }
}