package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Project extends SelectableId implements HasInnerPrice, NameDesc {
	public Objects objects = new Objects();

	public String projectName = new String();
	public String projectDescription = new String();
	public String savefilePath = new String();

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

	@Override
	public double calculerPrix() {
		double prix = 0;

		for (Batiment batiment: objects.getBatiments())
			prix += batiment.calculerPrix();

		return prix;
	}

	@Override
	public void calculerPrix(Devis.DevisCalculator calculator) {
		for (Batiment batiment: objects.getBatiments())
			calculator.addObject(batiment);
	}

	@Override
	public String getNom() {
		return projectName;
	}

	@Override
	public String getDescription() {
		return projectDescription;
	}

	@Override
	public void setNom(String nom) {
		projectName = nom;
	}

	@Override
	public void setDescription(String description) {
		projectDescription = description;
	}

	@Override
	public void serialize(Serialize serializer) {
	}

	@Override
	public String serialize(Objects objects) {
		String out = "FILE\n"
		    + "version:" + Config.maximumSavefileVersion + "\n"
		    + "projectName:" + projectName + "\n"
		    + "projectDescription:" + projectDescription + "\n"
		    + "EOS:FILE\n\n"
		;

		return out;
	}

	@Override
	public void clearChildren() {
		throw new IllegalAccessError("Shouldn't call Project.clearChildren()");
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Project.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Project.removeChildren()");
	}
}
