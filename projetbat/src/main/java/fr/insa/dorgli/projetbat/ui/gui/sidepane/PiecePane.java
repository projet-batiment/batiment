package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PiecePane extends ParentSidePane {
	Piece piece;
	Config config;

	private class PieceEditor extends NameDescEditor {
		public PieceEditor(Config config) {
			super(config, "Piece", piece);

			TextField name = new TextField();
			TextArea description = new TextArea();

			super.prependSaveFunction((ActionEvent eh) -> {
				// TODO!!
			});

			super.prependResetFunction(() -> {
				name.setText(piece.getNom());
				description.setText(piece.getDescription());
			});

			super.prependInitFunction((Pane pane) ->
				pane.getChildren().addAll(
					new Label("Piece: TODO!!")
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

	@Override
	public final void update() {
		PieceEditor editThis = new PieceEditor(config);
		editThis.initialize();
		super.addTab(editThis);

		super.childrenTab.setContent(new Children());
	}

	public PiecePane(Config config, Piece piece) {
		this.piece = piece;
		this.config = config;

		update();
	}
}