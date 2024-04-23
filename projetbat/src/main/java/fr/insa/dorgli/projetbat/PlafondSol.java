package fr.insa.dorgli.projetbat;

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
}
