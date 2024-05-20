package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.objects.concrete.DrawablePath;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableLine;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.DrawablePoint;
import fr.insa.dorgli.projetbat.objects.concrete.Drawable;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.control.State;
import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.objects.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javafx.scene.paint.Color;

public class DrawingContext {
	private Config config;
	private CanvasContainer cc;

	private HashSet<Drawable> drawnObjects;
//	private Drawable rootObject; // l'objet principal lde la vue actuelle
//	private Drawable selectedObject; // l'objet sélectionné avec un clic / menu

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
		if (config.controller.state.viewRootElement == null) {
			// TODO!!! amnesic debug
			config.tui.debug("no rootObjects");
		} else {
			config.tui.begin();

			// TODO!! toStringShort
			config.tui.debug("drawing from root object " + config.controller.state.viewRootElement.toString());
			draw(config.controller.state.viewRootElement);

			config.tui.ended();
		}

		State state = config.controller.state;
		if (state.getActionState() == State.ActionState.CREATE_MUR) {
			if (state.getCreator().step == 1) {
				Point2D.Double mousePosition = cc.getMousePositionData();
				Point mousePositionObject = new Point();
				mousePositionObject.setX(mousePosition.getX());
				mousePositionObject.setY(mousePosition.getY());

				Mur placeholderMur = new Mur();
				placeholderMur.setPointDebut( ((Mur) state.getCreator().object).getPointDebut() );
				placeholderMur.setPointFin(mousePositionObject);

				placeholderMur.draw(this, true);
				config.tui.debug("state == CREATE_MUR(1): drawing placeholder mur " + placeholderMur);
			}
		}

		config.tui.popWhere();
	}

	///// launch an object drawer

	public void draw(Drawable object) {
//		if (drawnObjects.contains(object)) {
//			 TODO!! amnesic debug
//			 TODO!! toStringShort
//			config.tui.debug("DrawingContext/draw: skipping already drawn object " + object.toString());
//		} else {
			config.tui.debug("DrawingContext/draw: drawing object " + object.toStringShort());
			drawnObjects.add(object);
			object.draw(this, config.controller.state.isSelectedElement(object));
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