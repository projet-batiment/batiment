package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToString;
import fr.insa.dorgli.projetbat.ToStringShort;
import java.util.ArrayList;

public class PlafondSol implements ToString, ToStringShort {
	private ArrayList<RevetementPlafondSol> revetements;
	private ArrayList<OuvertureNiveaux> ouvertures;

	public PlafondSol(ArrayList<RevetementPlafondSol> revetements, ArrayList<OuvertureNiveaux> ouvertures) {
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

	public String toString() {
		return toString(0);
	}

	public String toString(int depth) {
		String pfx = "";
		for (int i = 0; i <= depth; i++) {
			pfx += "  ";
		}
		int nextDepth = depth + 1;

		String revetementsOut = "[ ";
		for (RevetementPlafondSol each : revetements) {
			revetementsOut += each.toStringShort() + ", ";
		}
		revetementsOut += "]";

		String ouverturesOut = "[ ";
		for (OuvertureNiveaux each : ouvertures) {
			ouverturesOut += each.toStringShort() + ", ";
		}
		ouverturesOut += "]";

		return "PlafondSol {\n"
				+ pfx + "revetements: " + revetementsOut + ",\n"
				+ pfx + "ouvertures: " + ouverturesOut + ",\n"
				+ "}";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfPlafondSol(this);

		String out = String.valueOf(id) + "\n";

		if (!revetements.isEmpty()) {
			out += "PROP:revetements\n";
			for (RevetementPlafondSol r: revetements)
				out += r.serialize(objects) + "\n";
			out += "EOS:revetements\n";
		}
		if (!ouvertures.isEmpty()) {
			out += "PROP:ouvertures\n";
			for (OuvertureNiveaux o: ouvertures)
				out += o.serialize(objects) + "\n";
			out += "EOS:ouvertures\n";
		}

		return out + "EOS:Entry";
	}
}
