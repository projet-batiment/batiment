package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class NameDescEditor extends Editor {
	public NameDescEditor(Config config, String tabName, NameDesc nameDesc) {
		super(config, tabName);

		TextField nom = new TextField();
		TextArea description = new TextArea();

		HBox.setHgrow(nom, Priority.ALWAYS);
		HBox.setHgrow(description, Priority.ALWAYS);
		description.setWrapText(true);

		super.prependResetFunction(() -> {
			nom.setText(nameDesc.getNom());
			description.setText(nameDesc.getDescription());
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			nameDesc.setNom(nom.getText());
			nameDesc.setDescription(description.getText());
		});

		super.prependInitFunction((Pane pane) -> {
			pane.widthProperty().addListener((Observable eh) -> {
				final double maxWidth = 380;
				nom.setMaxWidth(maxWidth);
				description.setMaxWidth(maxWidth);
			});

			pane.getChildren().addAll(
				new HBox(
					new Label("Nom :"),
					nom
				),

				new VBox(
					new Label("Description :"),
					description
				)
			);
		});
	}
}
