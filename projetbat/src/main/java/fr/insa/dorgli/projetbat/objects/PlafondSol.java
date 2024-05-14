package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class PlafondSol extends HasPrice {
	private ArrayList<RevetementPlafondSol> revetements;
	private ArrayList<OuvertureNiveaux> ouvertures;

	public PlafondSol(int id, ArrayList<RevetementPlafondSol> revetements, ArrayList<OuvertureNiveaux> ouvertures) {
		super(id);
		this.revetements = revetements;
		this.ouvertures = ouvertures;
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

	public double calculerPrix(double airePiece) {
		double prixPlafondSol = 0;

		/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
		for (RevetementPlafondSol each: revetements) {
			prixPlafondSol += each.calculerPrix(airePiece);
		}
		for (RevetementPlafondSol each: revetements) {
			prixPlafondSol += each.calculerPrix(airePiece);
		}
		for (OuvertureNiveaux each: ouvertures) {
			prixPlafondSol += each.getTypeOuverture().getPrixOuverture();
		}

		return prixPlafondSol;
	}

	@Override
	public double calculerPrix() {
		// TODO: besoin de la surface !
		return 0;
	}

	@Override
	public void draw(DrawingContext ctxt, boolean isFocused) {
		ctxt.tui().error("plafondSol.draw: cannot draw plafondSol");
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .fieldShortCollection("revetements", (ArrayList<BObject>) ((ArrayList<?>) revetements))
		    .fieldShortCollection("ouvertures", (ArrayList<BObject>) ((ArrayList<?>) ouvertures))
        	    .getValue();
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
}
