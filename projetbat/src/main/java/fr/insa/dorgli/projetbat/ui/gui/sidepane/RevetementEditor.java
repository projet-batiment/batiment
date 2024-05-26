package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Revetement;
import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;
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

public class RevetementEditor extends Editor {
	public RevetementEditor(Config config, Revetement revetement) {
		super(config, "Revêtement");

		WrapLabel reactive = new WrapLabel("Positionnemenent automatique sur la surface.");
		WrapLabel aire = new WrapLabel();
		reactive.setTextFill(Color.RED);
		reactive.managedProperty().bind(reactive.visibleProperty());

		WrapLabel prix = new WrapLabel();

		TextField pos1L = new TextField();
		TextField pos1H = new TextField();
		TextField pos2L = new TextField();
		TextField pos2H = new TextField();

//		HBox.setHgrow(pos1L, Priority.ALWAYS);
//		HBox.setHgrow(pos1H, Priority.ALWAYS);
//		HBox.setHgrow(pos2L, Priority.ALWAYS);
//		HBox.setHgrow(pos2H, Priority.ALWAYS);

		pos1L.editableProperty().bind(reactive.visibleProperty());
		pos1H.editableProperty().bind(reactive.visibleProperty());
		pos2L.editableProperty().bind(reactive.visibleProperty());
		pos2H.editableProperty().bind(reactive.visibleProperty());

		ComboBox<TypeRevetement> typeRevetementCombo = new ComboBox<>();
		Callback<ListView<TypeRevetement>,ListCell<TypeRevetement>> callback = (l) -> new ListCell<>() {
			@Override
			protected void updateItem(TypeRevetement item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getNom());
				}
			}
		};
		typeRevetementCombo.setButtonCell(callback.call(null));
		typeRevetementCombo.setCellFactory(callback);

		Button becomeReactive = new Button("Position auto.");
		becomeReactive.setOnAction(eh -> {
			revetement.setPos1L(0);
			revetement.setPos1H(0);
			revetement.setPos2L(0);
			revetement.setPos2H(0);

			reset();
		});

		Button editParent = new Button("Éditer l'objet parent");
		editParent.setOnAction(eh -> {
			config.controller.state.setSelectedElement(revetement.getParents().getFirst());
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double pos1L_value = Double.parseDouble(pos1L.getText());
				double pos1H_value = Double.parseDouble(pos1H.getText());
				double pos2L_value = Double.parseDouble(pos2L.getText());
				double pos2H_value = Double.parseDouble(pos2H.getText());
				TypeRevetement typeRevetement_value = typeRevetementCombo.getValue();

				revetement.setPos1L(pos1L_value);
				revetement.setPos1H(pos1H_value);
				revetement.setPos2L(pos2L_value);
				revetement.setPos2H(pos2H_value);
				revetement.setTypeRevetement(typeRevetement_value);
			} catch (NumberFormatException e) {
				config.tui.error("revetementEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			reactive.setVisible(revetement.isReactive());
			aire.setText(String.valueOf(Math.floor(revetement.aire() * 100) / 100));

			ObservableList typeRevetementsItems = typeRevetementCombo.getItems();
			typeRevetementsItems.clear();
			config.project.objects.getAll().values()
				.stream().forEach(each -> {
					if (each instanceof TypeRevetement tr)
						typeRevetementsItems.add(tr);
				});

			if (revetement.getTypeRevetement()!= null) {
				typeRevetementCombo.setValue(revetement.getTypeRevetement());
			}

			if (revetement.getTypeRevetement() instanceof TypeRevetement)
				prix.setText(String.valueOf(revetement.calculerPrix()));
			else
				prix.setText("(aucun type de revêtement !)");

			pos1L.setText(String.valueOf(revetement.getPos1L()));
			pos1H.setText(String.valueOf(revetement.getPos1H()));
			pos2L.setText(String.valueOf(revetement.getPos2L()));
			pos2H.setText(String.valueOf(revetement.getPos2H()));
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(new WrapLabel("Coin 1 :"),
				new HBox(
					pos1L,
					pos1H
				),

				new WrapLabel("Coin 2 :"),
				new HBox(
					pos2L,
					pos2H
				),

				new HBox(
					new WrapLabel("Aire du revêtement :"),
					aire
				),

				new HBox(
					new WrapLabel("Type :"),
					typeRevetementCombo
				),

				new HBox(
					new WrapLabel("Prix :"),
					prix,
					new WrapLabel("€")
				),

				reactive,

				new HBox(
					becomeReactive,
					editParent
				)
			)
		);

		super.initialize();
	}
}