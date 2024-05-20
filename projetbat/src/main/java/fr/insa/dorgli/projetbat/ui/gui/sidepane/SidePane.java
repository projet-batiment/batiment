package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public abstract class SidePane extends TabPane {
	public void addTab(Tab newTab) {
		super.getTabs().add(newTab);
	}

	public abstract void update();

	public SidePane() {
		super.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
}
