package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;
import fr.insa.dorgli.projetbat.StructuredToString;

public class Niveau extends Drawable {
	private String nom;
	private String description;
	private double hauteur;
	private ArrayList<Piece> pieces;
	private ArrayList<Appart> apparts;

	public Niveau(int id, String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;
		this.pieces = pieces;
		this.apparts = apparts;
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

	public double getHauteur() {
		return hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public ArrayList<Appart> getApparts() {
		return apparts;
	}

	public void setApparts(ArrayList<Appart> apparts) {
		this.apparts = apparts;
	}

	public void draw(DrawingContext dcx, boolean isFocused) {
		dcx.tui().diveWhere("niveau.draw");

		dcx.tui().debug("in" + (isFocused ? " focused" : "") + " Niveau " + this.toStringShort());
		dcx.tui().debug("drawing " + pieces.size() + " piece objects");
		for (Piece piece: pieces) {
			dcx.draw(piece);
		}

		dcx.tui().popWhere();
	}

	public double calculerPrix() {
		double prixNiveau = 0;

		for (Piece eachPiece: pieces){
			prixNiveau += eachPiece.calculerPrix();
		}
		return prixNiveau;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
		    .field("hauteur", ""+hauteur)
		    .field("pieces", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) pieces)))
		    .field("apparts", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) apparts)))
            .getValue();
	}
}
