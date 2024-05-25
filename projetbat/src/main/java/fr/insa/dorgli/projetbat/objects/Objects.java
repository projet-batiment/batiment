package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.types.*;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;

public class Objects {
	// liste des objets
	private final HashSet<Batiment> batiments = new HashSet<>();
	private final HashSet<Niveau> niveaux = new HashSet<>();
	private final HashSet<Appart> apparts = new HashSet<>();
	private final HashSet<Piece> pieces = new HashSet<>();
	private final HashSet<Mur> murs = new HashSet<>();
	private final HashSet<PlafondSol> plafondsSols = new HashSet<>();
	private final HashSet<Point> points = new HashSet<>();

	private final HashSet<OuvertureMur> ouverturesMur = new HashSet<>();
	private final HashSet<OuvertureNiveaux> ouverturesNiveaux = new HashSet<>();

	private final HashSet<RevetementMur> revetementsMur = new HashSet<>();
	private final HashSet<RevetementPlafondSol> revetementsPlafondSol = new HashSet<>();

	private final HashSet<TypeBatiment> typesBatiment = new HashSet<>();
	private final HashSet<TypeAppart> typesAppart = new HashSet<>();
	private final HashSet<TypeMur> typesMur = new HashSet<>();
	private final HashSet<TypeOuvertureMur> typesOuverturesMur = new HashSet<>();
	private final HashSet<TypeOuvertureNiveaux> typesOuverturesNiveaux = new HashSet<>();
	private final HashSet<TypeRevetement> typesRevetement = new HashSet<>();

	private final HashSet<Devis> devis = new HashSet<>();

	private final HashMap<Integer, SelectableId> all = new HashMap<>();

	private int idCounter = 0;

	///// serialize

	public void serialize(Serialize serializer) {
		////////// types

		serializer.objectsSection("TypeBatiment");
		for (TypeBatiment each: typesBatiment)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("TypeAppart");
		for (TypeAppart each: typesAppart)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("TypeMur");
		for (TypeMur each: typesMur)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("TypeRevetement");
		for (TypeRevetement each: typesRevetement)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("TypeOuvertureNiveaux");
		for (TypeOuvertureNiveaux each: typesOuverturesNiveaux)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("TypeOuvertureMur");
		for (TypeOuvertureMur each: typesOuverturesMur)
			each.serialize(serializer);
		serializer.eos();

		////////// basic

		serializer.objectsSection("Point");
		for (Point each: points)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("Mur");
		for (Mur each: murs)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("Piece");
		for (Piece each: pieces)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("Appart");
		for (Appart each: apparts)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("Niveau");
		for (Niveau each: niveaux)
			each.serialize(serializer);
		serializer.eos();

		serializer.objectsSection("Batiment");
		for (Batiment each: batiments)
			each.serialize(serializer);
		serializer.eos();

		////////// devis

		serializer.objectsSection("Devis");
		for (Devis each: devis)
			each.serialize(serializer);
		serializer.eos();
	}

