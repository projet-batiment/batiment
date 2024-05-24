package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.objects.types.TypeBatiment;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import java.util.ArrayList;
import java.util.Collection;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.Collections;
import java.util.List;

public class Batiment extends BObject implements HasInnerPrice, NameDesc {
	private String nom;
	private String description;
	private TypeBatiment typeBatiment;
	private ArrayList<Niveau> niveaux;
	private ArrayList<Appart> apparts;

	public Batiment() {
		nom = "Nouveau bâtiment";
		description = new String();
		niveaux = new ArrayList<>();
		apparts = new ArrayList<>();
	}
	public Batiment(int id, String nom, String description, TypeBatiment typeBatiment, ArrayList<Niveau> niveaux, ArrayList<Appart> apparts) {
		super(id);
		this.nom = nom;
		this.description = description;

		setTypeBatiment(typeBatiment);

		this.niveaux = new ArrayList<>();
		this.apparts = new ArrayList<>();
		addChildren((BObject[]) niveaux.toArray(BObject[]::new));
		addChildren((BObject[]) apparts.toArray(BObject[]::new));
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

	public List<Niveau> getNiveaux() {
		return Collections.unmodifiableList(niveaux);
	}

	public void setNiveaux(ArrayList<Niveau> niveaux) {
		this.niveaux = niveaux;
	}

	public List<Appart> getApparts() {
		return Collections.unmodifiableList(apparts);
	}

	public void setApparts(ArrayList<Appart> apparts) {
		this.apparts = apparts;
	}

	public TypeBatiment getTypeBatiment() {
		return typeBatiment;
	}

	public final void setTypeBatiment(TypeBatiment typeBatiment) {
		this.typeBatiment = typeBatiment;
		typeBatiment.addParents(this);
	}

	@Override
	public double calculerPrix() {
		double prixBatiment = 0;

		for (Niveau eachNiveau: niveaux){
			prixBatiment += eachNiveau.calculerPrix();
		}

		return prixBatiment;
	}

	@Override
	public void calculerPrix(Devis.DevisCalculator calculator) {
		for (Niveau each: niveaux){
			calculator.addObject(each);
		}
	}

	@Override
	public boolean ready() {
		return (
			typeBatiment != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("typeBatiment", typeBatiment.toString(depth + 1))
		    .fieldShortCollection("apparts", (Collection<FancyToStrings>) ((ArrayList<?>) apparts))
		    .fieldShortCollection("niveaux", (Collection<FancyToStrings>) ((ArrayList<?>) niveaux))
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId(),
		    nom,
		    description,
		    typeBatiment.getId()
		);

		if (!niveaux.isEmpty()) {
			serializer.prop("niveaux");
			serializer.csv(niveaux.stream().map(each -> (int) each.getId()));
		}
		if (!apparts.isEmpty()) {
			serializer.prop("apparts");
			serializer.csv(apparts.stream().map(each -> (int) each.getId()));
		}

		serializer.eoEntry();
	}

	@Override
	public String serialize(Objects objects) {
		String out = String.join(",",
		    String.valueOf(super.getId()),
		    EscapeStrings.escapeString(nom),
		    EscapeStrings.escapeString(description),
		    String.valueOf(typeBatiment.getId())
		) + "\n";

		if (!niveaux.isEmpty()) {
			out += "PROP:niveaux\n";
			String[] niveauIds = new String[niveaux.size()];
			for (int i = 0; i < niveauIds.length; i++) {
				niveauIds[i] = String.valueOf(niveaux.get(i).getId());
			}
			out += String.join(",", niveauIds) + "\n";
		}
		if (!apparts.isEmpty()) {
			out += "PROP:apparts\n";
			String[] appartIds = new String[apparts.size()];
			for (int i = 0; i < appartIds.length; i++) {
				appartIds[i] = String.valueOf(apparts.get(i).getId());
			}
			out += String.join(",", appartIds) + "\n";
		}

		return out + "EOS:Entry";
	}

	@Override
	public void clearChildren() {
		apparts.clear();
		niveaux.clear();
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Appart known -> apparts.add(known);
				case Niveau known -> niveaux.add(known);

				default -> throw new IllegalArgumentException("Unknown children type for batiment: " + object.getClass().getSimpleName());
			}
			object.addParents(this);
		}
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Appart known -> apparts.remove(known);
				case Niveau known -> niveaux.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for batiment: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
