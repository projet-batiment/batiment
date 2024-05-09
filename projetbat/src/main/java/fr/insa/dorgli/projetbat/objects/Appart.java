package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.StructuredToString;
import java.util.ArrayList;

public class Appart extends BObject {
	private String nom;
	private String description;
	private ArrayList<Piece> pieces;
	private TypeAppart typeAppart;

	public Appart(int id, String nom, String description, ArrayList<Piece> pieces, TypeAppart typeAppart) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.pieces = pieces;
		this.typeAppart = typeAppart;
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

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public TypeAppart getTypeAppart() {
		return typeAppart;
	}

	public void setTypeAppart(TypeAppart typeAppart) {
		this.typeAppart = typeAppart;
	}

	public double calculerPrix() {
		// TODO ??
		return 0;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
		    .field("pieces", super.toStringArrayList((ArrayList<BObject>) ((ArrayList<?>) pieces)))
		    .field("typeAppart", typeAppart.toString(depth + 1))
		    .getValue();
	}

//	@Override
//	public String toStringShort() {
//		return "Appart#" + super.getId();
//	}
}
