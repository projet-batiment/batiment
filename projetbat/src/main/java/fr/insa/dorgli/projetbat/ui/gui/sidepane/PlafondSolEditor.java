package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.OuvertureNiveaux;
import fr.insa.dorgli.projetbat.objects.concrete.PlafondSol;
import fr.insa.dorgli.projetbat.objects.concrete.RevetementPlafondSol;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PlafondSolEditor extends Editor {
	public PlafondSolEditor(Config config, PlafondSol ps) {
		super(config, "Plafond/Sol");

		WrapLabel prixPS = new WrapLabel();
		WrapLabel airePS = new WrapLabel();

		WrapLabel notReady = new WrapLabel("Ce plafond n'est pas prêt.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

		ButtonnedListComponent revetements = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) ps.getRevetements(), "Revêtements");
		revetements.setAddObject(() -> {
			RevetementPlafondSol r = new RevetementPlafondSol();
			ps.addChildren(r);
			config.controller.menuButtonCreateOne(r);
		});

		revetements.setRemoveObject(() -> {
			Set objects = new HashSet(ps.getRevetements());
			var choosen = config.controller.chooseFromList("revêtement", objects);
			if (choosen instanceof RevetementPlafondSol r) {
				ps.removeChildren(r);
				revetements.update();
			}
		});

		revetements.setEditObject(() -> {
			Set objects = new HashSet(ps.getRevetements());
			var selected = config.controller.chooseFromList("revêtement", objects);
			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		ButtonnedListComponent ouvertures = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) ps.getOuvertures(), "Ouvertures");

		ouvertures.setAddObject(() -> {
			OuvertureNiveaux o = new OuvertureNiveaux();
			ps.addChildren(o);
			config.controller.menuButtonCreateOne(o);
		});

		ouvertures.setRemoveObject(() -> {
			Set objects = new HashSet(ps.getOuvertures());
			var choosen = config.controller.chooseFromList("ouverture", objects);
			if (choosen instanceof OuvertureNiveaux o) {
				ps.removeChildren(o);
				ouvertures.update();
			}
		});

		ouvertures.setEditObject(() -> {
			Set objects = new HashSet(ps.getOuvertures());
			var selected = config.controller.chooseFromList("ouverture", objects);
			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		Button editParent = new Button("Éditer l'objet parent");
		editParent.setOnAction(eh -> {
			config.controller.state.setSelectedElement(ps.getParents().getFirst());
		});

		super.prependResetFunction(() -> {
			prixPS.setText(String.valueOf(ps.calculerPrix()));
			airePS.setText(String.valueOf(ps.aire()));

			notReady.setVisible(! ps.ready());
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(new HBox(
					new WrapLabel("Prix :"),
					prixPS,
					new WrapLabel("€")
				),

				new HBox(
					new WrapLabel("Aire :"),
					airePS
				),

				notReady,

				new Separator(),
				revetements,

				new Separator(),
				ouvertures,

				new Separator(),
				editParent
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