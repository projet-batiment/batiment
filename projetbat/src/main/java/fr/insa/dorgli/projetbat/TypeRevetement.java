package fr.insa.dorgli.projetbat;

public class TypeRevetement implements ToStringShort {
	private String nom;
	private String description;
	private double prixUnitaire;

	public TypeRevetement(String nom, String description, double prixUnitaire) {
		this.nom = nom;
		this.description = description;
		this.prixUnitaire = prixUnitaire;
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

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public String toString() {
		return "TypeRevetement { prixUnitaire: " + prixUnitaire + ", nom: '" + nom + "', description: '" + description
				+ "' }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
