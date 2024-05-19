package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Mur;
import fr.insa.dorgli.projetbat.objects.TypeMur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MurPane extends ParentSidePane {
	Mur mur;
	Config config;

	private class Editor extends VBox {
		Config config;

		TextField x1;
		TextField y1;
		TextField x2;
		TextField y2;
		TextField hauteur;
		ComboBox<TypeMur> typeMurCombo;

		private void reset() {
			x1.setText(String.valueOf(mur.getPointDebut().getX()));
			y1.setText(String.valueOf(mur.getPointDebut().getY()));
			x2.setText(String.valueOf(mur.getPointFin().getX()));
			y2.setText(String.valueOf(mur.getPointFin().getY()));
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
		}

		public Editor(Config config) {
			this.config = config;

			x1 = new TextField();
			y1 = new TextField();
			x2 = new TextField();
			y2 = new TextField();
			hauteur = new TextField();

			typeMurCombo = new ComboBox<>();
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

			reset();

			HBox.setHgrow(x1, Priority.ALWAYS);
			HBox.setHgrow(y1, Priority.ALWAYS);
			HBox.setHgrow(x2, Priority.ALWAYS);
			HBox.setHgrow(y2, Priority.ALWAYS);

			Button save = new Button("Valider");
			save.setOnAction((ActionEvent eh) -> {
				try {
					double x1_value = Double.parseDouble(x1.getText());
					double y1_value = Double.parseDouble(y1.getText());
					double x2_value = Double.parseDouble(x2.getText());
					double y2_value = Double.parseDouble(y2.getText());
					double hauteur_value = Double.parseDouble(hauteur.getText());
					TypeMur typeMur_value = typeMurCombo.getValue();

					if (typeMur_value != null) {
						mur.getPointDebut().setX(x1_value);
						mur.getPointDebut().setY(y1_value);
						mur.getPointFin().setX(x2_value);
						mur.getPointFin().setY(y2_value);
						mur.setHauteur(hauteur_value);
						mur.setTypeMur(typeMur_value);

						config.getMainWindow().getCanvasContainer().redraw();
					} else {
						config.tui.error("murPane: validate: typeMur_value is null");
					}
				} catch (NumberFormatException e) {
					config.tui.error("murPane: validate: failed to parse double numbers: " + e.getMessage());
				}
			});

			Button cancel = new Button("Annuler");
			cancel.setOnAction((ActionEvent eh) -> {
				reset();
			});

			super.getChildren().addAll(new VBox(
					new Label("Coordonnées :"),
					new Label("Début :"),
					new HBox(x1, y1),
					new Label("Fin :"),
					new HBox(x2, y2)
				),

				new HBox(
					new Label("Type :"),
					typeMurCombo
				),

				new HBox(
					new Label("Hauteur :"),
					hauteur
				),

				new HBox(
					save,
					cancel
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
		super.editorTab.setContent(new Editor(config));
		super.childrenTab.setContent(new Children());
	}

	public MurPane(Config config, Mur mur) {
		this.mur = mur;
		this.config = config;

		update();
	}
}

