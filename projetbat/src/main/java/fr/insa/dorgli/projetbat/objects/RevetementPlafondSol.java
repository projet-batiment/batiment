package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.gui.DrawingContext;
import fr.insa.dorgli.projetbat.StructuredToString;

public class RevetementPlafondSol extends HasPrice {
	TypeRevetement typeRevetement;
	// TODO: à terme, définir les revetements plafond et sol avec des points verticaux sur les murs ?
	double pos1L;
	double pos1H;
	double pos2L;
	double pos2H;

	public RevetementPlafondSol(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		super(id);
		this.typeRevetement = typeRevetement;
		this.pos1L = pos1L;
		this.pos1H = pos1H;
		this.pos2L = pos2L;
		this.pos2H = pos2H;
	}

	public TypeRevetement getTypeRevetement() {
		return typeRevetement;
	}

	public void setTypeRevetement(TypeRevetement typeRevetement) {
		this.typeRevetement = typeRevetement;
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
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("pos1L", ""+pos1L)
		    .field("pos1H", ""+pos1H)
		    .field("pos2L", ""+pos2L)
		    .field("pos2H", ""+pos2H)
		    .field("typeOuverture", typeRevetement.toString(depth + 1))
            .getValue();
	}

	@Override
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("revetementMur.draw: cannot draw revetementMur");
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfRevetementPlafondSol(this);
		int rId = objects.getIdOfTypeRevetement(typeRevetement);

		return String.join(",",
		    String.valueOf(id),
		    String.valueOf(rId),
		    String.valueOf(pos1L),
		    String.valueOf(pos1H),
		    String.valueOf(pos2L),
		    String.valueOf(pos2H));
	}
}
