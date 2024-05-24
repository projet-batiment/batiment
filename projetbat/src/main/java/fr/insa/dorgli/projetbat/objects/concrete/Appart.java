package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Appart extends BObject implements HasInnerPrice, NameDesc {
	private String nom;
	private String description;
	private ArrayList<Piece> pieces;
	private TypeAppart typeAppart;

	public Appart() {
		nom = "Nouvel appartement";
		description = new String();
		pieces = new ArrayList<>();
	}
	public Appart(int id, String nom, String description, ArrayList<Piece> pieces, TypeAppart typeAppart) {
		super(id);
		this.nom = nom;
		this.description = description;

		setTypeAppart(typeAppart);

		this.pieces = new ArrayList<>();
		addChildren((BObject[]) pieces.toArray(BObject[]::new));
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public List<Piece> getPieces() {
		return Collections.unmodifiableList(pieces);
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public TypeAppart getTypeAppart() {
		return typeAppart;
	}

	public final void setTypeAppart(TypeAppart typeAppart) {
		this.typeAppart = typeAppart;
		typeAppart.addParents(this);
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
		    .field("typeAppart", typeAppart)
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

	@Override
	public double calculerPrix() {
		double prixAppart = 0;

		for (Piece eachPiece: pieces){
			prixAppart += eachPiece.calculerPrix();
		}
		return prixAppart;
	}

	@Override
	public void calculerPrix(Devis.DevisCalculator calculator) {
		for (Piece eachPiece: pieces){
			calculator.addObject(eachPiece);
		}
	}

	@Override
	public void clearChildren() {
		pieces.clear();
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Piece known -> pieces.add(known);

				default -> throw new IllegalArgumentException("Unknown children type for appart: " + object.getClass().getSimpleName());
			}
			object.addParents(this);
		}
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Piece known -> pieces.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for appart: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
