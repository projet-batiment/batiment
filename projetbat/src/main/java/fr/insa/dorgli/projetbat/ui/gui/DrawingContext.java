package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.objects.concrete.DrawablePath;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableLine;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.DrawablePoint;
import fr.insa.dorgli.projetbat.objects.concrete.Drawable;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.control.State;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableRoot;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javafx.scene.paint.Color;

public class DrawingContext {
	private Config config;
	private CanvasContainer cc;

	private HashSet<Drawable> drawnObjects;

	public enum ObjectState {
		NORMAL,
		SELECTED,
		MEMBER // the parent object is selected
	}

	public DrawingContext(Config config, CanvasContainer cc) {
		this.config = config;
		this.cc = cc;

		drawnObjects = new HashSet<>();
	}

	// clear the drawnObjects list
	public void reset() {
		drawnObjects.clear();
	}

	public void redraw() {
		config.tui.diveWhere("DrawingContext/redraw");

		reset();
		if (config.controller.state.getViewRootElement() == null) {
			config.tui.debug("no rootObjects");
		} else {
			config.tui.begin();

			config.tui.debug("drawing from root object " + config.controller.state.getViewRootElement().toString());
			DrawableRoot root = config.controller.state.getViewRootElement();
			config.controller.state.getViewRootElement().drawRoot(this);

			config.tui.ended();
		}

		State state = config.controller.state;
		if (state.getActionState() == State.ActionState.CREATE || state.getActionState() == State.ActionState.CREATE_SERIE) {
			if (state.getCreator().step == 1) {
				Point2D.Double mousePosition = cc.getMousePositionData();

				Mur created = (Mur) state.getCreator().object;
				cc.drawLine(created, new Line2D.Double(created.getPointDebut().getPoint(), mousePosition), 15, Color.web("CCB18A"));
				config.tui.debug("state == CREATE_MUR(1): manually drawing created mur " + created);
			}
		}

		config.tui.popWhere();
	}

	///// launch an object drawer

	public void draw(Drawable object) {
		// NOTE: ce qui est commenté ci-dessous permet de ne pas dessiner plusieurs fois un même objet
		// cependant, d'autres objets peuvent être affichés par dessus
		// nous n'avons pas eu le temps d'implémenter d'ordre d'importance de l'affichage
		// donc ce bloc reste commenté
//		if (drawnObjects.contains(object)) {
//			config.tui.debug("DrawingContext/draw: skipping already drawn object " + object.toString());
//		} else {
			ObjectState ostate;
			if (config.controller.state.isSelectedElement(object))
				ostate = ObjectState.SELECTED;
			else if (
			    config.controller.state.getTheSelectedElement() != config.controller.state.getViewRootElement()
			    && object.getParents().contains(config.controller.state.getTheSelectedElement())
			)
				ostate = ObjectState.MEMBER;
			else
				ostate = ObjectState.NORMAL;

			config.tui.debug("DrawingContext/draw: drawing " + ostate + " object " + object.toStringShort());
			drawnObjects.add(object);
			object.draw(this, ostate);
//		}
	}

	///// actual drawing stuff

	public void drawPoint(Point point, double radius, Color color, boolean important) {
		cc.drawPoint((DrawablePoint) point, point.getPoint(), radius, color, important);
	}

	public void drawLine(DrawableLine linkedObject, Point pointDebut, Point pointFin, double width, Color color) {
		cc.drawLine(linkedObject, new Line2D.Double(pointDebut.getPoint(), pointFin.getPoint()), width, color);
		draw(pointDebut);
		draw(pointFin);
	}

	public void drawPolygon(DrawablePath linkedObject, Point[] points, Color color) {
		// hypothèse pour malloc: il y a length doublons => newLength = length/2
		LinkedHashSet<Point2D.Double> geomPoints = new LinkedHashSet<>(points.length / 2);

		config.tui.debug("dcx/drawPolygon: length is " + points.length);
		String out = "[ ";

		// on supprime les doublons
		for (Point point : points) {
			if (point == null) {
				config.tui.error("dcx/drawPolygon: got a null point !!! ");
				continue;
			}
			geomPoints.add(new Point2D.Double(point.getX(), point.getY()));
			out += "(" + point.getX() + ":" + point.getY() + "), ";
		}
		config.tui.debug("dcx/drawPolygon: without duplicates: " + geomPoints.size() + out + " ]");

		cc.drawPolygon(linkedObject, geomPoints.toArray(Point2D.Double[]::new), color);
	}

	///// getters & setters

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public TUI tui() {
		return config.tui;
	}
}