package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
import java.util.Arrays;
import javafx.event.ActionEvent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class TypeRevetementEditor extends NameDescEditor {
	public TypeRevetementEditor(Config config, TypeRevetement typeRevetement) {
		super(config, "TypeRevetement", typeRevetement);

		TextField prixUnitaire = new TextField();
		HBox.setHgrow(prixUnitaire, Priority.ALWAYS);

		super.prependResetFunction(() -> {
			prixUnitaire.setText(String.valueOf(typeRevetement.getPrixUnitaire()));
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double prixUnitaire_value = Double.parseDouble(prixUnitaire.getText());
				typeRevetement.setPrixUnitaire(prixUnitaire_value);

				config.getMainWindow().getCanvasContainer().redraw();
			} catch (NumberFormatException e) {
				config.tui.error("typeRevetementPane: validate: failed to parse double numbers: " + e.getMessage());
				config.tui.debug(Arrays.toString(e.getStackTrace()));
			}
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Prix unitaire :"),
					prixUnitaire
				)
			)
		);

		super.initialize();
	}
}