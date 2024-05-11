package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.ToString;
import fr.insa.dorgli.projetbat.ToStringShort;
import fr.insa.dorgli.projetbat.gui.Drawable;
import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Mur implements ToString, ToStringShort, Drawable {
	private Point pointDebut;
	private Point pointFin;
	private double hauteur;
	private TypeMur typeMur;
	private ArrayList<RevetementMur> revetements1;
	private ArrayList<RevetementMur> revetements2;
	private ArrayList<OuvertureMur> ouvertures;

	public Mur(Point pointDebut, Point pointFin, double hauteur, TypeMur typeMur, ArrayList<RevetementMur> revetements1,
			ArrayList<RevetementMur> revetements2, ArrayList<OuvertureMur> ouvertures) throws IllegalArgumentException {

		if (pointDebut.getNiveauId() != pointFin.getNiveauId())
			throw new IllegalArgumentException("Un mur doit avoir des points sur le même niveau! '"
					+ pointDebut.getNiveauId() + "' != '" + pointFin.getNiveauId() + "'");

		this.pointDebut = pointDebut;
		this.pointFin = pointFin;
		this.hauteur = hauteur;
		this.typeMur = typeMur;
		this.revetements1 = revetements1;
		this.revetements2 = revetements2;
		this.ouvertures = ouvertures;
	}

	public Point getPointDebut() {
		return this.pointDebut;
	}

	public void setPointDebut(Point pointDebut) {
		this.pointDebut = pointDebut;
	}

	public Point getPointFin() {
		return this.pointFin;
	}

	public void setPointFin(Point pointFin) {
		this.pointFin = pointFin;
	}

	public double getHauteur() {
		return this.hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public TypeMur getTypeMur() {
		return this.typeMur;
	}

	public void setTypeMur(TypeMur typeMur) {
		this.typeMur = typeMur;
	}

	public ArrayList<RevetementMur> getRevetements1() {
		return this.revetements1;
	}

	public ArrayList<RevetementMur> getRevetements2() {
		return this.revetements2;
	}

	public ArrayList<OuvertureMur> getOuvertures() {
		return this.ouvertures;
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

		String revetements1Out = "[ ";
		for (RevetementMur each : revetements1) {
			revetements1Out += each.toStringShort() + ", ";
		}
		revetements1Out += "]";

		String revetements2Out = "[ ";
		for (RevetementMur each : revetements2) {
			revetements2Out += each.toStringShort() + ", ";
		}
		revetements2Out += "]";

		String ouverturesOut = "[ ";
		for (OuvertureMur each : ouvertures) {
			ouverturesOut += each.toStringShort() + ", ";
		}
		ouverturesOut += "]";

		return "Mur {\n"
				+ pfx + "pointDebut: " + pointDebut + ",\n"
				+ pfx + "pointFin: " + pointFin + ",\n"
				+ pfx + "hauteur: " + hauteur + ",\n"
				+ pfx + "revetements1: " + revetements1Out + ",\n"
				+ pfx + "revetements2: " + revetements2Out + ",\n"
				+ pfx + "ouvertures: " + ouverturesOut + ",\n"
				+ "}";
	}

	// fonction temporaire si l'on passe un jour aux outils géométriques de java.awt...
	// utile pour calculer les aires du mur et de ses revetments
	private double longueur() {
		double dcx = pointDebut.getX() - pointFin.getX();
		double dy = pointFin.getY() - pointDebut.getY();
		return Math.sqrt(Math.pow(dcx, 2) + Math.pow(dy, 2));
	}

	public double aire() {
		return longueur() * this.hauteur;
	}

	public double calculerPrix() {
		double prixMur = 0;
		prixMur = typeMur.getPrixUnitaire() * aire();

		double longueur = longueur();
		/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
		for (RevetementMur r: this.revetements1){
			prixMur += r.calculerPrix(longueur, hauteur);
		}
		for (RevetementMur r: this.revetements2){
			prixMur += r.calculerPrix(longueur, hauteur);
		}
		for (OuvertureMur o: this.ouvertures){
			prixMur += o.getTypeOuverture().getPrixOuverture();
		}
		return prixMur;
	}

	public void draw(DrawingContext dcx) {
		// TODO: amnesic debug dive
		dcx.tui().debug("Mur: drawing Mur " + this.toStringShort());
		dcx.drawLine(pointDebut.getX(), pointDebut.getY(), pointFin.getX(), pointFin.getY(), 10, Color.DIMGRAY);
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfMur(this);
		return String.join(",", id, pointDebut, pointFin, hauteur, typeMur);
	}
}
