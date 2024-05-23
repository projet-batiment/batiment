package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.layout.Pane;

public class Empty extends SmartTab {
	public Empty(Config config) {
		super(config, "(Vide)");

		super.prependInitFunction((Pane pane) -> 
			pane.getChildren().add(new WrapLabel("Aucun objet n'est sélectionné"))
		);

		super.initialize();
	}
}