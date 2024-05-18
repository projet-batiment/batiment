package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public abstract class SidePane extends TabPane {
	protected Tab editorTab;

	public SidePane() {
		editorTab = new Tab("Éditeur");
		super.getTabs().add(editorTab);

		super.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
}
