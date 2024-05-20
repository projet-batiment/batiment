package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Devis;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DevisPane extends SidePane {
	Devis devis;

	@Override
	public final void update() {
		super.editorTab.setContent(
			new VBox(
				new Label("Affichage du devis " + devis.getNom()),
				new Label("TODO"),
				new Label(devis.toString())
			)
		);
	}

	public DevisPane(Config config, Devis devis) {
		this.devis = devis;

		update();
	}
}