	public String serialize() {
		String out = new String();

		out += "OBJECTS:Point\n\n";
		for (Point each: points)
			out += each.serialize(this) + "\n";
		out += "EOS:Point\n\n";

		out += "OBJECTS:TypeRevetement\n\n";
		for (TypeRevetement each: typesRevetement)
			out += each.serialize(this) + "\n";
		out += "EOS:TypeRevetement\n\n";

		out += "OBJECTS:TypeOuvertureMur\n\n";
		for (TypeOuvertureMur each: typesOuverturesMur)
			out += each.serialize(this) + "\n";
		out += "EOS:TypeOuvertureMur\n\n";

		out += "OBJECTS:TypeOuvertureNiveau\n\n";
		for (TypeOuvertureNiveaux each: typesOuverturesNiveaux)
			out += each.serialize(this) + "\n";
		out += "EOS:TypeOuvertureNiveau\n\n";

		out += "OBJECTS:TypeMur\n\n";
		for (TypeMur each: typesMur)
			out += each.serialize(this) + "\n";
		out += "EOS:TypeMur\n\n";

		out += "OBJECTS:TypeAppart\n\n";
		for (TypeAppart each: typesAppart)
			out += each.serialize(this) + "\n";
		out += "EOS:TypeAppart\n\n";

		out += "OBJECTS:Mur\n\n";
		for (Mur each: murs)
			out += each.serialize(this) + "\n";
		out += "EOS:Mur\n\n";

		out += "OBJECTS:Piece\n\n";
		for (Piece each: pieces)
			out += each.serialize(this) + "\n";
		out += "EOS:Piece\n\n";

		out += "OBJECTS:Appart\n\n";
		for (Appart each: apparts)
			out += each.serialize(this) + "\n";
		out += "EOS:Appart\n\n";

		out += "OBJECTS:Niveau\n\n";
		for (Niveau each: niveaux)
			out += each.serialize(this) + "\n";
		out += "EOS:Niveau\n\n";

		out += "OBJECTS:Batiment\n\n";
		for (Batiment each: batiments)
			out += each.serialize(this) + "\n";
		out += "EOS:Batiment\n\n";

		out += "OBJECTS:Devis\n\n";
		for (Devis each: devis)
			out += each.serialize(this) + "\n";
		out += "EOS:Devis\n\n";

		return out;
	}

	///// idCounter generator + setter

	public int generateId() {
		while (all.get(idCounter) != null) {
			idCounter++;

			if (idCounter < 0) // in case of overflow
				idCounter = 1;
		}

		return idCounter;
	}

	public void setIdCounter(int newIdCounter) {
		idCounter = newIdCounter;
	}

	///// create objects (with ID & into matching HashMap)

	/**
	 * add an object to the list of all objects and of its type
	 * @param object
	 * @return the id given to the object, or -1 if the id is already held by another object 
	 */
	public int put(SelectableId object) {
		if (object.getId() <= 0) {
			object.setId(generateId());
		}

		if (all.get(object.getId()) != null) {
			return -1;
		}

		switch (object) {
			case Batiment batiment -> batiments.add(batiment);
			case Niveau niveau -> niveaux.add(niveau);
			case Appart each -> apparts.add(each);
			case Piece each -> pieces.add(each);
			case Mur each -> murs.add(each);
			case PlafondSol each -> plafondsSols.add(each);
			case Point each -> points.add(each);

			case OuvertureMur each -> ouverturesMur.add(each);
			case OuvertureNiveaux each -> ouverturesNiveaux.add(each);

			case RevetementMur each -> revetementsMur.add(each);
			case RevetementPlafondSol each -> revetementsPlafondSol.add(each);

			case TypeMur each -> typesMur.add(each);
			case TypeOuvertureMur each -> typesOuverturesMur.add(each);
			case TypeOuvertureNiveaux each -> typesOuverturesNiveaux.add(each);
			case TypeRevetement each -> typesRevetement.add(each);
			case TypeAppart each -> typesAppart.add(each);
			case TypeBatiment each -> typesBatiment.add(each);

			case Devis each -> devis.add(each);

			default -> throw new AssertionError();
		}

		all.put(object.getId(), object);
		return object.getId();
	}

	/**
	 * remove an object from the list of all objects and of its type
	 * @param object
	 */
	public void remove(SelectableId object) {
		if (all.get(object.getId()) != null) {
			return;
		}

		switch (object) {
			case Batiment batiment -> batiments.remove(batiment);
			case Niveau niveau -> niveaux.add(niveau);
			case Appart each -> apparts.add(each);
			case Piece each -> pieces.add(each);
			case Mur each -> murs.add(each);
			case PlafondSol each -> plafondsSols.add(each);
			case Point each -> points.add(each);

			case OuvertureMur each -> ouverturesMur.add(each);
			case OuvertureNiveaux each -> ouverturesNiveaux.add(each);

			case RevetementMur each -> revetementsMur.add(each);
			case RevetementPlafondSol each -> revetementsPlafondSol.add(each);

			case TypeMur each -> typesMur.add(each);
			case TypeOuvertureMur each -> typesOuverturesMur.add(each);
			case TypeOuvertureNiveaux each -> typesOuverturesNiveaux.add(each);
			case TypeRevetement each -> typesRevetement.add(each);
			case TypeAppart each -> typesAppart.add(each);
			case TypeBatiment each -> typesBatiment.add(each);

			case Devis each -> devis.add(each);

			default -> throw new AssertionError();
		}

		all.remove(object.getId());
	}

