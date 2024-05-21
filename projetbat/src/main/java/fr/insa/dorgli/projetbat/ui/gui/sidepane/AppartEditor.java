package fr.insa.dorgli.projetbat.ui.gui.sidepane;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
			pane.getChildren().add(
				new HBox(
					new Label("Type :"),
					typeAppartCombo
				)
			)
		);

		super.initialize();
	}
}

