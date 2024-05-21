package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class TypeRevetementPane extends SidePane {
	TypeRevetement typeRevetement;
	Config config;

	private class TypeRevetementEditor extends NameDescEditor {
		public TypeRevetementEditor(Config config) {
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
						new Label("Prix unitaire :"),
						prixUnitaire
					)
				)
			);
		}
	}

	@Override
	public final void update() {
		TypeRevetementEditor editThis = new TypeRevetementEditor(config);
		editThis.initialize();
		super.addTab(editThis);
	}

	public TypeRevetementPane(Config config, TypeRevetement typeRevetement) {
		this.config = config;
		this.typeRevetement = typeRevetement;

		update();
	}
}
