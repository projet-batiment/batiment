package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import javafx.scene.control.Button;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DevisEditor extends NameDescEditor {
	public DevisEditor(Config config, Devis devis) {
		super(config, "Devis", devis);

		VBox ourVBox = new VBox();

		if (devis.getStudiedObject().size() == 1) {
			ourVBox.getChildren().add(
				new WrapLabel("Devis de : " + ((FancyToStrings) devis.getStudiedObject().iterator().next()).toStringShort())
			);
		} else if (devis.getStudiedObject().size() > 1) {
			VBox studiedObjectsVBox = new VBox();
			devis.getStudiedObject().stream()
				.forEach((HasPrice each) -> {
					Label eachLabel = new Label( ((FancyToStrings)each).toStringShort() );
					studiedObjectsVBox.getChildren().add(eachLabel);
				});

			ourVBox.getChildren().addAll(
				new WrapLabel("Devis des objects :"),
				studiedObjectsVBox
			);
		} else {
			ourVBox.getChildren().add(
				new WrapLabel("Ce devis n'est lié à aucun objet !")
			);
		}

		Button delete = new Button("Supprimer ce devis");
		delete.setOnAction(eh -> config.controller.state.removeDevis(devis));

		super.prependInitFunction((Pane pane) -> {
			pane.getChildren().addAll(
				ourVBox,
				new WrapLabel("Prix : " + (Math.round(devis.calculerPrix() * 100) / 100) + "€"),
				delete
			);
		});

		super.initialize();
	}
}
