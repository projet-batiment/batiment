package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.PointComponent;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import javafx.scene.layout.Pane;

public class PointEditor extends Editor {
	public PointEditor(Config config, Point point) {
		super(config, "Point");

		super.prependInitFunction((Pane pane) ->
			pane.getChildren().add(
				new PointComponent(config, this, point, "Coordonnées :")
			)
		);

		super.initialize();
	}
}