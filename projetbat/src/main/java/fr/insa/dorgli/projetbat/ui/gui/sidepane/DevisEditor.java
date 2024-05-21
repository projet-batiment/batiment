package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DevisEditor extends NameDescEditor {
	public DevisEditor(Config config, Devis devis) {
		super(config, "Devis", devis);

		VBox studiedObjectsVBox = new VBox();
		devis.getStudiedObject().stream()
			.forEach((HasPrice each) -> {
				Label eachLabel = new Label( ((FancyToStrings)each).toStringShort() );
				studiedObjectsVBox.getChildren().add(eachLabel);
			});

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().addAll(new Label("Devis"),
				studiedObjectsVBox
			)
		);
	}
}
