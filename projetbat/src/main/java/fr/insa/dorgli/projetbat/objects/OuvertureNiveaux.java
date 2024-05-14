package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

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

	public String serialize(Objects objects) {
		int id = objects.getIdOfOuvertureNiveaux(this);
		return String.join(",",
		    String.valueOf(id),
		    String.valueOf(objects.getIdOfTypeOuvertureNiveau(typeOuverture)),
		    String.valueOf(posL),
		    String.valueOf(posH)
		);
	}
}
