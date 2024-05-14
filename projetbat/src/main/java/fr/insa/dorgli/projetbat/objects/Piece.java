package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.Deserialize;
import fr.insa.dorgli.projetbat.ToString;
import fr.insa.dorgli.projetbat.ToStringShort;
import fr.insa.dorgli.projetbat.gui.Drawable;
import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;

public class Piece implements ToString, ToStringShort, Drawable {
	private String nom;
	private String description;
	private ArrayList<Point> points;
	private ArrayList<Mur> murs;
	private PlafondSol plafond;
	private PlafondSol sol;

	public Piece(String nom, String description, ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol) {
		this.nom = nom;
		this.description = description;
		this.points = points;
		this.murs = murs;
		this.plafond = plafond;
		this.sol = sol;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Mur> getMurs() {
		return murs;
	}

	public void setMurs(ArrayList<Mur> murs) {
		this.murs = murs;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public PlafondSol getPlafond() {
		return plafond;
	}

	public void setPlafond(PlafondSol plafond) {
		this.plafond = plafond;
	}

	public PlafondSol getSol() {
		return sol;
	}

	public void setSol(PlafondSol sol) {
		this.sol = sol;
	}

	public String toString() {
		return toString(0);
	}

	public String toString(int depth) {
		String pfx = "";
		for (int i = 0; i <= depth; i++) {
			pfx += "  ";
		}
		int nextDepth = depth + 1;

		String pointsOut = "[ ";
		for (Point each : points) {
			pointsOut += each.toStringShort() + ", ";
		}
		pointsOut += "]";

		String mursOut = "[ ";
		for (Mur each : murs) {
			mursOut += each.toStringShort() + ", ";
		}
		mursOut += "]";

		return "Piece {\n"
				+ pfx + "nom: '" + nom + "',\n"
				+ pfx + "description: '" + description + "',\n"
				+ pfx + "points: " + pointsOut + ",\n"
				+ pfx + "murs: " + mursOut + ",\n"
				+ pfx + "plafond: " + plafond.toString(depth + 1) + ",\n"
				+ pfx + "sol: " + sol.toString(depth + 1) + ",\n"
				+ "}";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + nom + " )";
	}

	public double aire() {
		double out = 0;
		/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
		for (int i = 0; i < points.size(); i++) {
			Point current = points.get(i);
			Point next = points.get((i + 1) % points.size());
			out += current.getX() * next.getY() - current.getY() * next.getX();
		}
		return 0.5 * Math.abs(out);
	}

	public double calculerPrix() {
		double prixPiece = 0;
		double airePiece = aire();

		 for (Mur eachMur: murs) {
			prixPiece += eachMur.calculerPrix();
		 }

		prixPiece += plafond.calculerPrix(airePiece);
		prixPiece += sol.calculerPrix(airePiece);

		return prixPiece;
	}

	public void draw(DrawingContext dcx) {
		dcx.tui().diveWhere("piece.draw");

		dcx.tui().debug("in Piece " + this.toStringShort());
		dcx.tui().debug("drawing " + murs.size() + " murs objects");
		for (Mur mur: murs) {
			dcx.draw(mur);
		}

		dcx.tui().popWhere();
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfPiece(this);
		String out = String.join(",",
		    String.valueOf(id),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description)
		) + "\n";

		if (!points.isEmpty()) {
			out += "PROP:points\n";
			String[] pointIds = new String[points.size()];
			for (int i = 0; i < pointIds.length; i++) {
				pointIds[i] = String.valueOf(objects.getIdOfPoint(points.get(i)));
			}
			out += String.join(",", pointIds) + "\n";
		}
		if (!murs.isEmpty()) {
			out += "PROP:murs\n";
			String[] murIds = new String[murs.size()];
			for (int i = 0; i < murIds.length; i++) {
				murIds[i] = String.valueOf(objects.getIdOfMur(murs.get(i)));
			}
			out += String.join(",", murIds) + "\n";
		}
		if (plafond != null) {
			out += "PROP:plafond\n";
			out += plafond.serialize(objects) + "\nEOS:plafond\n";
		}
		if (sol != null) {
			out += "PROP:sol\n";
			out += sol.serialize(objects) + "\nEOS:sol\n";
		}

		return out + "EOS:Entry";
	}
}
