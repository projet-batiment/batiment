package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;
import fr.insa.dorgli.projetbat.StructuredToString;

public class Piece extends HasPrice {
	private String nom;
	private String description;
	private ArrayList<Point> points;
	private ArrayList<Mur> murs;
	private PlafondSol plafond;
	private PlafondSol sol;

	public Piece(int id, String nom, String description, ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol) {
		super(id);
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

	public void draw(DrawingContext dcx, boolean isFocused) {
		dcx.tui().diveWhere("piece.draw");

		dcx.tui().debug("in" + (isFocused ? " focused" : "") + " Piece " + this.toStringShort());
		dcx.tui().debug("drawing " + murs.size() + " murs objects");
		for (Mur mur: murs) {
			dcx.draw(mur);
		}

		dcx.tui().popWhere();
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
		    .field("points", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) points)) )
		    .field("murs", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) murs)))
		    .field("plafond", plafond.toString(depth + 1))
		    .field("sol", sol.toString(depth + 1))
            .getValue();
	}
}
