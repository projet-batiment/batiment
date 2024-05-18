package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Tab;

public abstract class ParentSidePane extends SidePane {
	protected Tab childrenTab;

	public ParentSidePane() {
		childrenTab = new Tab("Objets subordonnés");
		super.getTabs().add(childrenTab);
	}
}
