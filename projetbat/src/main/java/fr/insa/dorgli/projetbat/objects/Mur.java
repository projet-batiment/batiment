package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.StructuredToString;
import fr.insa.dorgli.projetbat.gui.DrawingContext;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Mur extends HasPrice {
	private Point pointDebut;
	private Point pointFin;
	private double hauteur;
	private TypeMur typeMur;
	private ArrayList<RevetementMur> revetements1;
	private ArrayList<RevetementMur> revetements2;
	private ArrayList<OuvertureMur> ouvertures;

	public Mur(int id, Point pointDebut, Point pointFin, double hauteur, TypeMur typeMur, ArrayList<RevetementMur> revetements1,
			ArrayList<RevetementMur> revetements2, ArrayList<OuvertureMur> ouvertures) throws IllegalArgumentException {
		super(id);

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

	public void draw(DrawingContext dcx, boolean isFocused) {
		// TODO: amnesic debug dive
		dcx.tui().debug("Mur: drawing " + (isFocused ? "focused " : "") + "Mur " + this.toStringShort());
		if (isFocused) {
			dcx.drawLine(this, pointDebut.getX(), pointDebut.getY(), pointFin.getX(), pointFin.getY(), 15, Color.INDIANRED);
		} else {
			dcx.drawLine(this, pointDebut.getX(), pointDebut.getY(), pointFin.getX(), pointFin.getY(), 10, Color.DIMGRAY);
		}
	}

	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("pointDebut", pointDebut.toString(depth + 1))
		    .field("pointFin", pointFin.toString(depth + 1))
		    .field("hauteur", ""+hauteur)
		    .field("revetements1", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) revetements1)) )
		    .field("revetements2", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) revetements2)))
		    .field("ouvertures", super.toStringArrayList( (ArrayList<BObject>) ((ArrayList<?>) ouvertures)))
		    .getValue();
	}
}
