package fr.insa.dorgli.projetbat.ui.gui.sidepane;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class AppartEditor extends NameDescEditor {
	public AppartEditor(Config config, Appart appart) {
		super(config, "Appart", appart);

		ComboBox<TypeAppart> typeAppartCombo = new ComboBox<>();
		Callback<ListView<TypeAppart>,ListCell<TypeAppart>> callback = (ListView<TypeAppart> l) -> new ListCell<TypeAppart>() {
			@Override
			protected void updateItem(TypeAppart item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getNom());
				}
			}
		};
		typeAppartCombo.setButtonCell(callback.call(null));
		typeAppartCombo.setCellFactory(callback);

		WrapLabel prix = new WrapLabel();

		Button editType = new Button("Éditer");
		editType.setOnAction(eh -> {
			if (typeAppartCombo.getValue() instanceof TypeAppart ta)
				config.controller.state.setSelectedElement(ta);
		});

		WrapLabel notReady = new WrapLabel("Cet appartement n'est pas prêt.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

		ButtonnedListComponent pieces = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) appart.getPieces(), "Pièces");

		pieces.setRemoveObject(() -> {
			Set objects = new HashSet(appart.getPieces());
			Piece selected = (Piece) config.controller.chooseFromList("pièce", objects);

			if (selected != null) {
				appart.removeChildren(selected);
				pieces.update();
			}
		});

		pieces.setAddObject(() -> {
			Set objects = config.project.objects.getPieces();
			var selected = config.controller.chooseFromList("pièce", objects);
			if (selected instanceof Piece piece) {
				appart.addChildren(piece);
			}
		});

		pieces.setEditObject(() -> {
			Set objects = new HashSet(appart.getPieces());
			Piece selected = (Piece) config.controller.chooseFromList("pièce", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				TypeAppart typeAppart_value = typeAppartCombo.getValue();

				if (typeAppart_value != null) {
					appart.setTypeAppart(typeAppart_value);
				} else {
					config.tui.error("appartEditor: validate: typeAppart_value is null");
				}
			} catch (NumberFormatException e) {
				config.tui.error("appartEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			ObservableList typeAppartItems = typeAppartCombo.getItems();
			typeAppartItems.clear();
			config.project.objects.getAll().values()
				.stream().forEach(each -> {
					if (each instanceof TypeAppart typeAppart)
						typeAppartItems.add(typeAppart);
				});

			if (appart.getTypeAppart() != null) {
				typeAppartCombo.setValue(appart.getTypeAppart());
			}

			pieces.update();
			prix.setText(String.valueOf(appart.calculerPrix()));

			notReady.setVisible(! appart.ready());
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Type d'appartement :"),
					typeAppartCombo,
					editType
				),

				new HBox(
					new WrapLabel("Prix total :"),
					prix,
					new WrapLabel("€")
				),

				notReady,

				new Separator(),
				pieces
			)
		);

		super.initialize();
	}
}