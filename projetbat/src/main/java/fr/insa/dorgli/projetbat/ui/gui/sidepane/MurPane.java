package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Mur;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MurPane extends ParentSidePane {
	Mur mur;

	private class Editor extends VBox {
		TextField x1;
		TextField y1;
		TextField x2;
		TextField y2;
		TextField hauteur;
		Label typeMur; // TODO!!!

		private void reset() {
			x1.setText(String.valueOf(mur.getPointDebut().getX()));
			y1.setText(String.valueOf(mur.getPointDebut().getY()));
			x2.setText(String.valueOf(mur.getPointFin().getX()));
			y2.setText(String.valueOf(mur.getPointFin().getY()));
			hauteur.setText(String.valueOf(mur.getHauteur()));
			typeMur.setText(mur.getTypeMur().getNom());
		}

		public Editor(Config config) {
			x1 = new TextField();
			y1 = new TextField();
			x2 = new TextField();
			y2 = new TextField();
			hauteur = new TextField();
			typeMur = new Label();

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
					// TODO!! typeMur

					mur.getPointDebut().setX(x1_value);
					mur.getPointDebut().setY(y1_value);
					mur.getPointFin().setX(x2_value);
					mur.getPointFin().setY(y2_value);
					mur.setHauteur(hauteur_value);
//					mur.setTypeMur(typeMur_value);

					config.getMainWindow().getCanvasContainer().redraw();
				} catch (NumberFormatException e) {
					config.tui.error("murPane: validate: failed to parse double numbers: " + e.getMessage());
					config.tui.debug(Arrays.toString(e.getStackTrace()));
				}
			});

			Button cancel = new Button("Annuler");
			cancel.setOnAction((ActionEvent eh) -> {
				reset();
			});

			super.getChildren().addAll(
				new VBox(
					new Label("Coordonnées :"),
					new Label("Début :"),
					new HBox(x1, y1),
					new Label("Fin :"),
					new HBox(x2, y2)
				),

				new HBox(
					new Label("Type :"),
					typeMur
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

	public MurPane(Config config, Mur mur) {
		this.mur = mur;

		super.editorTab.setContent(new Editor(config));
		super.childrenTab.setContent(new Children());
	}
}

