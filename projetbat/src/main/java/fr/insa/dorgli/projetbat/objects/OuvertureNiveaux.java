package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.gui.DrawingContext;
import fr.insa.dorgli.projetbat.StructuredToString;

public class OuvertureNiveaux extends Drawable {
	TypeOuvertureNiveau typeOuverture;
	double posL;
	double posH;

	public OuvertureNiveaux(int id, TypeOuvertureNiveau typeOuverture, double posL, double posH) {
		super(id);
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

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("posL", ""+posL)
		    .field("posH", ""+posH)
		    .field("typeOuverture", typeOuverture.toString(depth + 1))
            .getValue();
	}

	@Override
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("ouvertureNiveaux.draw: cannot draw ouvertureNiveaux");
	}
}
