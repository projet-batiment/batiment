package fr.insa.dorgli.projetbat.objects;

public abstract class HasPrice extends Drawable {
	public abstract double calculerPrix();

	public HasPrice(int id) {
		super(id);
	}
}
