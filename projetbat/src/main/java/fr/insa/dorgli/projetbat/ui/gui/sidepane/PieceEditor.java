package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PieceEditor extends NameDescEditor {
	public PieceEditor(Config config, Piece piece) {
		super(config, "Piece", piece);

		super.prependSaveFunction((ActionEvent eh) -> {
			// TODO!!
		});

		super.prependResetFunction(() -> {
			// TODO!!
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new Label("Piece: TODO!!")
			)
		);

		super.initialize();
	}
}
//
//private class Children extends VBox {
//	public Children() {
//		ListView<String> plafonds = new ListView<>();
//		ObservableList<String> plafondsItems = plafonds.getItems();
//		plafondsItems.add(piece.getPlafond().toStringShort());
//
//		ListView<String> sols = new ListView<>();
//		ObservableList<String> solsItems = sols.getItems();
//		solsItems.add(piece.getSol().toStringShort());
//
//		ListView<String> murs = new ListView<>();
//		ObservableList<String> mursItems = murs.getItems();
//		for (Mur mur: piece.getMurs()) {
//			mursItems.add(mur.toStringShort());
//		}
//
//		super.getChildren().addAll(
//			new HBox(
//				new Label("Plafond :"),
//				plafonds
//			),
//
//			new HBox(
//				new Label("Sols :"),
//				sols
//			),
//
//			new VBox(
//				new Label("Murs :"),
//				murs
//			)
//		);
//	}
//}