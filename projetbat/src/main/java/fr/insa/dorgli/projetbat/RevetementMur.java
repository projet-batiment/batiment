package fr.insa.dorgli.projetbat;

public class RevetementMur implements ToStringShort {
	TypeRevetement typeRevetement;
	double pos1L;
	double pos1H;
	double pos2L;
	double pos2H;

	public RevetementMur(TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
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

	public double getPos1L() {
		return pos1L;
	}

	public void setPos1L(int pos1L) {
		this.pos1L = pos1L;
	}

	public double getPos1H() {
		return pos1H;
	}

	public void setPos1H(int pos1H) {
		this.pos1H = pos1H;
	}

	public double getPos2L() {
		return pos2L;
	}

	public void setPos2L(int pos2L) {
		this.pos2L = pos2L;
	}

	public double getPos2H() {
		return pos2H;
	}

	public void setPos2H(int pos2H) {
		this.pos2H = pos2H;
	}

	public String toString() {
		return "RevetementMur { type -> '" + typeRevetement.getNom() + "'"
		    + ", positions: (" + pos1L + ", " + pos1H + ") -- (" + pos2L + ", " + pos2H + ") }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID au lieu du nom
		return "( #" + typeRevetement.getNom() + ", (" + pos1L + ", " + pos1H + " | " + pos2L + ", " + pos2H + ") )";
	}
}
