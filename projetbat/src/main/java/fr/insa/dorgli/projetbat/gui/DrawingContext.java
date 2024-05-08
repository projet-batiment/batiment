package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import fr.insa.dorgli.projetbat.TUI;
import java.awt.geom.Line2D;
import java.util.HashSet;
import javafx.scene.paint.Color;

public class DrawingContext {
	private Config config;
	private CanvasContainer cc;

	private HashSet<Drawable> drawnObjects;
	private Drawable rootObject; // l'objet principal lde la vue actuelle
	private Drawable selectedObject; // l'objet sélectionné avec un clic / menu

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
		if (rootObject == null) {
			// TODO!!! amnesic debug
			config.tui.debug("no rootObjects");
		} else {
			config.tui.begin();

			// TODO!! toStringShort
			config.tui.debug("drawing from root object " + rootObject.toString());
			draw(rootObject);

			config.tui.ended();
		}

		config.tui.popWhere();
	}

	///// launch an object drawer

	public void draw(Drawable object) {
		if (drawnObjects.contains(object)) {
			// TODO!! amnesic debug
			// TODO!! toStringShort
			config.tui.debug("DrawingContext/draw: skipping already drawn object " + object.toString());
		} else {
			// TODO!! toStringShort
			config.tui.debug("DrawingContext/draw: drawing object " + object.toString());
			drawnObjects.add(object);
			object.draw(this, object == selectedObject);
		}
	}

	///// actual drawing stuff

	public void drawPoint(Drawable linkedObject, double x, double y) {
		cc.drawPoint(x, y);
	}

	public void drawLine(Drawable linkedObject, double x1, double y1, double x2, double y2, double width, Color color) {
		cc.drawLine(linkedObject, x1, y1, x2, y2, width, color);
	}

	///// getters & setters

	public Drawable getRootObject() {
		return rootObject;
	}

	public void setRootObject(Drawable rootObject) {
		this.rootObject = rootObject;
	}

	public Drawable getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Drawable selectedObject) {
		this.selectedObject = selectedObject;
	}

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