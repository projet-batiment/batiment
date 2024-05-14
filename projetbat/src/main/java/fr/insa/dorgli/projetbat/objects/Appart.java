package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.Deserialize;
import java.util.ArrayList;

public class Appart {
	private String nom;
	private String description;
	private ArrayList<Piece> pieces;
	private TypeAppart typeAppart;

	public Appart(String nom, String description, ArrayList<Piece> pieces, TypeAppart typeAppart) {
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

	@Override
	public String toString() {
		String piecesOut = "[ ";
		for (Piece piece: pieces) {
			piecesOut += piece.toStringShort() + ", ";
		}
		piecesOut += "]";
		return "Appart {\n  nom: '" + nom + "',\n  description: '" + description + "',\n  pieces: " + piecesOut + ",\n  typeAppart: " + typeAppart.toStringShort() + "\n}";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + nom + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfAppart(this);

		String out = String.join(",",
		    String.valueOf(id),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(objects.getIdOfTypeAppart(typeAppart))
		) + "\n";

		if (!pieces.isEmpty()) {
			out += "PROP:pieces\n";
			String[] pieceIds = new String[pieces.size()];
			for (int i = 0; i < pieceIds.length; i++) {
				pieceIds[i] = String.valueOf(objects.getIdOfPiece(pieces.get(i)));
			}
			out += String.join(",", pieceIds) + "\n";
		}

		return out + "EOS:Entry";
	}
}
