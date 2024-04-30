package fr.insa.dorgli.projetbat.objects;

import java.util.ArrayList;

public class Batiment {
	private TypeBatiment typeBatiment;
	private ArrayList<Niveau> niveau;
	private ArrayList<Niveau> appart;

	public Batiment(TypeBatiment typeBatiment, ArrayList<Niveau> niveau, ArrayList<Niveau> appart) {
		this.typeBatiment = typeBatiment;
		this.niveau = niveau;
		this.appart = appart;
	}

	public TypeBatiment getTypeBatiment() {
		return typeBatiment;
	}

	public void setTypeBatiment(TypeBatiment typeBatiment) {
		this.typeBatiment = typeBatiment;
	}

	public ArrayList<Niveau> getNiveau() {
		return niveau;
	}

	public void setNiveau(ArrayList<Niveau> niveau) {
		this.niveau = niveau;
	}

	public ArrayList<Niveau> getAppart() {
		return appart;
	}

	public void setAppart(ArrayList<Niveau> appart) {
		this.appart = appart;
	}

	public double calculerPrix() {
		double prixBatiment = 0;

		for (Niveau eachNiveau: niveau){
			prixBatiment += eachNiveau.calculerPrix();
		}
		return prixBatiment;
	}

}
