package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
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
			reset();
		});

		cancel = new Button("Annuler");
		cancel.setOnAction((ActionEvent t) -> {
			reset();
		});

		WrapLabel parents = new WrapLabel();

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new Separator(),
				new HBox(
					save,
					cancel
				)
			)
		);

	}
}
