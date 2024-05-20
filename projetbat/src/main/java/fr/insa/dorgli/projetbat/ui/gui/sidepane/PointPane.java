package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PointPane extends SidePane {
	Point point;
	Config config;

	private class Editor extends VBox {
		TextField x;
		TextField y;

		private void reset() {
			x.setText(String.valueOf(point.getX()));
			y.setText(String.valueOf(point.getY()));
		}

		public Editor(Config config) {
			x = new TextField();
			y = new TextField();

			reset();

			HBox.setHgrow(x, Priority.ALWAYS);
			HBox.setHgrow(y, Priority.ALWAYS);

			Button save = new Button("Valider");
			save.setOnAction((ActionEvent eh) -> {
				try {
					double x_value = Double.parseDouble(x.getText());
					double y_value = Double.parseDouble(y.getText());

					point.getPoint().setLocation(x_value, y_value);

					config.getMainWindow().getCanvasContainer().redraw();
				} catch (NumberFormatException e) {
					config.tui.error("pointPane: validate: failed to parse double numbers: " + e.getMessage());
					config.tui.debug(Arrays.toString(e.getStackTrace()));
				}
			});

			Button cancel = new Button("Annuler");
			cancel.setOnAction((ActionEvent eh) -> {
				reset();
			});

			super.getChildren().addAll(
				new VBox(
					new Label("Coordonnées :"),
					new HBox(x, y)
				),

				new HBox(
					save,
					cancel
				)
			);
		}
	}

	@Override
	public final void update() {
		super.editorTab.setContent(new Editor(config));
	}

	public PointPane(Config config, Point point) {
		this.config = config;
		this.point = point;

		update();
	}
}
