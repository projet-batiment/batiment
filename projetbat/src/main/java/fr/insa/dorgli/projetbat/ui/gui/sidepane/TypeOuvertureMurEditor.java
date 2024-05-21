package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeOuvertureMur;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class TypeOuvertureMurEditor extends NameDescEditor {
	public TypeOuvertureMurEditor(Config config, TypeOuvertureMur typeOuvertureMur) {
		super(config, "TypeOuvertureMur", typeOuvertureMur);

		TextField hauteur = new TextField();
		TextField largeur = new TextField();
		TextField prixOuverture = new TextField();

		HBox.setHgrow(hauteur, Priority.ALWAYS);
		HBox.setHgrow(largeur, Priority.ALWAYS);
		HBox.setHgrow(prixOuverture, Priority.ALWAYS);

		super.prependResetFunction(() -> {
			hauteur.setText(String.valueOf(typeOuvertureMur.getHauteur()));
			largeur.setText(String.valueOf(typeOuvertureMur.getLargeur()));
			prixOuverture.setText(String.valueOf(typeOuvertureMur.getPrixOuverture()));
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double hauteur_value = Double.parseDouble(hauteur.getText());
				double largeur_value = Double.parseDouble(largeur.getText());
				double prixOuverture_value = Double.parseDouble(prixOuverture.getText());

				typeOuvertureMur.setHauteur(hauteur_value);
				typeOuvertureMur.setLargeur(largeur_value);
				typeOuvertureMur.setPrixOuverture(prixOuverture_value);

				config.getMainWindow().getCanvasContainer().redraw();
			} catch (NumberFormatException e) {
				config.tui.error("typeOuvertureMurPane: validate: failed to parse double numbers: " + e.getMessage());
				config.tui.debug(Arrays.toString(e.getStackTrace()));
			}
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new Label("Hauteur :"),
					hauteur
				),

				new HBox(
					new Label("Largeur :"),
					largeur
				),

				new HBox(
					new Label("Prix ouverture :"),
					prixOuverture
				)
			)
		);

		super.initialize();
	}
}
