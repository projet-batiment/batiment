package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.objects.concrete.PlafondSol;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PieceEditor extends NameDescEditor {
	public PieceEditor(Config config, Piece piece) {
		super(config, "Piece", piece);

		WrapLabel prix = new WrapLabel();
		WrapLabel aire = new WrapLabel();

		WrapLabel notReady = new WrapLabel("Cette pièce n'est pas prête.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

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
		editSol.setOnAction(eh -> {
			if (! (piece.getSol() instanceof PlafondSol)) {
				PlafondSol newSol = new PlafondSol();
				config.project.objects.put(newSol);
				piece.setSol(newSol);
			}
			config.controller.state.setSelectedElement(piece.getSol());
		});

		Button addToAppart = new Button("Ajouter à un appartement...");
		addToAppart.setOnAction(eh -> {
			if (piece.getParents().stream().filter(each -> each instanceof Piece).count() == 0) {
				Set objects = new HashSet(config.project.objects.getApparts());
				Appart appart = (Appart) config.controller.chooseFromList("appartement", objects);
				if (appart != null) {
					appart.addChildren(piece);
					config.controller.refreshUI();
				}
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Ce piece appartient déjà à un appartement.").showAndWait();
			}
		});

		Button removeFromAppart = new Button("Supprimer de l'appartement");
		removeFromAppart.setOnAction(eh -> {
			Optional<SelectableId> result = piece.getParents().stream().filter(each -> each instanceof Appart).findFirst();
			if (result.isPresent()) {
				((Appart) result.get()).addChildren(piece);
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Cette pièce n'appartient à aucun appartement !").showAndWait();
			}
		});

		Button editAppart = new Button("Éditer le niveau");
		editAppart.setOnAction(eh -> {
			Optional<SelectableId> result = piece.getParents().stream().filter(each -> each instanceof Niveau).findFirst();
			if (result.isPresent()) {
				config.controller.state.setSelectedElement(result.get());
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Cette pièce n'appartient à aucun niveau ! Ce n'est pas normal !!").showAndWait();
			}
		});

		Button editNiveau = new Button("Éditer l'appartement");
		editNiveau.setOnAction(eh -> {
			Optional<SelectableId> result = piece.getParents().stream().filter(each -> each instanceof Appart).findFirst();
			if (result.isPresent()) {
				config.controller.state.setSelectedElement(result.get());
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Cette pièce n'appartient à aucun appartement !").showAndWait();
			}
		});

		super.prependResetFunction(() -> {
			prix.setText(String.valueOf(piece.calculerPrix()));
			aire.setText(String.valueOf(piece.aire()));

			notReady.setVisible(! piece.ready());
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Prix :"),
					prix,
					new WrapLabel("€")
				),

				new HBox(
					new WrapLabel("Aire :"),
					aire
				),

				notReady,
				new Separator(),

				new HBox(
					new WrapLabel("Éditer : "),
					editPlafond,
					editSol
				),

				new Separator(),
				new HBox(
					addToAppart,
					removeFromAppart
				),
				new HBox(
					editNiveau,
					editAppart
				)
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