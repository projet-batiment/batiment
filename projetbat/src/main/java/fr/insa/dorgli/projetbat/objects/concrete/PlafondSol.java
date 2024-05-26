package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import java.util.Collection;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class PlafondSol extends Drawable implements HasPrice {
	private ArrayList<RevetementPlafondSol> revetements;
	private ArrayList<OuvertureNiveaux> ouvertures;

	public PlafondSol() {
		revetements = new ArrayList<>();
		ouvertures = new ArrayList<>();
	}
	public PlafondSol(int id, ArrayList<RevetementPlafondSol> revetements, ArrayList<OuvertureNiveaux> ouvertures) {
		super(id);

		this.revetements = new ArrayList<>();
		this.ouvertures = new ArrayList<>();

		addChildren((BObject[]) revetements.toArray(BObject[]::new));
		addChildren((BObject[]) ouvertures.toArray(BObject[]::new));
	}

	public ArrayList<RevetementPlafondSol> getRevetements() {
		return revetements;
	}

	public void setRevetements(ArrayList<RevetementPlafondSol> revetements) {
		this.revetements = revetements;
	}

	public ArrayList<OuvertureNiveaux> getOuvertures() {
		return ouvertures;
	}

	public void setOuvertures(ArrayList<OuvertureNiveaux> ouvertures) {
		this.ouvertures = ouvertures;
	}

	@Override
	public double calculerPrix() {
		if (super.parents.size() != 1)
			return 0;

		double airePiece = ((Piece) parents.iterator().next()).aire();
		double prixPlafondSol = 0;

		/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
		for (RevetementPlafondSol each: revetements) {
			prixPlafondSol += each.calculerPrix();
		}
		for (RevetementPlafondSol each: revetements) {
			prixPlafondSol += each.calculerPrix();
		}
		for (OuvertureNiveaux each: ouvertures) {
			prixPlafondSol += each.calculerPrix();
		}

		return prixPlafondSol;
	}

	@Override
	public void draw(DrawingContext ctxt, DrawingContext.ObjectState ostate) {
		ctxt.tui().error("plafondSol.draw: cannot draw plafondSol");
	}

	@Override
	public boolean ready() {
		return true;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .fieldShortCollection("revetements", (Collection<FancyToStrings>) ((ArrayList<?>) revetements))
		    .fieldShortCollection("ouvertures", (Collection<FancyToStrings>) ((ArrayList<?>) ouvertures))
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId()
		);

		if (!revetements.isEmpty()) {
			serializer.innerProp("revetements");
			for (RevetementPlafondSol r: revetements)
				r.serialize(serializer);
			serializer.eos();
		}
		if (!ouvertures.isEmpty()) {
			serializer.innerProp("ouvertures");
			for (OuvertureNiveaux r: ouvertures)
				r.serialize(serializer);
			serializer.eos();
		}

		serializer.eoEntry();
	}

	public String serialize(Objects objects) {
		String out = String.valueOf(super.getId()) + "\n";

		if (!revetements.isEmpty()) {
			out += "PROP:RevetementPlafondSol\n";
			for (RevetementPlafondSol r: revetements)
				out += r.serialize(objects) + "\n";
			out += "EOS:RevetementPlafondSol\n";
		}
		if (!ouvertures.isEmpty()) {
			out += "PROP:OuvertureNiveaux\n";
			for (OuvertureNiveaux o: ouvertures)
				out += o.serialize(objects) + "\n";
			out += "EOS:OuvertureNiveaux\n";
		}

		return out + "EOS:Entry";
	}

	@Override
	public void clearChildren() {
		ouvertures.clear();
		revetements.clear();
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case OuvertureNiveaux known -> ouvertures.add(known);
				case RevetementPlafondSol known -> revetements.add(known);

				default -> throw new IllegalArgumentException("Unknown children type for plafondSol: " + object.getClass().getSimpleName());
			}
			object.addParents(this);
		}
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case OuvertureNiveaux known -> ouvertures.remove(known);
				case RevetementPlafondSol known -> revetements.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for plafondSol: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
