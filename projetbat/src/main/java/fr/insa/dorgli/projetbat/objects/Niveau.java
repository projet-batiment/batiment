package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class Niveau extends Drawable {
	private String nom;
	private String description;
	private double hauteur;
	private ArrayList<Piece> pieces;
	private ArrayList<Appart> apparts;
	private ArrayList<Mur> orpheanMurs;

	public Niveau(int id, String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts, ArrayList<Mur> orpheanMurs) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;
		this.pieces = pieces;
		this.apparts = apparts;
		this.orpheanMurs = orpheanMurs;
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

	public ArrayList<Mur> getOrpheanMurs() {
		return orpheanMurs;
	}

	public void setOrpheanMurs(ArrayList<Mur> orpheanMurs) {
		this.orpheanMurs = orpheanMurs;
	}

	@Override
	public void draw(DrawingContext dcx, boolean isFocused) {
		dcx.tui().diveWhere("niveau.draw");

		dcx.tui().debug("in" + (isFocused ? " focused" : "") + " Niveau " + this.toStringShort());
		dcx.tui().debug("drawing " + pieces.size() + " piece objects");
		for (Piece piece: pieces) {
			dcx.draw(piece);
		}
		dcx.tui().debug("drawing " + orpheanMurs.size() + " orpheanMurs objects");
		for (Mur orphean: orpheanMurs) {
			dcx.draw(orphean);
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
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("hauteur", String.valueOf(hauteur))
		    .fieldShortCollection("pieces", (ArrayList<BObject>) ((ArrayList<?>) pieces))
		    .fieldShortCollection("apparts", (ArrayList<BObject>) ((ArrayList<?>) apparts))
		    .fieldShortCollection("orpheanMurs", (ArrayList<BObject>) ((ArrayList<?>) orpheanMurs))
        	    .getValue();
	}

	public String serialize(Objects objects) {
		String out = String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(hauteur)
		) + "\n";

		if (!pieces.isEmpty()) {
			out += "PROP:pieces\n";
			String[] pieceIds = new String[pieces.size()];
			for (int i = 0; i < pieceIds.length; i++) {
				pieceIds[i] = String.valueOf(pieces.get(i).getId());
			}
			out += String.join(",", pieceIds) + "\n";
		}
		if (!apparts.isEmpty()) {
			out += "PROP:apparts\n";
			String[] appartIds = new String[apparts.size()];
			for (int i = 0; i < appartIds.length; i++) {
				appartIds[i] = String.valueOf(apparts.get(i).getId());
			}
			out += String.join(",", appartIds) + "\n";
		}
		if (!orpheanMurs.isEmpty()) {
			out += "PROP:orpheanMurs\n";
			String[] orpheanMurIds = new String[orpheanMurs.size()];
			for (int i = 0; i < orpheanMurIds.length; i++) {
				orpheanMurIds[i] = String.valueOf(orpheanMurs.get(i).getId());
			}
			out += String.join(",", orpheanMurIds) + "\n";
		}

		return out + "EOS:Entry";
	}
}
