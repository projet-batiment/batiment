package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Batiment extends HasPrice {
	private String nom;
	private String description;
	private TypeBatiment typeBatiment;
	private ArrayList<Niveau> niveaux;
	private ArrayList<Appart> apparts;

	public Batiment(int id, String nom, String description, TypeBatiment typeBatiment, ArrayList<Niveau> niveaux, ArrayList<Appart> apparts) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.typeBatiment = typeBatiment;
		this.niveaux = niveaux;
		this.apparts = apparts;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Niveau> getNiveaux() {
		return niveaux;
	}

	public void setNiveaux(ArrayList<Niveau> niveaux) {
		this.niveaux = niveaux;
	}

	public ArrayList<Appart> getApparts() {
		return apparts;
	}

	public void setApparts(ArrayList<Appart> apparts) {
		this.apparts = apparts;
	}

	public TypeBatiment getTypeBatiment() {
		return typeBatiment;
	}

	public void setTypeBatiment(TypeBatiment typeBatiment) {
		this.typeBatiment = typeBatiment;
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
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("batiment.draw: cannot draw batiment");
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
		    .field("typeBatiment", typeBatiment.toString(depth + 1))
		    .fieldShortCollection("apparts", (ArrayList<BObject>) ((ArrayList<?>) apparts))
		    .field("niveaux", (ArrayList<BObject>) ((ArrayList<?>) niveaux))
        	    .getValue();
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfBatiment(this);

		String out = String.join(",",
		    String.valueOf(id),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(typeBatiment.getId())
		) + "\n";

		if (!niveaux.isEmpty()) {
			out += "PROP:niveaux\n";
			String[] niveauIds = new String[niveaux.size()];
			for (int i = 0; i < niveauIds.length; i++) {
				niveauIds[i] = String.valueOf(objects.getIdOfNiveau(niveaux.get(i)));
			}
			out += String.join(",", niveauIds) + "\n";
		}
		if (!apparts.isEmpty()) {
			out += "PROP:apparts\n";
			String[] appartIds = new String[apparts.size()];
			for (int i = 0; i < appartIds.length; i++) {
				appartIds[i] = String.valueOf(objects.getIdOfAppart(apparts.get(i)));
			}
			out += String.join(",", appartIds) + "\n";
		}

		return out + "EOS:Entry";
	}
}
