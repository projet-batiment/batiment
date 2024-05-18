package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Mur;
import fr.insa.dorgli.projetbat.objects.Piece;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PiecePane extends SidePane {
	Piece piece;

	private class Editor extends VBox {
		TextField name;
		TextArea description;

		private void reset() {
			name.setText(piece.getNom());
			description.setText(piece.getDescription());
		}

		public Editor(Config config) {
			name = new TextField();
			description = new TextArea();
			reset();

			this.widthProperty().addListener((Observable eh) -> {
				double maxWidth = this.getWidth() - 5;
				name.setMaxWidth(maxWidth);
				description.setMaxWidth(maxWidth);
			});

			HBox.setHgrow(name, Priority.ALWAYS);
			description.setWrapText(true);

			Button save = new Button("Valider");
			save.setOnAction((ActionEvent eh) -> {
				piece.setNom(name.getText());
				piece.setDescription(description.getText());

				config.getMainWindow().getCanvasContainer().redraw();
			});

			Button cancel = new Button("Annuler");
			cancel.setOnAction((ActionEvent eh) -> {
				reset();
			});

			super.getChildren().addAll(
				new HBox(
					new Label("Nom :"),
					name
				),

				new VBox(
					new Label("Description :"),
					description
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
			ListView<String> plafonds = new ListView<>();
			ObservableList<String> plafondsItems = plafonds.getItems();
			plafondsItems.add(piece.getPlafond().toStringShort());

			ListView<String> sols = new ListView<>();
			ObservableList<String> solsItems = sols.getItems();
			solsItems.add(piece.getSol().toStringShort());

			ListView<String> murs = new ListView<>();
			ObservableList<String> mursItems = murs.getItems();
			for (Mur mur: piece.getMurs()) {
				mursItems.add(mur.toStringShort());
			}

			super.getChildren().addAll(
				new HBox(
					new Label("Plafond :"),
					plafonds
				),

				new HBox(
					new Label("Sols :"),
					sols
				),

				new VBox(
					new Label("Murs :"),
					murs
				)
			);
		}
	}

	public PiecePane(Config config, Piece piece) {
		this.piece = piece;

		super.editorTab.setContent(new Editor(config));
		super.childrenTab.setContent(new Children());
	}
}