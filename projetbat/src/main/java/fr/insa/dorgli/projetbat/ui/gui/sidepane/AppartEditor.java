package fr.insa.dorgli.projetbat.ui.gui.sidepane;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.IcedListComponent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

		Button removePiece = new Button("Enlever...");
		removePiece.setOnAction(eh -> {
			Set objects = new HashSet(appart.getPieces());
			appart.getPieces().remove((Piece) config.controller.chooseFromList("pièce", objects));
		});

		Button addPiece = new Button("Ajouter...");
		addPiece.setOnAction(eh -> {
			new Alert(AlertType.INFORMATION, "Pour ajouter une ou plusieurs pièces à cet appartement, sélectionnez les sur le plan.").showAndWait();
		});

		VBox pieces = new IcedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) appart.getPieces());
//		final int maxNameLength = 23;
//		VBox pieces = new VBox();
//		for (Piece p: appart.getPieces()) {
//			String name = p.getNom();
//			if (name.length() > maxNameLength)
//				name = name.substring(0, maxNameLength - 2) + "...";
//			pieces.getChildren().add(new WrapLabel(name));
//		}

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
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Type d'appartement :"),
					typeAppartCombo
				),

				new HBox(
					new WrapLabel("Pièces :"),
					addPiece,
					removePiece
				),
				pieces
			)
		);

		super.initialize();
	}
}

