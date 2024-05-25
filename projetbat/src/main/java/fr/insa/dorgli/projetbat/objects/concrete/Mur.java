package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.objects.types.TypeMur;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;

public class Mur extends DrawableLine implements HasPrice {
	private Point pointDebut;
	private Point pointFin;
	private double hauteur;
	private TypeMur typeMur;
	private ArrayList<RevetementMur> revetements1;
	private ArrayList<RevetementMur> revetements2;
	private ArrayList<OuvertureMur> ouvertures;

	public Mur() {
		revetements1 = new ArrayList<>();
		revetements2 = new ArrayList<>();
		ouvertures = new ArrayList<>();
	}
	public Mur(int id, Point pointDebut, Point pointFin, double hauteur, TypeMur typeMur, ArrayList<RevetementMur> revetements1,
			ArrayList<RevetementMur> revetements2, ArrayList<OuvertureMur> ouvertures) throws IllegalArgumentException {
		super(id);

		if (pointDebut.getNiveau() != pointFin.getNiveau()) {
			throw new IllegalArgumentException("Un mur doit avoir des points sur le même niveau! '"
			    + pointDebut.getNiveau().toStringShort() + "' != '" + pointFin.getNiveau().toStringShort() + "'");
		}

		this.hauteur = hauteur;

		setPointDebut(pointDebut);
		setPointFin(pointFin);
		setTypeMur(typeMur);

		this.revetements1 = new ArrayList<>();
		for (RevetementMur each: revetements1)
			addRevetement1(each);

		this.revetements1 = new ArrayList<>();
		for (RevetementMur each: revetements1)
			addRevetement1(each);

		this.ouvertures = new ArrayList<>();
		for (OuvertureMur each: ouvertures)
			addOuverture(each);
	}

	public Point getPointDebut() {
		return pointDebut;
	}

	public final void setPointDebut(Point pointDebut) {
		if (this.pointDebut instanceof Point oldPoint)
			oldPoint.removeParents(this);

		pointDebut.addParents(this);
		this.pointDebut = pointDebut;
	}

	public Point getPointFin() {
		return pointFin;
	}

	public final void setPointFin(Point pointFin) {
		if (this.pointFin instanceof Point oldPoint)
			oldPoint.removeParents(this);

		pointFin.addParents(this);
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

	public final void setTypeMur(TypeMur typeMur) {
		this.typeMur = typeMur;
		typeMur.addParents(this);
	}

	public List<RevetementMur> getRevetements1() {
		return Collections.unmodifiableList(revetements1);
	}

	public final void addRevetement1(RevetementMur object) {
		revetements1.add(object);
		object.addParents(this);
	}

	public List<RevetementMur> getRevetements2() {
		return Collections.unmodifiableList(revetements2);
	}

	public void addRevetement2(RevetementMur object) {
		revetements2.add(object);
		object.addParents(this);
	}

	public List<OuvertureMur> getOuvertures() {
		return Collections.unmodifiableList(ouvertures);
	}

	public final void addOuverture(OuvertureMur object) {
		ouvertures.add(object);
		object.addParents(this);
	}

	private double longueur() {
		double dcx = pointDebut.getX() - pointFin.getX();
		double dy = pointFin.getY() - pointDebut.getY();
		return Math.sqrt(Math.pow(dcx, 2) + Math.pow(dy, 2));
	}

	public double aire() {
		return longueur() * this.hauteur;
	}

	@Override
	public double calculerPrix() {
		double prixMur = typeMur.getPrixUnitaire() * aire();

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

	@Override
	public void draw(DrawingContext dcx, DrawingContext.ObjectState ostate) {
		switch (ostate) {
			case NORMAL -> dcx.drawLine(this, pointDebut, pointFin, 10, Color.DIMGRAY);
			case SELECTED -> dcx.drawLine(this, pointDebut, pointFin, 15, Color.web("9c896c"));
			case MEMBER -> dcx.drawLine(this, pointDebut, pointFin, 10, Color.web("7f6f56"));
		}
	}

	@Override
	public boolean ready() {
		return (
			pointDebut != null
			&& pointFin != null
			&& typeMur != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .field("pointDebut", pointDebut.toString(depth + 1))
		    .field("pointFin", pointFin.toString(depth + 1))
		    .field("hauteur", String.valueOf(hauteur))
		    .fieldShortCollection("revetements1", (Collection<FancyToStrings>) ((ArrayList<?>) revetements1))
		    .fieldShortCollection("revetements2", (Collection<FancyToStrings>) ((ArrayList<?>) revetements2))
		    .fieldShortCollection("ouvertures", (Collection<FancyToStrings>) ((ArrayList<?>) ouvertures))
		    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
	}

	public String serialize(Objects objects) {
		String out = String.join(",",
		    String.valueOf(super.getId()),
		    String.valueOf(pointDebut.getId()),
		    String.valueOf(pointFin.getId()),
		    String.valueOf(hauteur),
		    String.valueOf(typeMur.getId())
		) + "\n";

		if (!revetements1.isEmpty()) {
			out += "PROP:RevetementMur:1\n";
			for (RevetementMur r: revetements1)
				out += r.serialize(objects) + "\n";
			out += "EOS:RevetementMur:1\n";
		}
		if (!revetements2.isEmpty()) {
			out += "PROP:RevetementMur:2\n";
			for (RevetementMur r: revetements2)
				out += r.serialize(objects) + "\n";
			out += "EOS:RevetementMur:2\n";
		}
		if (!ouvertures.isEmpty()) {
			out += "PROP:OuvertureMur\n";
			for (OuvertureMur o: ouvertures)
				out += o.serialize(objects) + "\n";
			out += "EOS:OuvertureMur\n";
		}

		return out + "EOS:Entry";
	}

	@Override
	public void clearChildren() {
		revetements1.clear();
		revetements2.clear();
		ouvertures.clear();
		pointDebut = null;
		pointFin = null;
	}

	@Override
	public final void addChildren(BObject... objects) {
		throw new IllegalAccessError("Shouldn't call Mur.addChildren()");
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case RevetementMur known -> {
					revetements1.remove(known);
					revetements2.remove(known);
				}
				case OuvertureMur known -> ouvertures.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for mur: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
