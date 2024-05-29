package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
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
	private ArrayList<Drawable> orphans;

	public Niveau() {
		nom = "Nouveau niveau";
		description = new String();
		pieces = new ArrayList<>();
		apparts = new ArrayList<>();
		orphans = new ArrayList<>();
	}
	public Niveau(int id, String nom, String description, double hauteur, ArrayList<Piece> pieces, ArrayList<Appart> apparts, ArrayList<Drawable> orphans) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;

		this.pieces = new ArrayList<>();
		this.apparts = new ArrayList<>();
		this.orphans = new ArrayList<>();
		addChildren((BObject[]) pieces.toArray(BObject[]::new));
		addChildren((BObject[]) apparts.toArray(BObject[]::new));
		addChildren((BObject[]) orphans.toArray(BObject[]::new));
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

	public List<Drawable> getOrphans() {
		return Collections.unmodifiableList(orphans);
	}

	public void setOrphans(ArrayList<Drawable> orphans) {
		this.orphans = orphans;
	}

	@Override
	public void drawRoot(DrawingContext dcx) {
		dcx.tui().diveWhere("niveau.draw");

		dcx.tui().debug("in Niveau " + this.toStringShort());
		dcx.tui().debug("drawing " + pieces.size() + " piece objects");
		for (Piece piece: pieces) {
			dcx.draw(piece);
		}
		dcx.tui().debug("drawing " + orphans.size() + " orphanMurs objects");
		for (Drawable orphan: orphans) {
			dcx.draw(orphan);
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

		for (Drawable each: orphans){
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
		    .fieldShortCollection("orphans", (Collection<FancyToStrings>) ((ArrayList<?>) orphans))
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId(),
		    nom,
		    description,
		    hauteur
		);

		if (!apparts.isEmpty()) {
			serializer.prop("apparts");
			serializer.csv(apparts.stream().map(each -> (int) each.getId()));
		}
		if (!pieces.isEmpty()) {
			serializer.prop("pieces");
			serializer.csv(pieces.stream().map(each -> (int) each.getId()));
		}
		if (!orphans.isEmpty()) {
			serializer.prop("orphans");
			serializer.csv(orphans.stream().map(each -> (int) each.getId()));
		}

		serializer.eoEntry();
	}

	@Override
	public void clearChildren() {
		apparts.clear();
		pieces.clear();
		orphans.clear();
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case Piece known -> pieces.add(known);
				case Appart known -> apparts.add(known);
				case Drawable known -> orphans.add(known);

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
				case Drawable known -> orphans.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for niveau: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
