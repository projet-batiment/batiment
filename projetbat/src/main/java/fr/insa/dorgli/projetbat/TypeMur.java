package fr.insa.dorgli.projetbat;

public class TypeMur implements ToStringShort {
	private String nom;
	private String description;
	private double epaisseur;
	private double prixUnitaire;

	public TypeMur(String nom, String description, double epaisseur, double prixUnitaire) {
		this.nom = nom;
		this.description = description;
		this.epaisseur = epaisseur;
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

	public double getEpaisseur() {
		return epaisseur;
	}

	public void setEpaisseur(double epaisseur) {
		this.epaisseur = epaisseur;
	}

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public double PrixStructureMur(){
		prixStructureMur = (aireMur * this.prixUnitaire);
		return prixStructureMur;
	}

	public String toString() {
		return "TypeMur { epaisseur: " + epaisseur + ", prixUnitaire: " + prixUnitaire + ", nom: " + nom + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
