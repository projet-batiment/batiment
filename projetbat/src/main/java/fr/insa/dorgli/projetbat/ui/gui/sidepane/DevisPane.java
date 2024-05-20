package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Devis;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DevisPane extends SidePane {
	Devis devis;
	Config config;

	private class DevisEditor extends Editor {
		public DevisEditor(Config config) {
			super(config, "Devis");

			super.prependInitFunction((Pane pane) ->
				pane.getChildren().addAll(
					new Label("Affichage du devis " + devis.getNom()),
					new Label("TODO"),
					new Label(devis.toString())
				)
			);
		}
	}

	@Override
	public final void update() {
		DevisEditor editThis = new DevisEditor(config);
		editThis.initialize();
		super.addTab(editThis);
	}

	public DevisPane(Config config, Devis devis) {
		this.config = config;
		this.devis = devis;

		update();
	}
}
