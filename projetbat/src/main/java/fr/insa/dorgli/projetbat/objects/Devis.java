package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.HashSet;

public class Devis extends SelectableId implements NameDesc, HasPrice {
	private String nom;
	private String description;
	private HashSet<HasPrice> studiedObjects;

	private HashSet<FancyToStrings> asFancyToStrings() {
		return asFancyToStrings(studiedObjects);
	}
	private HashSet<FancyToStrings> asFancyToStrings(HashSet<HasPrice> set) {
		return (HashSet<FancyToStrings>) ((HashSet<?>) set);
	}

	public Devis(HasPrice studiedObject) throws IllegalArgumentException {
		this(new String(), new String(), studiedObject);
	}

	public Devis(HashSet<HasPrice> studiedObjects) throws IllegalArgumentException {
		this(new String(), new String(), studiedObjects);
	}

	public Devis(String nom, String description, HasPrice studiedObject) throws IllegalArgumentException {
		HashSet<HasPrice> tmpStudiedObjects = new HashSet<>();
		System.out.println(studiedObject.toString());
		tmpStudiedObjects.add(studiedObject);

		// this could potentially fail
		setStudiedObject(tmpStudiedObjects);

		this.nom = nom;
		this.description = description;
	}

	public Devis(String nom, String description, HashSet<HasPrice> studiedObjects) throws IllegalArgumentException {
		// this could potentially fail
		setStudiedObject(studiedObjects);

		this.nom = nom;
		this.description = description;
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public HashSet<HasPrice> getStudiedObject() {
		return studiedObjects;
	}

	public final void setStudiedObject(HashSet<HasPrice> studiedObjects) throws IllegalArgumentException {
		if (this.studiedObjects == null) {
			this.studiedObjects = studiedObjects;
		} else {
			String ourShortStrings = asFancyToStrings().stream()
			    .map(each -> each.toStringShort())
			    .reduce(", ", String::concat);

			String thierShortStrings = asFancyToStrings(studiedObjects).stream()
			    .map(each -> each.toStringShort())
			    .reduce(", ", String::concat);

			throw new IllegalArgumentException("Already got a studiedObjects: [ "
			    + ourShortStrings + " ] != null ( [ " + thierShortStrings + " ] )");
		}

		for (HasPrice studiedObject: studiedObjects) {
			if (studiedObject instanceof FancyToStrings fancyToStringsObject) {
			} else {
				throw new IllegalArgumentException("Expected studiedObject to be an instance of BObject, but got " + studiedObject.getClass().getSimpleName());
			}
		}
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .fieldShortCollection("studiedObject", asFancyToStrings())
		    .field("studiedObject price", prixToString())
        	    .getValue();
	}

	@Override
	public String serialize(Objects objects) {
		throw new UnsupportedOperationException("TODO!!!");
	}

	@Override
	public double calculerPrix() {
		var prixWrapper = new Object(){ double prix = 0; };

		studiedObjects.stream()
		    .forEach((HasPrice each) -> prixWrapper.prix += each.calculerPrix())
		;

		return prixWrapper.prix;
	}

	public String prixToString() {
		return Math.round(this.calculerPrix() * 100 ) / 100 + "€";
	}
}
