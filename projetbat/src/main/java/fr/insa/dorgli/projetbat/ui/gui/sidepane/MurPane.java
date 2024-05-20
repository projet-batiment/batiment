package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.PointComponent;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.types.TypeMur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MurPane extends ParentSidePane {
	Mur mur;
	Config config;

	private class MurEditor extends Editor {
		public MurEditor(Config config) {
			super(config, "Mur");

			TextField hauteur = new TextField();

			ComboBox<TypeMur> typeMurCombo = new ComboBox<>();
			Callback<ListView<TypeMur>,ListCell<TypeMur>> callback = (ListView<TypeMur> l) -> new ListCell<TypeMur>() {
				@Override
				protected void updateItem(TypeMur item, boolean empty) {
					super.updateItem(item, empty);
					
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					} else {
						setText(item.getNom());
					}
				}
			};
			typeMurCombo.setButtonCell(callback.call(null));
			typeMurCombo.setCellFactory(callback);

			super.prependSaveFunction((ActionEvent eh) -> {
				try {
					double hauteur_value = Double.parseDouble(hauteur.getText());
					TypeMur typeMur_value = typeMurCombo.getValue();

					if (typeMur_value != null) {
						mur.setHauteur(hauteur_value);
						mur.setTypeMur(typeMur_value);
					} else {
						config.tui.error("murEditor: validate: typeMur_value is null");
					}
				} catch (NumberFormatException e) {
					config.tui.error("murEditor: validate: failed to parse double numbers: " + e.getMessage());
				}
			});

			super.prependResetFunction(() -> {
				hauteur.setText(String.valueOf(mur.getHauteur()));

				ObservableList typeMurItems = typeMurCombo.getItems();
				typeMurItems.clear();
				config.project.objects.getAll().values()
					.stream().forEach(each -> {
						if (each instanceof TypeMur typeMur)
							typeMurItems.add(typeMur);
					});

				if (mur.getTypeMur() != null) {
					typeMurCombo.setValue(mur.getTypeMur());
				}
			});

			super.prependInitFunction((Pane pane) ->
				pane.getChildren().addAll(new VBox(
						new Label("Coordonnées :"),
						new PointComponent(config, this, mur.getPointDebut(), "Début :"),
						new PointComponent(config, this, mur.getPointFin(), "Fin :")
					),

					new HBox(
						new Label("Type :"),
						typeMurCombo
					),

					new HBox(
						new Label("Hauteur :"),
						hauteur
					)
				)
			);
		}
	}

	private class Children extends VBox {
		public Children() {
//			ListView<String> plafonds = new ListView<>();
//			ObservableList<String> plafondsItems = plafonds.getItems();
//			plafondsItems.add(piece.getPlafond().toStringShort());
//			HBox plafondsBox = new HBox(
//				new Label("Plafond :"),
//				plafonds
//			);
//
//			ListView<String> sols = new ListView<>();
//			ObservableList<String> solsItems = sols.getItems();
//			solsItems.add(piece.getSol().toStringShort());
//			HBox solsBox = new HBox(
//				new Label("Sols :"),
//				sols
//			);
//
//			ListView<String> murs = new ListView<>();
//			ObservableList<String> mursItems = murs.getItems();
//			for (Mur mur: piece.getMurs()) {
//				mursItems.add(mur.toStringShort());
//			}
//			VBox mursBox = new VBox(
//				new Label("Murs :"),
//				murs
//			);

//			super.getChildren().addAll(
//				plafondsBox,
//				solsBox,
//				mursBox
//			);
			super.getChildren().add(new Label("TODO!!"));
		}
	}

	@Override
	public final void update() {
		MurEditor editThis = new MurEditor(config);
		editThis.initialize();
		super.addTab(editThis);

		super.childrenTab.setContent(new Children());
	}

	public MurPane(Config config, Mur mur) {
		this.mur = mur;
		this.config = config;

		update();
	}
}