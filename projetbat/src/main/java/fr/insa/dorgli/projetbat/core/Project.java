package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.Drawable;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Project extends FancyToStrings implements HasPrice {
	public Objects objects = new Objects();

	public String projectName = new String();
	public String projectDescription = new String();
	public String savefilePath = new String();

	public Drawable firstViewRootElement;

	@Override
	public String toStringShort() {
		return "Project";
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("projectName", projectName)
		    .textField("projectDescription", projectDescription)
        	    .getValue();
	}

	@Override
	public String toString() {
		return toString(0);
	}
	@Override
	public String toString(int depth) {
		return toString(depth, false);
	}
	public String toString(boolean indentFirst) {
		return toString(0, indentFirst);
	}

	@Override
	public double calculerPrix() {
		var prixWrapper = new Object(){ double prix = 0; };

		objects.getAll().values().stream()
		    .filter((BObject bobject) -> bobject instanceof Batiment)
		    .forEach((BObject batiment) -> prixWrapper.prix += ((Batiment)batiment).calculerPrix())
		;

		return prixWrapper.prix;
	}
}
