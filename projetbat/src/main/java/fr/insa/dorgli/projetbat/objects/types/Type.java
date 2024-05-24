package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public abstract class Type extends BObject implements NameDesc {
	protected String nom;
	protected String description;

	public Type() {
		this.nom = "Nouveau " + this.getClass().getSimpleName();
		this.description = new String();
	}
	public Type(int id, String nom, String description) {
		super(id);
		this.nom = nom;
		this.description = description;
	}

	@Override
	public final String getNom() {
		return nom;
	}

	@Override
	public final void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
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

	@Override
	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description)
		);
	}

	@Override
	public void clearChildren() {
		for (SelectableId each: super.getParents()) {
			each.removeChildren(this);
		}
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Type.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Type.removeChildren()");
	}
}
