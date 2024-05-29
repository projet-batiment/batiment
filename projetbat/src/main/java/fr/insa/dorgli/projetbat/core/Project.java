package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.io.File;

public class Project extends SelectableId implements HasInnerPrice, NameDesc {
	public Objects objects;

	public String projectName;
	public String projectDescription;
	public File file;
	public String savefilePath;

	public Project() {
		super(0);

		objects = new Objects();
		projectName = new String();
		projectDescription = new String();
		file = null;
	}

	@Override
	public String toStringShort() {
		return "Project";
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("projectName", projectName)
		    .textField("projectDescription", projectDescription)
		    .textField("file", file == null ? "(null)" : file.getPath())
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
		serializer.section("FILE");
		serializer.csv("version", Config.maximumSavefileVersion);
		serializer.csv("projectName", projectName);
		serializer.csv("projectDescription", projectDescription);
		serializer.eos();

		objects.serialize(serializer);

		SelectableId currentBatiment = serializer.config.controller.state.getCurrentBatiment();
		SelectableId viewRootElement = serializer.config.controller.state.getViewRootElement();
		int batimentId = currentBatiment == null ? -1 : currentBatiment.getId();
		int viewRootId = viewRootElement == null ? -1 : viewRootElement.getId();

		serializer.section("FILE");
		serializer.csv("last view", batimentId, viewRootId);
		serializer.eos();
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
