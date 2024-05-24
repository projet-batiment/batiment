package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class RevetementMur extends Drawable implements HasPrice {
	TypeRevetement typeRevetement;
	double pos1L;
	double pos1H;
	double pos2L;
	double pos2H;

	public RevetementMur() {
		pos1L = 0;
		pos1H = 0;
		pos2L = 0;
		pos2H = 0;
	}
	public RevetementMur(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		super(id);
		setTypeRevetement(typeRevetement);
		this.pos1L = pos1L;
		this.pos1H = pos1H;
		this.pos2L = pos2L;
		this.pos2H = pos2H;
	}

	public TypeRevetement getTypeRevetement() {
		return typeRevetement;
	}

	public final void setTypeRevetement(TypeRevetement typeRevetement) {
		this.typeRevetement = typeRevetement;
		typeRevetement.addParents(this);
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

	@Override
	public double calculerPrix() {
		// TODO: besoin de la surface !
		return 0;
	}

	@Override
	public boolean ready() {
		return (
			typeRevetement != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .field("pos1L", String.valueOf(pos1L))
		    .field("pos1H", String.valueOf(pos1H))
		    .field("pos2L", String.valueOf(pos2L))
		    .field("pos2H", String.valueOf(pos2H))
		    .field("typeOuverture", typeRevetement.toString(depth + 1))
        	    .getValue();
	}

	@Override
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("revetementMur.draw: cannot draw revetementMur");
	}

	@Override
	public void serialize(Serialize serializer) {
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(typeRevetement.getId()),
		    String.valueOf(pos1L),
		    String.valueOf(pos1H),
		    String.valueOf(pos2L),
		    String.valueOf(pos2H)
		);
	}

	@Override
	public void clearChildren() {
		typeRevetement = null;
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call RevetementMur.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call RevetementMur.removeChildren()");
	}
}
