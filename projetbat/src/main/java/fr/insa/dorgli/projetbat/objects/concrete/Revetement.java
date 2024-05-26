package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Name;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

// une classe abstraite qui n'aurait à terme pas dû exister : 
// les revetementPlafondSol auraient été conscient de réels Points
// et les revetementMur auraient potentiellement pu contenir des Point2D sur le mur
// mais vu que c'est pas le cas, on simplifie la structure !!

public abstract class Revetement extends Drawable implements HasPrice, Name {
	protected TypeRevetement typeRevetement;
	protected double pos1L;
	protected double pos1H;
	protected double pos2L;
	protected double pos2H;

	public Revetement() {
		pos1L = 0;
		pos1H = 0;
		pos2L = 0;
		pos2H = 0;
	}
	public Revetement(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
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

	public void setPos1L(double pos1L) {
		this.pos1L = pos1L;
	}

	public double getPos1H() {
		return pos1H;
	}

	public void setPos1H(double pos1H) {
		this.pos1H = pos1H;
	}

	public double getPos2L() {
		return pos2L;
	}

	public void setPos2L(double pos2L) {
		this.pos2L = pos2L;
	}

	public double getPos2H() {
		return pos2H;
	}

	public void setPos2H(double pos2H) {
		this.pos2H = pos2H;
	}

	public abstract double aire();
	public abstract boolean isReactive();

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
		    .field("typeRevetement", typeRevetement)
        	    .getValue();
	}

	@Override
	public void draw(DrawingContext ctxt, DrawingContext.ObjectState ostate) {
		ctxt.tui().error("revetement.draw: cannot draw revetement");
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
			super.getId(),
			typeRevetement.getId(),
			pos1L,
			pos1H,
			pos2L,
			pos2H
		);
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(typeRevetement.getId()),
		    String.valueOf(pos1L),
		    String.valueOf(pos1H),
		    String.valueOf(pos2L),
		    String.valueOf(pos2H));
	}

	@Override
	public void clearChildren() {
		typeRevetement = null;
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Revetement.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Revetement.removeChildren()");
	}

	@Override
	public String getNom() {
		return typeRevetement.getNom();
	}

	@Override
	public void setNom(String nom) {
		// exists though shouldn't be called
		typeRevetement.setNom(nom);
	}
}
