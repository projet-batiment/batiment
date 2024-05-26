package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.OuvertureMur;
import fr.insa.dorgli.projetbat.objects.types.TypeOuvertureMur;
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
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class OuvertureMurEditor extends Editor {
	public OuvertureMurEditor(Config config, OuvertureMur ouverture) {
		super(config, "Ouverture");

		ComboBox<TypeOuvertureMur> typeOuvertureMurCombo = new ComboBox<>();
		Callback<ListView<TypeOuvertureMur>,ListCell<TypeOuvertureMur>> callback = (l) -> new ListCell<>() {
			@Override
			protected void updateItem(TypeOuvertureMur item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getNom());
				}
			}
		};
		typeOuvertureMurCombo.setButtonCell(callback.call(null));
		typeOuvertureMurCombo.setCellFactory(callback);

		WrapLabel prix = new WrapLabel();

		TextField posL = new TextField();
		TextField posH = new TextField();

		Button editParent = new Button("Éditer l'objet parent");
		editParent.setOnAction(eh -> {
			config.controller.state.setSelectedElement(ouverture.getParents().getFirst());
		});

		Button editType = new Button("Éditer");
		editType.setOnAction(eh -> {
			if (typeOuvertureMurCombo.getValue() instanceof TypeOuvertureMur to)
				config.controller.state.setSelectedElement(to);
		});

		WrapLabel notReady = new WrapLabel("Cette ouverture n'est pas prête.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double posL_value = Double.parseDouble(posL.getText());
				double posH_value = Double.parseDouble(posH.getText());
				TypeOuvertureMur typeOuvertureMur_value = typeOuvertureMurCombo.getValue();

				ouverture.setPosL(posL_value);
				ouverture.setPosH(posH_value);
				ouverture.setTypeOuverture(typeOuvertureMur_value);
			} catch (NumberFormatException e) {
				config.tui.error("ouvertureEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			ObservableList typeOuvertureMurItems = typeOuvertureMurCombo.getItems();
			typeOuvertureMurItems.clear();
			config.project.objects.getAll().values()
				.stream().forEach(each -> {
					if (each instanceof TypeOuvertureMur typeOuvertureMur)
						typeOuvertureMurItems.add(typeOuvertureMur);
				});

			if (ouverture.getTypeOuverture()!= null) {
				typeOuvertureMurCombo.setValue(ouverture.getTypeOuverture());
			}

			if (ouverture.getTypeOuverture() instanceof TypeOuvertureMur to)
				prix.setText(String.valueOf(to.getPrixOuverture()));
			else
				prix.setText("(aucun type d'ouverture !)");

			posL.setText(String.valueOf(ouverture.getPosL()));
			posH.setText(String.valueOf(ouverture.getPosH()));

			notReady.setVisible(! ouverture.ready());
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(new WrapLabel("Position :"),
				new HBox(
					posL,
					posH
				),

				new HBox(
					new WrapLabel("Type :"),
					typeOuvertureMurCombo,
					editType
				),

				new HBox(
					new WrapLabel("Prix :"),
					prix,
					new WrapLabel("€")
				),

				notReady,
				editParent
			)
		);

		super.initialize();
	}
}