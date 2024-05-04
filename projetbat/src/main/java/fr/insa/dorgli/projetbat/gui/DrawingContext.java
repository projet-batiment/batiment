package fr.insa.dorgli.projetbat.gui;

import fr.insa.dorgli.projetbat.Config;
import fr.insa.dorgli.projetbat.TUI;
import java.util.HashSet;
import javafx.scene.paint.Color;

public class DrawingContext {
	private Config config;
	private CanvasContainer cc;

	private HashSet<Drawable> drawnObjects;
	private Drawable focusedObject;

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
		if (focusedObject == null) {
			// TODO!!! amnesic debug
			config.tui.debug("no focusedObjects");
		} else {
			config.tui.begin();

			// TODO!! toStringShort
			config.tui.debug("drawing from focused " + focusedObject.toString());
			draw(focusedObject);

			config.tui.ended();
		}

		config.tui.popWhere();
	}

	public void draw(Drawable object) {
		if (drawnObjects.contains(object)) {
			// TODO!! amnesic debug
			// TODO!! toStringShort
			config.tui.debug("DrawingContext/draw: skipping already drawn object " + object.toString());
		} else {
			// TODO!! toStringShort
			config.tui.debug("DrawingContext/draw: drawing object " + object.toString());
			drawnObjects.add(object);
			object.draw(this);
		}
	}

	///// drawing stuff
	public void drawPoint(double x, double y) {
		cc.drawPoint(x, y);
	}

	public void drawLine(double x1, double y1, double x2, double y2, double width, Color color) {
		cc.drawLine(x1, y1, x2, y2, width, color);
	}

	///// getters & setters

	public Drawable getFocusedObject() {
		return focusedObject;
	}

	public void setFocusedObject(Drawable focusedObject) {
		this.focusedObject = focusedObject;
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