package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.PointComponent;
import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import javafx.scene.layout.Pane;

public class PointPane extends SidePane {
	Point point;
	Config config;

	private class PointEditor extends Editor {
		public PointEditor(Config config) {
			super(config, "Point");

			super.prependInitFunction((Pane pane) ->
				pane.getChildren().add(
					new PointComponent(config, this, point, "Coordonnées :")
				)
			);
		}
	}

	@Override
	public final void update() {
		PointEditor editThis = new PointEditor(config);
		editThis.initialize();
		super.addTab(editThis);
	}

	public PointPane(Config config, Point point) {
		this.config = config;
		this.point = point;

		update();
	}
}
