package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.types.TypeOuvertureNiveau;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class OuvertureNiveaux extends Drawable {
	TypeOuvertureNiveau typeOuverture;
	double posL;
	double posH;

	public OuvertureNiveaux() {
		posL = 0;
		posH = 0;
	}
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
	public boolean ready() {
		return (
			typeOuverture != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .field("posL", String.valueOf(posL))
		    .field("posH", String.valueOf(posH))
		    .field("typeOuverture", typeOuverture.toString(depth + 1))
        	    .getValue();
	}

	@Override
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("ouvertureNiveaux.draw: cannot draw ouvertureNiveaux");
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(typeOuverture.getId()),
		    String.valueOf(posL),
		    String.valueOf(posH)
		);
	}
}
