package fr.insa.dorgli.projetbat.objects;

public class OuvertureNiveaux {
	TypeOuvertureNiveau typeOuverture;
	double posL;
	double posH;

	public OuvertureNiveaux(TypeOuvertureNiveau typeOuverture, double posL, double posH) {
		this.typeOuverture = typeOuverture;
		this.posL = posL;
		this.posH = posH;
	}

	public TypeOuvertureNiveau getTypeOuverture() {
		return typeOuverture;
	}

	public void setTypeOuverture(TypeOuvertureNiveau typeOuverture) {
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
		return "OuvertureNiveau { typeOuverture: " + typeOuverture.toStringShort() + ", position: (" + posL + ", "
				+ posH + ") }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfOuvertureNiveaux(this);
		return String.join(",", id, typeOuvertureNiveau, posL, posH);
	}
}
