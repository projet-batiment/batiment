package fr.insa.dorgli.projetbat;

import java.util.ArrayList;

public class Piece implements ToString, ToStringShort {
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
		double prix = 0;
		double airePiece = aire();

		// for (Mur eachMur: murs) {
		// 	prix += eachMur.calculerPrix();
		// }

		prix += plafond.calculerPrix(airePiece);
		prix += sol.calculerPrix(airePiece);

		return prix;
	}
}
