package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Label;

public class Empty extends SidePane {
	@Override
	public void update() { }

	public Empty() {
		final String text = "Aucun objet n'est sélectionné";

		final Label editorLabel;
		editorLabel = new Label(text);
		super.editorTab.setContent(editorLabel);
	}
}