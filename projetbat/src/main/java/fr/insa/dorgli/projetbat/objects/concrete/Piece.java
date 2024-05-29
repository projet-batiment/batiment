package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.HasInnerPrice;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.ui.gui.DrawingContext;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import java.util.ArrayList;
import java.util.Collection;
import fr.insa.dorgli.projetbat.utils.StructuredToString;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;

public class Piece extends DrawablePath implements HasInnerPrice, NameDesc {
	private String nom;
	private String description;
	private ArrayList<Point> points;
	private ArrayList<Mur> murs;
	private PlafondSol plafond;
	private PlafondSol sol;

	public Piece() {
		nom = "Nouvelle pièce";
		description = new String();
		points = new ArrayList<>();
		murs = new ArrayList<>();
	}
	public Piece(int id, String nom, String description, ArrayList<Point> points, ArrayList<Mur> murs, PlafondSol plafond, PlafondSol sol) {
		super(id);
		this.nom = nom;
		this.description = description;

		setSol(sol);
		setPlafond(plafond);

		this.murs = new ArrayList<>();
		this.points = new ArrayList<>();
		addChildren((BObject[]) murs.toArray(BObject[]::new));
		addChildren((BObject[]) points.toArray(BObject[]::new));
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

	public List<Mur> getMurs() {
		return Collections.unmodifiableList(murs);
	}

	public void setMurs(ArrayList<Mur> murs) {
		this.murs = murs;
	}

	public List<Point> getPoints() {
		return Collections.unmodifiableList(points);
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public PlafondSol getPlafond() {
		return plafond;
	}

	public final void setPlafond(PlafondSol plafond) {
		this.plafond = plafond;
		plafond.addParents(this);
	}

	public PlafondSol getSol() {
		return sol;
	}

	public final void setSol(PlafondSol sol) {
		this.sol = sol;
		sol.addParents(this);
	}

	public double aire() {
		double out = 0;
		/// TODO!!! implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
		for (int i = 0; i < points.size(); i++) {
			Point current = points.get(i);
			Point next = points.get((i + 1) % points.size());
			out += current.getX() * next.getY() - current.getY() * next.getX();
		}
		return 0.5 * Math.abs(out);
	}

	@Override
	public double calculerPrix() {
		double prixPiece = 0;
		double airePiece = aire();

		for (Mur eachMur: murs) {
			prixPiece += eachMur.calculerPrix();
		}

		if (plafond != null)
			prixPiece += plafond.calculerPrix();
		if (sol != null)
			prixPiece += sol.calculerPrix();

		return prixPiece;
	}

	@Override
	public void calculerPrix(Devis.DevisCalculator calc) {
		for (Mur eachMur: murs) {
			calc.addObject(eachMur);
		}

		calc.addObject(plafond);
		calc.addObject(sol);
	}

	@Override
	public void draw(DrawingContext dcx, DrawingContext.ObjectState ostate) {
		dcx.tui().diveWhere("piece.draw");

		// dessiner la pièce elle même
		Color color = Color.NAVAJOWHITE;
		switch (ostate) {
			case SELECTED -> color = Color.web("8AA5E4");
			case MEMBER -> color = Color.web("ccb18a");
		}
		dcx.drawPolygon(this, points.toArray(Point[]::new), color);

		// dessiner les murs
		dcx.tui().debug("drawing " + murs.size() + " murs objects");
		for (int i = 0; i < murs.size(); i++) {
			dcx.draw(murs.get(i));
		}

		dcx.tui().popWhere();
	}

	@Override
	public boolean ready() {
		return (
			plafond != null
			&& sol != null
		);
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .fieldShortCollection("points", (Collection<FancyToStrings>) ((ArrayList<?>) points))
		    .fieldShortCollection("murs", (Collection<FancyToStrings>) ((ArrayList<?>) murs))
		    .field("plafond", plafond.toString(depth + 1))
		    .field("sol", sol.toString(depth + 1))
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId(),
		    nom,
		    description
		);

		if (!murs.isEmpty()) {
			serializer.prop("murs");
			serializer.csv(murs.stream().map(each -> (int) each.getId()));
		}
		if (!points.isEmpty()) {
			serializer.prop("points");
			serializer.csv(points.stream().map(each -> (int) each.getId()));
		}
		if (plafond != null) {
			serializer.innerProp("plafond");
			plafond.serialize(serializer);
			serializer.eos();
		}
		if (sol != null) {
			serializer.innerProp("sol");
			sol.serialize(serializer);
			serializer.eos();
		}

		serializer.eoEntry();
	}

	@Override
	public void clearChildren() {
		points.clear();
		murs.clear();
		plafond = null;
		sol = null;
	}

	@Override
	public final void addChildren(BObject... objects) {
		for (BObject object: objects) {
			if (object.getParents().contains(this))
				continue;

			switch (object) {
				case PlafondSol known -> throw new IllegalAccessError("Shoudn't call Piece.addChildren with object PlafondSol");
				case Mur known -> {
					murs.add(known);
					addChildren(known.getPointDebut(), known.getPointFin());
				}
				case Point known -> points.add(known);

				default -> throw new IllegalArgumentException("Unknown children type for piece: " + object.getClass().getSimpleName());
			}
			object.addParents(this);
		}
	}

	@Override
	public void removeChildren(BObject... objects) {
		for (BObject object: objects) {
			switch (object) {
				case PlafondSol known -> throw new IllegalAccessError("Shoudn't call Piece.removeChildren with object PlafondSol");
				case Mur known -> {
					murs.remove(known);
					removeChildren(known.getPointDebut(), known.getPointFin());
				}
				case Point known -> points.remove(known);

				default -> throw new IllegalArgumentException("Unknown children type for piece: " + object.getClass().getSimpleName());
			}
			object.removeParents(this);
		}
	}
}
