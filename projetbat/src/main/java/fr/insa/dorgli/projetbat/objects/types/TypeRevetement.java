package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.concrete.Objects;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeRevetement extends Type {
	private double prixUnitaire;

	public TypeRevetement() {
		this.prixUnitaire = 0;
	}
	public TypeRevetement(int id, String nom, String description, double prixUnitaire) {
		super(id, nom, description);
		this.prixUnitaire = prixUnitaire;
	}

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("prixUnitaire", String.valueOf(prixUnitaire))
        	    .getValue();
	}

	@Override
	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(prixUnitaire)
		);
	}
}
