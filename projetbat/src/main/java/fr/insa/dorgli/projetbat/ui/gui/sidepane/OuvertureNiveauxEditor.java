package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.OuvertureNiveaux;
import fr.insa.dorgli.projetbat.objects.types.TypeOuvertureNiveaux;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class OuvertureNiveauxEditor extends Editor {
	public OuvertureNiveauxEditor(Config config, OuvertureNiveaux ouverture) {
		super(config, "Ouverture");

		ComboBox<TypeOuvertureNiveaux> typeOuvertureNiveauxCombo = new ComboBox<>();
		Callback<ListView<TypeOuvertureNiveaux>,ListCell<TypeOuvertureNiveaux>> callback = (l) -> new ListCell<>() {
			@Override
			protected void updateItem(TypeOuvertureNiveaux item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getNom());
				}
			}
		};
		typeOuvertureNiveauxCombo.setButtonCell(callback.call(null));
		typeOuvertureNiveauxCombo.setCellFactory(callback);

		WrapLabel prix = new WrapLabel();

		TextField posL = new TextField();
		TextField posH = new TextField();

		Button editParent = new Button("Éditer l'objet parent");
		editParent.setOnAction(eh -> {
			config.controller.state.setSelectedElement(ouverture.getParents().getFirst());
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double posL_value = Double.parseDouble(posL.getText());
				double posH_value = Double.parseDouble(posH.getText());
				TypeOuvertureNiveaux typeOuvertureNiveaux_value = typeOuvertureNiveauxCombo.getValue();

				ouverture.setPosL(posL_value);
				ouverture.setPosH(posH_value);
				ouverture.setTypeOuverture(typeOuvertureNiveaux_value);
			} catch (NumberFormatException e) {
				config.tui.error("ouvertureEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			ObservableList typeOuvertureNiveauxItems = typeOuvertureNiveauxCombo.getItems();
			typeOuvertureNiveauxItems.clear();
			config.project.objects.getAll().values()
				.stream().forEach(each -> {
					if (each instanceof TypeOuvertureNiveaux typeOuvertureNiveaux)
						typeOuvertureNiveauxItems.add(typeOuvertureNiveaux);
				});

			if (ouverture.getTypeOuverture()!= null) {
				typeOuvertureNiveauxCombo.setValue(ouverture.getTypeOuverture());
			}

			if (ouverture.getTypeOuverture() instanceof TypeOuvertureNiveaux to)
				prix.setText(String.valueOf(to.getPrixOuverture()));
			else
				prix.setText("(aucun type d'ouverture !)");

			posL.setText(String.valueOf(ouverture.getPosL()));
			posH.setText(String.valueOf(ouverture.getPosH()));
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(new WrapLabel("Position :"),
				new HBox(
					posL,
					posH
				),

				new HBox(
					new WrapLabel("Type :"),
					typeOuvertureNiveauxCombo
				),

				new HBox(
					new WrapLabel("Prix :"),
					prix,
					new WrapLabel("€")
				),

				editParent
			)
		);

		super.initialize();
	}
}