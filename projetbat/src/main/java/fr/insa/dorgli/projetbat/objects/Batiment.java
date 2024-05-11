package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToString;
import fr.insa.dorgli.projetbat.ToStringShort;
import java.util.ArrayList;

public class Batiment implements ToString, ToStringShort {
	private String nom;
	private String description;
	private TypeBatiment typeBatiment;
	private ArrayList<Niveau> niveaux;
	private ArrayList<Appart> apparts;

	public Batiment(String nom, String description, TypeBatiment typeBatiment, ArrayList<Niveau> niveaux, ArrayList<Appart> apparts) {
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

	public double calculerPrix() {
		double prixBatiment = 0;

		for (Niveau eachNiveau: niveaux){
			prixBatiment += eachNiveau.calculerPrix();
		}
		return prixBatiment;
	}


	public String toString() {
		return toString(0);
	}

	public String toString(int depth) {
		String pfx = "";
		for (int i = 0; i <= depth; i++) {
			pfx += "  ";
		}
		int nextDepth = depth + 1;

		String niveauxOut = "[ ";
		for (Niveau each : niveaux) {
			niveauxOut += each.toStringShort() + ", ";
		}
		niveauxOut += "]";


		String appartsOut = "[ ";
		for (Appart each : apparts) {
			appartsOut += each.toStringShort() + ", ";
		}
		appartsOut += "]";

		return "Niveau {\n"
				+ pfx + "nom: '" + nom + "',\n"
				+ pfx + "description: '" + description + "',\n"
				+ pfx + "apparts: " + appartsOut + ",\n"
				+ pfx + "niveaux: " + niveauxOut + ",\n"
				+ "}";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + nom + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfBatiment(this);
		return String.join(",", id, nom, description, typeBatiment);
	}
}
