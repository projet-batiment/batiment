package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Devis extends SelectableId implements NameDesc, HasPrice {
	private String nom;
	private String description;
	private double prixDevis; // potentiel prix des charges du devis
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
		tmpStudiedObjects.add(studiedObject);

		// this could potentially fail
		setStudiedObject(tmpStudiedObjects);

		this.nom = nom;
		this.description = description;
		this.prixDevis = 0;
	}

	public Devis(String nom, String description, HashSet<HasPrice> studiedObjects) throws IllegalArgumentException {
		// this could potentially fail
		setStudiedObject(studiedObjects);

		this.nom = nom;
		this.description = description;
		this.prixDevis = 0;
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
			    .collect(Collectors.joining(","));

			String theirShortStrings = asFancyToStrings(studiedObjects).stream()
			    .map(each -> each.toStringShort())
			    .collect(Collectors.joining(","));

			throw new IllegalArgumentException("Already got a studiedObjects: [ "
			    + ourShortStrings + " ] != null ( [ " + theirShortStrings + " ] )");
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
	public void serialize(Serialize serializer) {
	}

	@Override
	public String serialize(Objects objects) {
		throw new UnsupportedOperationException("TODO!!!");
	}

	public class DevisCalculator {
		private double prix = 0;
		private final HashSet<HasPrice> processedObjects = new HashSet<>();

		public void addObject(HasPrice object) {
			if (! processedObjects.contains(object)) {
				if (object instanceof HasInnerPrice parent)
					parent.calculerPrix(this);
				else
					prix += object.calculerPrix();

				processedObjects.add(object);
			}
		}

		public double getPrix() {
			return prix;
		}
	}

	@Override
	public double calculerPrix() {
		DevisCalculator calculator = new DevisCalculator();

		for (HasPrice each: studiedObjects) {
			calculator.addObject(each);
		}

		return calculator.getPrix() + prixDevis;
	}

	public String prixToString() {
		return Math.round(this.calculerPrix() * 100 ) / 100 + "€";
	}

	@Override
	public void clearChildren() {
		throw new IllegalAccessError("Shouldn't call Devis.clearChildren()");
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Devis.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Devis.removeChildren()");
	}
}
