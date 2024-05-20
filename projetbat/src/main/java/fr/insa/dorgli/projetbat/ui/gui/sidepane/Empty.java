package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class Empty extends SidePane {
	@Override
	public void update() { }

	public Empty() {
		super.addTab(new Tab("(Vide)", new Label("Aucun objet n'est sélectionné")));
	}
}