	public SelectableId get(int id) {
		return all.get(id);
	}

	public HashMap<Integer, SelectableId> getAll() {
		return all;
	}


	public HashSet<Batiment> getBatiments() {
		return batiments;
	}
	public HashSet<Niveau> getNiveaux() {
		return niveaux;
	}
	public HashSet<Appart> getApparts() {
		return apparts;
	}
	public HashSet<Piece> getPieces() {
		return pieces;
	}
	public HashSet<Mur> getMurs() {
		return murs;
	}
	public HashSet<PlafondSol> getPlafondsSols() {
		return plafondsSols;
	}
	public HashSet<Point> getPoints() {
		return points;
	}

	public HashSet<OuvertureMur> getOuverturesMur() {
		return ouverturesMur;
	}
	public HashSet<OuvertureNiveaux> getOuverturesNiveaux() {
		return ouverturesNiveaux;
	}

	public HashSet<RevetementMur> getRevetementsMur() {
		return revetementsMur;
	}
	public HashSet<RevetementPlafondSol> getRevetementsPlafondSol() {
		return revetementsPlafondSol;
	}

	public HashSet<TypeMur> getTypesMur() {
		return typesMur;
	}
	public HashSet<TypeOuvertureMur> getTypesOuverturesMur() {
		return typesOuverturesMur;
	}
	public HashSet<TypeOuvertureNiveaux> getTypesOuverturesNiveau() {
		return typesOuverturesNiveaux;
	}
	public HashSet<TypeRevetement> getTypesRevetement() {
		return typesRevetement;
	}
	public HashSet<TypeAppart> getTypesAppart() {
		return typesAppart;
	}
	public HashSet<TypeBatiment> getTypesBatiment() {
		return typesBatiment;
	}

	public HashSet<Devis> getDevis() {
		return devis;
	}

	@Override
	public String toString() {
		return new StructuredToString.OfFancyToStrings(0, "Objects")
		    .field("typesBatiment", (Collection<FancyToStrings>) ((Collection<?>)typesBatiment) )
		    .field("typesAppart", (Collection<FancyToStrings>) ((Collection<?>)typesAppart) )
		    .field("typesMur", (Collection<FancyToStrings>) ((Collection<?>)typesMur) )
		    .field("typesOuverturesMur", (Collection<FancyToStrings>) ((Collection<?>)typesOuverturesMur) )
		    .field("typesOuverturesNiveau", (Collection<FancyToStrings>) ((Collection<?>)typesOuverturesNiveaux) )
		    .field("typesRevetement", (Collection<FancyToStrings>) ((Collection<?>)typesRevetement) )

		    .field("OuverturesMur", (Collection<FancyToStrings>) ((Collection<?>)ouverturesMur) )
		    .field("OuverturesNiveaux", (Collection<FancyToStrings>) ((Collection<?>)ouverturesNiveaux) )
		    .field("RevetementMur", (Collection<FancyToStrings>) ((Collection<?>)revetementsMur) )
		    .field("RevetementPlafondSol", (Collection<FancyToStrings>) ((Collection<?>)revetementsPlafondSol) )

		    .field("batiments", (Collection<FancyToStrings>) ((Collection<?>)batiments) )
		    .field("niveaux", (Collection<FancyToStrings>) ((Collection<?>)niveaux) )
		    .field("apparts", (Collection<FancyToStrings>) ((Collection<?>)apparts) )
		    .field("pieces", (Collection<FancyToStrings>) ((Collection<?>)pieces) )
		    .field("plafondSols", (Collection<FancyToStrings>) ((Collection<?>)murs) )
		    .field("murs", (Collection<FancyToStrings>) ((Collection<?>)murs) )
		    .field("points", (Collection<FancyToStrings>) ((Collection<?>)points) )

		    .field("devis", (Collection<FancyToStrings>) ((Collection<?>)devis) )

		    .getValue()
		;
	}
}
