package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeMur;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class TypeMurPane extends SidePane {
	TypeMur typeMur;
	Config config;

	private class TypeMurEditor extends NameDescEditor {
		public TypeMurEditor(Config config) {
			super(config, "TypeMur", typeMur);

			TextField epaisseur = new TextField();
			TextField prixUnitaire = new TextField();

			HBox.setHgrow(epaisseur, Priority.ALWAYS);
			HBox.setHgrow(prixUnitaire, Priority.ALWAYS);

			super.prependResetFunction(() -> {
				epaisseur.setText(String.valueOf(typeMur.getEpaisseur()));
				prixUnitaire.setText(String.valueOf(typeMur.getPrixUnitaire()));
			});

			super.prependSaveFunction((ActionEvent eh) -> {
				try {
					double epaisseur_value = Double.parseDouble(epaisseur.getText());
					double prixUnitaire_value = Double.parseDouble(prixUnitaire.getText());

					typeMur.setEpaisseur(epaisseur_value);
					typeMur.setPrixUnitaire(prixUnitaire_value);

					config.getMainWindow().getCanvasContainer().redraw();
				} catch (NumberFormatException e) {
					config.tui.error("typeMurPane: validate: failed to parse double numbers: " + e.getMessage());
					config.tui.debug(Arrays.toString(e.getStackTrace()));
				}
			});

			super.prependInitFunction((Pane pane) ->
				pane.getChildren().addAll(
					new HBox(
						new Label("Épaisseur :"),
						epaisseur
					),

					new HBox(
						new Label("Prix unitaire :"),
						prixUnitaire
					)
				)
			);
		}
	}

	@Override
	public final void update() {
		TypeMurEditor editThis = new TypeMurEditor(config);
		editThis.initialize();
		super.addTab(editThis);
	}

	public TypeMurPane(Config config, TypeMur typeMur) {
		this.config = config;
		this.typeMur = typeMur;

		update();
	}
}