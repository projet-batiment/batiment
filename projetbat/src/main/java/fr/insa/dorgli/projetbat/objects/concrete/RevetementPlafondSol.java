package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class RevetementPlafondSol extends Drawable implements HasPrice {
	TypeRevetement typeRevetement;
	// TODO: à terme, définir les revetements plafond et sol avec des points verticaux sur les murs ?
	double pos1L;
	double pos1H;
	double pos2L;
	double pos2H;

	public RevetementPlafondSol() {
		pos1L = 0;
		pos1H = 0;
		pos2L = 0;
		pos2H = 0;
	}
	public RevetementPlafondSol(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
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

	public double aire(double airePiece) {
		if ((pos1L == 0) && (pos2L == 0) && (pos1H == 0) && (pos2H == 0)) {
			return airePiece;
		} else {
			return Math.abs((pos1L - pos2L) * (pos1H - pos2H));
			/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
			/// pour si un jour on implémente les PlafondSol polygonaux plus que rectangulaires :
			// double out = 0;
			// for (int i = 0; i < points.size(); i++) {
			// 	Point current = points.get(i);
			// 	Point next = points.get((i + 1) % points.size());
			// 	out += current.getX() * next.getY() - current.getY() * next.getX();
			// }
			// return 0.5 * Math.abs(out);
		}
	}

	public double calculerPrix(double airePiece) {
		return typeRevetement.getPrixUnitaire() * aire(airePiece);
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
		throw new IllegalAccessError("Shouldn't call RevetementMur.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call RevetementMur.removeChildren()");
	}
}
