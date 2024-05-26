package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.objects.types.TypeBatiment;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class BatimentEditor extends NameDescEditor {
	public BatimentEditor(Config config, Batiment batiment) {
		super(config, "Batiment", batiment);

		ComboBox<TypeBatiment> typeBatimentCombo = new ComboBox<>();
		Callback<ListView<TypeBatiment>,ListCell<TypeBatiment>> callback = (ListView<TypeBatiment> l) -> new ListCell<TypeBatiment>() {
			@Override
			protected void updateItem(TypeBatiment item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getNom());
				}
			}
		};
		typeBatimentCombo.setButtonCell(callback.call(null));
		typeBatimentCombo.setCellFactory(callback);

		WrapLabel prix = new WrapLabel();

		WrapLabel notReady = new WrapLabel("Ce bâtiment n'est pas prêt.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

		Button editType = new Button("Éditer");
		editType.setOnAction(eh -> {
			if (typeBatimentCombo.getValue() instanceof TypeBatiment tb)
				config.controller.state.setSelectedElement(tb);
		});

		ButtonnedListComponent niveaux = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) batiment.getNiveaux(), "Niveaux");

		niveaux.setRemoveObject(() -> {
			Set objects = new HashSet(batiment.getNiveaux());

			Alert alert = new Alert(Alert.AlertType.WARNING,
			    "La suppression de niveaux n'est pas une action stable. Continuer ?",
			    ButtonType.OK,
			    ButtonType.CANCEL
			);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				Niveau selected = (Niveau) config.controller.chooseFromList("niveau", objects);

				if (selected != null) {
					batiment.removeChildren(selected);
					niveaux.update();
				}
			}
		});

		niveaux.setAddObject(() -> {
			Niveau n = new Niveau();
			batiment.addChildren(n);
			config.controller.menuButtonCreateOne(n);
		});

		niveaux.setEditObject(() -> {
			Set objects = new HashSet(batiment.getNiveaux());
			Niveau selected = (Niveau) config.controller.chooseFromList("niveau", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		ButtonnedListComponent apparts = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) batiment.getApparts(), "Appartements");

		apparts.setRemoveObject(() -> {
			Set objects = new HashSet(batiment.getApparts());

			Alert alert = new Alert(Alert.AlertType.WARNING,
			    "La suppression d'appartements n'est pas une action stable. Continuer ?",
			    ButtonType.OK,
			    ButtonType.CANCEL
			);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				Appart selected = (Appart) config.controller.chooseFromList("appartement", objects);

				if (selected != null) {
					batiment.removeChildren(selected);
					apparts.update();
				}
			}
		});

		apparts.setAddObject(() -> {
			new Alert(Alert.AlertType.INFORMATION, "Pour créer un appartement, utilisez le menu \"Créer > Un Appartement\"").showAndWait();
		});

		apparts.setEditObject(() -> {
			Set objects = new HashSet(batiment.getApparts());
			Appart selected = (Appart) config.controller.chooseFromList("appartement", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				TypeBatiment typeBatiment_value = typeBatimentCombo.getValue();

				if (typeBatiment_value != null) {
					batiment.setTypeBatiment(typeBatiment_value);
				} else {
					config.tui.error("batimentEditor: validate: typeBatiment_value is null");
				}
			} catch (NumberFormatException e) {
				config.tui.error("batimentEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			ObservableList typeBatimentItems = typeBatimentCombo.getItems();
			typeBatimentItems.clear();
			typeBatimentItems.addAll(config.project.objects.getTypesBatiment());

			if (batiment.getTypeBatiment()!= null) {
				typeBatimentCombo.setValue(batiment.getTypeBatiment());
			}

			niveaux.update();
			apparts.update();
			prix.setText(String.valueOf(batiment.calculerPrix()));

			notReady.setVisible(! batiment.ready());
		});

		super.prependInitFunction((Pane pane) -> {
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Type de bâtiment :"),
					typeBatimentCombo,
					editType
				),

				new HBox(
					new WrapLabel("Prix :"),
					prix,
					new WrapLabel("€")
				),

				notReady,

				new Separator(),
				niveaux,

				new Separator(),
				apparts
			);
		});
		super.initialize();
	}
}
