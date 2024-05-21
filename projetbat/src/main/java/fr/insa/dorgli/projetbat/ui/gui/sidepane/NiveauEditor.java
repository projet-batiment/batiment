package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class NiveauEditor extends NameDescEditor {
	public NiveauEditor(Config config, Niveau niveau) {
		super(config, "Niveau", niveau);

		TextField hauteur = new TextField();

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double hauteur_value = Double.parseDouble(hauteur.getText());

				niveau.setHauteur(hauteur_value);
			} catch (NumberFormatException e) {
				config.tui.error("niveauEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			hauteur.setText(String.valueOf(niveau.getHauteur()));
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new Label("Hauteur :"),
					hauteur
				),
				new Label("TODO!! listes")
			)
		);

		super.initialize();
	}
}
