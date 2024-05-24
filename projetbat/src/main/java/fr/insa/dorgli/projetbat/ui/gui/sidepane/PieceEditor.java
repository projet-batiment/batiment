package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.objects.concrete.PlafondSol;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.IcedListComponent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PieceEditor extends NameDescEditor {
	public PieceEditor(Config config, Piece piece) {
		super(config, "Piece", piece);

		Button editPlafond = new Button("le plafond");
		editPlafond.setOnAction(eh -> {
			if (! (piece.getPlafond() instanceof PlafondSol)) {
				PlafondSol newPlafond = new PlafondSol();
				config.project.objects.put(newPlafond);
				piece.setPlafond(newPlafond);
			}
			config.controller.state.setSelectedElement(piece.getPlafond());
		});

		Button editSol = new Button("le sol");
		editPlafond.setOnAction(eh -> {
			if (! (piece.getSol() instanceof PlafondSol)) {
				PlafondSol newSol = new PlafondSol();
				config.project.objects.put(newSol);
				piece.setSol(newSol);
			}
			config.controller.state.setSelectedElement(piece.getSol());
		});

		ButtonnedListComponent murs = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) piece.getMurs(), "Murs");

		murs.setAddObject(() -> {
			new Alert(AlertType.INFORMATION, "Pour ajouter un ou plusieurs murs à cette pièce, sélectionnez les sur le plan.").showAndWait();
		});

		murs.setRemoveObject(() -> {
			Set objects = new HashSet(piece.getMurs());
			Mur selected = (Mur) config.controller.chooseFromList("pièce", objects);

			if (selected != null) {
				piece.removeChildren(selected);
				murs.update();
			}
		});

		murs.setEditObject(() -> {
			Set objects = new HashSet(piece.getMurs());
			Mur selected = (Mur) config.controller.chooseFromList("mur", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
				murs.update();
			}
		});

		super.prependResetFunction(() -> {
			murs.update();
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Éditer : "),
					editPlafond,
					editSol
				),

				murs
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
//				new WrapLabel("Plafond :"),
//				plafonds
//			),
//
//			new HBox(
//				new WrapLabel("Sols :"),
//				sols
//			),
//
//			new VBox(
//				new WrapLabel("Murs :"),
//				murs
//			)
//		);
//	}
//}