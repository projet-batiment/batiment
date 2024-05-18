package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public abstract class SidePane extends TabPane {
	protected Tab editorTab;
	protected Tab childrenTab;

	public SidePane() {
		editorTab = new Tab("Éditeur");
		childrenTab = new Tab("Objets subordonnés");

		super.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		super.getTabs().addAll(
		    editorTab,
		    childrenTab
		);
	}
}
