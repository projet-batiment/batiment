package fr.insa.dorgli.projetbat;

public class RevetementPlafondSol implements ToStringShort {
	TypeRevetement typeRevetement;
	// TODO: à terme, définir les revetements plafond et sol avec des points verticaux sur les murs ?
	int pos1L;
	int pos1H;
	int pos2L;
	int pos2H;

	public RevetementPlafondSol(TypeRevetement typeRevetement, int pos1L, int pos1H, int pos2L, int pos2H) {
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

	public int getPos1L() {
		return pos1L;
	}

	public void setPos1L(int pos1L) {
		this.pos1L = pos1L;
	}

	public int getPos1H() {
		return pos1H;
	}

	public void setPos1H(int pos1H) {
		this.pos1H = pos1H;
	}

	public int getPos2L() {
		return pos2L;
	}

	public void setPos2L(int pos2L) {
		this.pos2L = pos2L;
	}

	public int getPos2H() {
		return pos2H;
	}

	public void setPos2H(int pos2H) {
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

	public String toString() {
		return "RevetementSol { type -> " + typeRevetement.getNom() + ", positions: (" + pos1L + ", " + pos1H + ") -> ("
				+ pos2L + ", " + pos2H + ") }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID au lieu du nom
		return "( #" + typeRevetement.getNom() + ", (" + pos1L + "," + pos1H + " | " + pos2L + "," + pos2H + ") )";
	}
}
