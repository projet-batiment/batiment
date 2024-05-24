package fr.insa.dorgli.projetbat.objects;

public interface HasInnerPrice extends HasPrice {
	public abstract void calculerPrix(Devis.DevisCalculator calculator);
}
