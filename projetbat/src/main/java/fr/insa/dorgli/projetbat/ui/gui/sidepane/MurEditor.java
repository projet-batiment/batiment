package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.PointComponent;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.objects.concrete.OuvertureMur;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.objects.concrete.RevetementMur;
import fr.insa.dorgli.projetbat.objects.types.TypeMur;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MurEditor extends Editor {
	public MurEditor(Config config, Mur mur) {
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

		Button addToPiece = new Button("Ajouter à une pièce...");
		addToPiece.setOnAction(eh -> {
			if (mur.getParents().stream().filter(each -> each instanceof Piece).count() < 2) {
				Set objects = new HashSet(mur.getPointDebut().getNiveau().getPieces());
				Piece piece = (Piece) config.controller.chooseFromList("pièce", objects);
				if (piece != null) {
					mur.addToPiece(piece);
					config.controller.refreshUI();
				}
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Ce mur appartient déjà à deux pièces.").showAndWait();
			}
		});

		Button removeFromPiece = new Button("Supprimer d'une pièce...");
		removeFromPiece.setOnAction(eh -> {
			if (mur.getParents().stream().filter(each -> each instanceof Piece).count() > 0) {
				Set objects = new HashSet(mur.getPointDebut().getNiveau().getPieces());
				var choosen = config.controller.chooseFromList("pièce", objects);
				if (choosen instanceof Piece piece) {
					mur.removeFromPiece(piece, (Niveau) config.controller.state.getViewRootElement());
					config.controller.refreshUI();
				}
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Ce mur n'appartient à aucune pièce !").showAndWait();
			}
		});

		ButtonnedListComponent revetements1 = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) mur.getRevetements1(), "Revêtements (1)");
		revetements1.setAddObject(() -> {
			Set objects = new HashSet(config.project.objects.getRevetementsMur());
			config.tui.println(String.valueOf(config.project.objects.getRevetementsMur()));
			var choosen = config.controller.chooseFromList("revêtement", objects);
			if (choosen instanceof RevetementMur r) {
				mur.addRevetement1(r);
				revetements1.update();
			}
		});

		revetements1.setRemoveObject(() -> {
			Set objects = new HashSet(mur.getRevetements1());
			var choosen = config.controller.chooseFromList("revêtement", objects);
			if (choosen instanceof RevetementMur r) {
				mur.removeChildren(r);
				revetements1.update();
			}
		});

		revetements1.setEditObject(() -> {
			Set objects = new HashSet(mur.getRevetements1());
			var selected = config.controller.chooseFromList("revêtement", objects);
			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
				revetements1.update();
			}
		});

		ButtonnedListComponent revetements2 = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) mur.getRevetements2(), "Revêtements (2)");

		revetements2.setAddObject(() -> {
			Set objects = new HashSet(config.project.objects.getRevetementsMur());
			var choosen = config.controller.chooseFromList("revêtement", objects);
			if (choosen instanceof RevetementMur r) {
				mur.addRevetement2(r);
				revetements2.update();
			}
		});

		revetements2.setRemoveObject(() -> {
			Set objects = new HashSet(mur.getRevetements2());
			var choosen = config.controller.chooseFromList("revêtement", objects);
			if (choosen instanceof RevetementMur r) {
				mur.removeChildren(r);
				revetements2.update();
			}
		});

		revetements2.setEditObject(() -> {
			Set objects = new HashSet(mur.getRevetements2());
			var selected = config.controller.chooseFromList("revêtement", objects);
			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
				revetements2.update();
			}
		});

		ButtonnedListComponent ouvertures = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) mur.getRevetements2(), "Ouvertures");

		ouvertures.setAddObject(() -> {
			Set objects = new HashSet(config.project.objects.getOuverturesMur());
			var choosen = config.controller.chooseFromList("ouverture", objects);
			if (choosen instanceof OuvertureMur o) {
				mur.addOuverture(o);
				ouvertures.update();
			}
		});

		ouvertures.setRemoveObject(() -> {
			Set objects = new HashSet(mur.getRevetements2());
			var choosen = config.controller.chooseFromList("ouverture", objects);
			if (choosen instanceof OuvertureMur o) {
				mur.removeChildren(o);
				ouvertures.update();
			}
		});

		ouvertures.setEditObject(() -> {
			Set objects = new HashSet(mur.getRevetements2());
			var selected = config.controller.chooseFromList("ouverture", objects);
			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
				ouvertures.update();
			}
		});

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
					new WrapLabel("Coordonnées :"),
					new PointComponent(config, this, mur.getPointDebut(), "Début :"),
					new PointComponent(config, this, mur.getPointFin(), "Fin :")
				),

				new HBox(
					new WrapLabel("Type :"),
					typeMurCombo
				),

				new HBox(
					new WrapLabel("Hauteur :"),
					hauteur
				),

				addToPiece,
				removeFromPiece,

				revetements1,
				revetements2,
				ouvertures
			)
		);

		super.initialize();
	}
}

//	private class Children extends VBox {
//		public Children() {
//			ListView<String> plafonds = new ListView<>();
//			ObservableList<String> plafondsItems = plafonds.getItems();
//			plafondsItems.add(piece.getPlafond().toStringShort());
//			HBox plafondsBox = new HBox(
//				new WrapLabel("Plafond :"),
//				plafonds
//			);
//
//			ListView<String> sols = new ListView<>();
//			ObservableList<String> solsItems = sols.getItems();
//			solsItems.add(piece.getSol().toStringShort());
//			HBox solsBox = new HBox(
//				new WrapLabel("Sols :"),
//				sols
//			);
//
//			ListView<String> murs = new ListView<>();
//			ObservableList<String> mursItems = murs.getItems();
//			for (Mur mur: piece.getMurs()) {
//				mursItems.add(mur.toStringShort());
//			}
//			VBox mursBox = new VBox(
//				new WrapLabel("Murs :"),
//				murs
//			);

//			super.getChildren().addAll(
//				plafondsBox,
//				solsBox,
//				mursBox
//			);
//			super.getChildren().add(new WrapLabel("TODO!!"));
//		}
//	}