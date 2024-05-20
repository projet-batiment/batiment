package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.concrete.Objects;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public abstract class Type extends BObject {
	protected String nom;
	protected String description;

	public Type() {
		this.nom = new String();
		this.description = new String();
	}
	public Type(int id, String nom, String description) {
		super(id);
		this.nom = nom;
		this.description = description;
	}

	public final String getNom() {
		return nom;
	}

	public final void setNom(String nom) {
		this.nom = nom;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	@Override
	public final boolean ready() {
		return true;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
        	    .getValue();
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description)
		);
	}
}
