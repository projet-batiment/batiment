package fr.insa.dorgli.projetbat;

public class OuvertureMur implements ToStringShort {
	TypeOuvertureMur typeOuverture;
	double posL;
	double posH;

	public OuvertureMur(TypeOuvertureMur typeOuverture, double posL, double posH) {
		this.typeOuverture = typeOuverture;
		this.posL = posL;
		this.posH = posH;
	}

	public TypeOuvertureMur getTypeOuverture() {
		return typeOuverture;
	}

	public void setTypeOuverture(TypeOuvertureMur typeOuverture) {
		this.typeOuverture = typeOuverture;
	}

	public double getPosL() {
		return posL;
	}

	public void setPosL(double posL) {
		this.posL = posL;
	}

	public double getPosH() {
		return posH;
	}

	public void setPosH(double posH) {
		this.posH = posH;
	}

	public String toString() {
		return "OuvertureMur { typeOuverture: " + typeOuverture.toStringShort() + ", position: (" + posL + ", "
				+ posH + ") }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
