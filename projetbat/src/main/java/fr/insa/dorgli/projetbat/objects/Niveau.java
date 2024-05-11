package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToString;
import fr.insa.dorgli.projetbat.ToStringShort;
import fr.insa.dorgli.projetbat.gui.Drawable;
import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;

public class Niveau implements ToString, ToStringShort, Drawable {
	private String nom;
	private String description;
	private double hauteur;
	private ArrayList<Piece> pieces;
	private ArrayList<Appart> apparts;

	public Niveau(String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts) {
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

	public String toString() {
		return toString(0);
	}

	public String toString(int depth) {
		String pfx = "";
		for (int i = 0; i <= depth; i++) {
			pfx += "  ";
		}
		int nextDepth = depth + 1;

		String piecesOut = "[ ";
		for (Piece each : pieces) {
			piecesOut += each.toStringShort() + ", ";
		}
		piecesOut += "]";

		String appartsOut = "[ ";
		for (Appart each : apparts) {
			appartsOut += each.toStringShort() + ", ";
		}
		appartsOut += "]";

		return "Niveau {\n"
				+ pfx + "nom: '" + nom + "',\n"
				+ pfx + "description: '" + description + "',\n"
				+ pfx + "hauteur: " + hauteur + ",\n"
				+ pfx + "pieces: " + piecesOut + ",\n"
				+ pfx + "apparts: " + appartsOut + ",\n"
				+ "}";
	}

	public void draw(DrawingContext dcx) {
		dcx.tui().diveWhere("niveau.draw");

		dcx.tui().debug("in Niveau " + this.toStringShort());
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

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfNiveau(this);
		String out = String.join(",", String.valueOf(id), nom, description, String.valueOf(hauteur)) + "\n";

		if (!pieces.isEmpty()) {
			out += "PROP:pieces\n";
			String[] pieceIds = new String[pieces.size()];
			for (int i = 0; i < pieceIds.length; i++) {
				pieceIds[i] = String.valueOf(objects.getIdOfPiece(pieces.get(i)));
			}
			out += String.join(",", pieceIds) + "\n";
		}
		if (!apparts.isEmpty()) {
			out += "PROP:apparts\n";
			String[] appartIds = new String[apparts.size()];
			for (int i = 0; i < appartIds.length; i++) {
				appartIds[i] = String.valueOf(objects.getIdOfAppart(apparts.get(i)));
			}
			out += String.join(",", appartIds) + "\n";
		}

		return out + "EOS:Entry";
	}
}
