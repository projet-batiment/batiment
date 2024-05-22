package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Project extends SelectableId implements HasPrice, NameDesc {
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
	public String serialize(Objects objects) {
		// TODO
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}
}
