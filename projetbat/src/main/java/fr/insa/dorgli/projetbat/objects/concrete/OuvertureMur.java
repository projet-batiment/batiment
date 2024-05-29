package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Name;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.objects.types.TypeOuvertureMur;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;

public class OuvertureMur extends Drawable implements Name, HasPrice {
	TypeOuvertureMur typeOuverture;
	double posL;
	double posH;

	public OuvertureMur() {
		posL = 0;
		posH = 0;
	}
	public OuvertureMur(int id, TypeOuvertureMur typeOuverture, double posL, double posH) {
		super(id);
		setTypeOuverture(typeOuverture);
		this.posL = posL;
		this.posH = posH;
	}

	public TypeOuvertureMur getTypeOuverture() {
		return typeOuverture;
	}

	public final void setTypeOuverture(TypeOuvertureMur typeOuverture) {
		this.typeOuverture = typeOuverture;
		typeOuverture.addParents(this);
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
	public void draw(DrawingContext ctxt, DrawingContext.ObjectState ostate) {
		ctxt.tui().error("ouvertureMur.draw: cannot draw ouvertureMur");
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
			super.getId(),
			typeOuverture.getId(),
			posL,
			posH
		);
	}

	@Override
	public void clearChildren() {
		typeOuverture = null;
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call OuvertureMur.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call OuvertureMur.addChildren()");
	}

	@Override
	public String getNom() {
		if (typeOuverture != null)
			return typeOuverture.getNom();
		else
			return "";
	}

	@Override
	public void setNom(String nom) {
		// exists though shouldn't be called
		typeOuverture.setNom(nom);
	}

	@Override
	public double calculerPrix() {
		if (typeOuverture == null)
			return 0;

		return typeOuverture.getPrixOuverture();
	}
}
