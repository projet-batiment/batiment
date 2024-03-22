package main.java.fr.insa.dorgli.projetbat;

public class OuvertureMur implements ToStringShort {
    TypeOuvertureMur typeOuvertureMur;
    double posL;
    double posH;

    public OuvertureMur(TypeOuvertureMur typeOuvertureMur, double posL, double posH) {
        this.typeOuvertureMur = typeOuvertureMur;
        this.posL = posL;
        this.posH = posH;
    }

    public TypeOuvertureMur getTypeOuvertureMur() {
        return typeOuvertureMur;
    }

    public void setTypeOuvertureMur(TypeOuvertureMur typeOuvertureMur) {
        this.typeOuvertureMur = typeOuvertureMur;
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
        return "OuvertureMur { typeOuvertureMur: " + typeOuvertureMur.toStringShort() + ", position: (" + posL + ", "
                + posH + ") }";
    }

    public String toStringShort() {
        // TODO -> toStringShort -> afficher l'ID
        return "( #" + "TODO" + ")";
    }
}
