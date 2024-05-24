package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import java.util.Collection;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.Collections;
import java.util.List;

public class Niveau extends DrawableRoot implements NameDesc, HasInnerPrice {
	private String nom;
	private String description;
	private double hauteur;
	private ArrayList<Piece> pieces;
	private ArrayList<Appart> apparts;
	private ArrayList<Drawable> orpheans;

	public Niveau() {
		nom = "Nouveau niveau";
		description = new String();
		pieces = new ArrayList<>();
		apparts = new ArrayList<>();
		orpheans = new ArrayList<>();
	}
	public Niveau(int id, String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts, ArrayList<Drawable> orpheans) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;

		this.pieces = new ArrayList<>();
		this.apparts = new ArrayList<>();
		this.orpheans = new ArrayList<>();
		addChildren((BObject[]) pieces.toArray(BObject[]::new));
		addChildren((BObject[]) apparts.toArray(BObject[]::new));
		addChildren((BObject[]) orpheans.toArray(BObject[]::new));
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

	public double getHauteur() {
		return hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public List<Piece> getPieces() {
		return Collections.unmodifiableList(pieces);
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public List<Appart> getApparts() {
		return Collections.unmodifiableList(apparts);
	}

	public void setApparts(ArrayList<Appart> apparts) {
		this.apparts = apparts;
	}

	public List<Drawable> getOrpheans() {
		return Collections.unmodifiableList(orpheans);
	}

	public void setOrpheans(ArrayList<Drawable> orpheans) {
		this.orpheans = orpheans;
	}

	@Override
	public void drawRoot(DrawingContext dcx) {
		dcx.tui().diveWhere("niveau.draw");

		dcx.tui().debug("in Niveau " + this.toStringShort());
		dcx.tui().debug("drawing " + pieces.size() + " piece objects");
		for (Piece piece: pieces) {
			dcx.draw(piece);
		}
		dcx.tui().debug("drawing " + orpheans.size() + " orpheanMurs objects");
		for (Drawable orphean: orpheans) {
			dcx.draw(orphean);
		}

		dcx.tui().popWhere();
	}

	@Override
	public double calculerPrix() {
		double prixNiveau = 0;

		for (Piece eachPiece: pieces){
			prixNiveau += eachPiece.calculerPrix();
		}
		return prixNiveau;
	}

	@Override
	public void calculerPrix(Devis.DevisCalculator calc) {
		for (Piece eachPiece: pieces){
			calc.addObject(eachPiece);
		}

		for (Drawable each: orpheans){
			if (each instanceof HasPrice priced)
				calc.addObject(priced);
		}
	}

	@Override
	public boolean ready() {
		return true;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("hauteur", String.valueOf(hauteur))
		    .fieldShortCollection("pieces", (Collection<FancyToStrings>) ((ArrayList<?>) pieces))
		    .fieldShortCollection("apparts", (Collection<FancyToStrings>) ((ArrayList<?>) apparts))
		    .fieldShortCollection("orpheans", (Collection<FancyToStrings>) ((ArrayList<?>) orpheans))
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
		if (!orpheans.isEmpty()) {
			out += "PROP:orpheans\n";
			String[] orpheanIds = new String[orpheans.size()];
			for (int i = 0; i < orpheanIds.length; i++) {
				orpheanIds[i] = String.valueOf(orpheans.get(i).getId());
			}
			out += String.join(",", orpheanIds) + "\n";
		}

		return out + "EOS:Entry";
	}

	@Override
	public void clearChildren() {
		apparts.clear();
		pieces.clear();
		orpheans.clear();
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Piece known -> pieces.add(known);
				case Appart known -> apparts.add(known);
				case Drawable known -> orpheans.add(known);

				default -> throw new IllegalArgumentException("Unknown children type for niveau: " + object.getClass().getSimpleName());
			}
			object.addParents(this);
		}
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Piece known -> pieces.remove(known);
				case Appart known -> apparts.remove(known);
				case Drawable known -> orpheans.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for niveau: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
