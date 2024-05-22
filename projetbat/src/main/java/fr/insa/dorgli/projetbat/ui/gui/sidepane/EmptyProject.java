package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EmptyProject extends SmartTab {
	public EmptyProject(Config config) {
		super(config, "(Project vide)");

		super.prependInitFunction((Pane pane) -> 
			pane.getChildren().addAll(
				new Label("Ce projet ne contient aucun élément !"),
				new Label("Vous pouvez commencer par créer un bâtiment.")
			)
		);

		super.initialize();
	}
}
