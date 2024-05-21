package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public abstract class Editor extends SmartTab {
	private final Button save;
	private final Button cancel;

	public Editor(Config config, String typeName) {
		super(config, "Éditeur: " + typeName);

		save = new Button("Valider");
		save.setOnAction((ActionEvent event) -> {
			save(event);
		});

		cancel = new Button("Annuler");
		cancel.setOnAction((ActionEvent t) -> {
			reset();
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					save,
					cancel
				)
			)
		);

	}
}