package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Appart;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.Drawable;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.objects.concrete.Piece;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.ButtonnedListComponent;
import javafx.event.ActionEvent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NiveauEditor extends NameDescEditor {
	public NiveauEditor(Config config, Niveau niveau) {
		super(config, "Niveau", niveau);

		TextField hauteur = new TextField();

		WrapLabel prix = new WrapLabel();

		WrapLabel notReady = new WrapLabel("Ce niveau n'est pas prêt.");
		notReady.managedProperty().bind(notReady.visibleProperty());
		notReady.setTextFill(Color.RED);

		ButtonnedListComponent pieces = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) niveau.getPieces(), "Pièces");

		pieces.setRemoveObject(() -> {
			Set objects = new HashSet(niveau.getPieces());

			Alert alert = new Alert(Alert.AlertType.WARNING,
			    "La suppression de pièces n'est pas une action stable. Continuer ?",
			    ButtonType.OK,
			    ButtonType.CANCEL
			);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				Piece selected = (Piece) config.controller.chooseFromList("pièce", objects);

				if (selected != null) {
					niveau.removeChildren(selected);
					pieces.update();
				}
			}
		});

		pieces.setAddObject(() -> {
			new Alert(Alert.AlertType.INFORMATION, "Pour créer une pièce, utilisez le menu 'Créer > Une Pièce'").showAndWait();
		});

		pieces.setEditObject(() -> {
			Set objects = new HashSet(niveau.getPieces());
			Piece selected = (Piece) config.controller.chooseFromList("pièce", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		ButtonnedListComponent apparts = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) niveau.getApparts(), "Appartements");

		apparts.setRemoveObject(() -> {
			Set objects = new HashSet(niveau.getApparts());

			Alert alert = new Alert(Alert.AlertType.WARNING,
			    "La suppression d'appartements n'est pas une action stable. Continuer ?",
			    ButtonType.OK,
			    ButtonType.CANCEL
			);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				Appart selected = (Appart) config.controller.chooseFromList("appartement", objects);

				if (selected != null) {
					niveau.removeChildren(selected);
					apparts.update();
				}
			}
		});

		apparts.setAddObject(() -> {
			new Alert(Alert.AlertType.INFORMATION, "Pour créer un appartement, utilisez le menu \"Créer > Un Appartement\"").showAndWait();
		});

		apparts.setEditObject(() -> {
			Set objects = new HashSet(niveau.getApparts());
			Appart selected = (Appart) config.controller.chooseFromList("appartement", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		ButtonnedListComponent orphans = new ButtonnedListComponent(config, this, (Collection<SelectableId>) (Collection<?>) niveau.getApparts(), "Orphelins");

		orphans.setRemoveObject(() -> {
			Set objects = new HashSet(niveau.getApparts());

			Alert alert = new Alert(Alert.AlertType.WARNING,
			    "La suppression d'orphelins n'est pas une action stable. Continuer ?",
			    ButtonType.OK,
			    ButtonType.CANCEL
			);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				Drawable selected = (Drawable) config.controller.chooseFromList("orphelin", objects);

				if (selected != null) {
					niveau.removeChildren(selected);
					orphans.update();
				}
			}
		});

		orphans.setAddObject(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("C'est bien de vouloir essayer tous les boutons !...");
			alert.setContentText("...mais, vouloir créer un orphelin, c'est contradictoire, non ? Les orphelins sont tous les objets élémentaires créés dans un niveau qui attendent d'être assignés à un parent. Dans les faits, l'appellation \"orphelin\" n'est donc pas adaptée, puisqu'ils n'ont pas encore eu de parent. Qu'importe : pour créer un objet élémentaire, cherchez dans le menu \"Créer\"");
			alert.showAndWait();
		});

		orphans.setEditObject(() -> {
			Set objects = new HashSet(niveau.getApparts());
			Drawable selected = (Drawable) config.controller.chooseFromList("orphelin", objects);

			if (selected != null) {
				config.controller.state.setSelectedElement(selected);
			}
		});

		super.prependSaveFunction((ActionEvent eh) -> {
			try {
				double hauteur_value = Double.parseDouble(hauteur.getText());

				niveau.setHauteur(hauteur_value);
			} catch (NumberFormatException e) {
				config.tui.error("niveauEditor: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		super.prependResetFunction(() -> {
			hauteur.setText(String.valueOf(niveau.getHauteur()));

			pieces.update();
			apparts.update();
			orphans.update();
			prix.setText(String.valueOf(niveau.calculerPrix()));

			notReady.setVisible(! niveau.ready());
		});

		Button editBatiment = new Button("Éditer le bâtiment");
		editBatiment.setOnAction(eh -> {
			Optional<SelectableId> result = niveau.getParents().stream().filter(each -> each instanceof Batiment).findFirst();
			if (result.isPresent()) {
				config.controller.state.setSelectedElement(result.get());
			} else {
				new Alert(Alert.AlertType.INFORMATION, "Ce niveau n'appartient à aucun appartement... Ce n'est pas normal !").showAndWait();
			}
		});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(
				new HBox(
					new WrapLabel("Hauteur :"),
					hauteur
				),

				new HBox(
					new WrapLabel("Prix total :"),
					prix,
					new WrapLabel("€")
				),

				notReady,
				editBatiment,

				new Separator(),
				pieces,

				new Separator(),
				apparts,

				new Separator(),
				orphans
			)
		);

		super.initialize();
	}
}
