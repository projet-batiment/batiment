package fr.insa.dorgli.projetbat;

public class RevetementMur implements ToStringShort {
	TypeRevetement typeRevetement;
	int pos1L;
	int pos1H;
	int pos2L;
	int pos2H;

	public RevetementMur(TypeRevetement typeRevetement, int pos1L, int pos1H, int pos2L, int pos2H) {
		this.typeRevetement = typeRevetement;
		this.pos1L = pos1L;
		this.pos1H = pos1H;
		this.pos2L = pos2L;
		this.pos2H = pos2H;
	}

	public TypeRevetement getTypeRevetement() {
		return typeRevetement;
	}

	public void setTypeRevetement(TypeRevetement typeRevetement) {
		this.typeRevetement = typeRevetement;
	}

	public int getPos1L() {
		return pos1L;
	}

	public void setPos1L(int pos1L) {
		this.pos1L = pos1L;
	}

	public int getPos1H() {
		return pos1H;
	}

	public void setPos1H(int pos1H) {
		this.pos1H = pos1H;
	}

	public int getPos2L() {
		return pos2L;
	}

	public void setPos2L(int pos2L) {
		this.pos2L = pos2L;
	}

	public int getPos2H() {
		return pos2H;
	}

	public void setPos2H(int pos2H) {
		this.pos2H = pos2H;
	}

	public double aire(double largeurMur, double hauteurMur) {
		double largeur;
		double hauteur;

		/// TODO!!! implement java.awt.Area -> intersect the revetements' surfaces with the ouvertures' surfaces
		/// TODO: si un jour on implémente les PlafondSol polygonaux plus que rectangulaires...
		if ((pos1L == 0 ) && (pos2L == 0 )){
			largeur = largeurMur;
		} else {
			largeur = this.pos1L - this.pos2L;
		}
		if ((pos1H == 0) && (pos2H == 0)) {
			hauteur = hauteurMur;
		} else {
			hauteur = this.pos1H - this.pos2H;
		}

		return largeur * hauteur;
	}

	public double calculerPrix(double largeurMur, double hauteurMur) {
		return typeRevetement.getPrixUnitaire() * aire(largeurMur, hauteurMur);
	}

	public String toString() {
		return "RevetementMur { type -> " + typeRevetement.getNom() + ", positions: (" + pos1L + ", " + pos1H + ") -> ("
				+ pos2L + ", " + pos2H + ") }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID au lieu du nom
		return "( #" + typeRevetement.getNom() + ", (" + pos1L + "," + pos1H + " | " + pos2L + "," + pos2H + ") )";
	}
}
