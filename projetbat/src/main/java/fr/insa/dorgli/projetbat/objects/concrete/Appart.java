package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.ArrayList;
import java.util.Collection;

public class Appart extends BObject implements HasPrice, NameDesc {
	private String nom;
	private String description;
	private ArrayList<Piece> pieces;
	private TypeAppart typeAppart;

	public Appart() {
		nom = new String();
		description = new String();
		pieces = new ArrayList<>();
	}
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

	@Override
	public boolean ready() {
		return (
			typeAppart != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .fieldShortCollection("pieces", (Collection<FancyToStrings>) ((ArrayList<?>) pieces))
		    .field("typeAppart", typeAppart.toString(depth + 1))
		    .getValue();
	}

	public String serialize(Objects objects) {
		String out = String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(typeAppart.getId())
		) + "\n";

		if (!pieces.isEmpty()) {
			out += "PROP:pieces\n";
			String[] pieceIds = new String[pieces.size()];
			for (int i = 0; i < pieceIds.length; i++) {
				pieceIds[i] = String.valueOf(pieces.get(i).getId());
			}
			out += String.join(",", pieceIds) + "\n";
		}

		return out + "EOS:Entry";
	}

	public double calculerPrix() {
		double prixAppart = 0;

		for (Piece eachPiece: pieces){
			prixAppart += eachPiece.calculerPrix();
		}
		return prixAppart;
	}
}
