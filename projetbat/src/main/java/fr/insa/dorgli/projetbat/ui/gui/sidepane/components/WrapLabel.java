package fr.insa.dorgli.projetbat.ui.gui.sidepane.components;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class WrapLabel extends Label {
	public WrapLabel(String string) {
		super(string);
		super.setWrapText(true);
		super.setTextAlignment(TextAlignment.JUSTIFY);
	}
}
