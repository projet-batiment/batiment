package fr.insa.dorgli.projetbat.ui.gui.sidepane.components;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Point;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.Editor;
import javafx.event.ActionEvent;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PointComponent extends VBox {
	public PointComponent(Config config, Editor editor, Point point) {
		this(config, editor, point, null);
	}

	public PointComponent(Config config, Editor editor, Point point, String name) {
		TextField x = new TextField();
		TextField y = new TextField();

		HBox.setHgrow(x, Priority.ALWAYS);
		HBox.setHgrow(y, Priority.ALWAYS);

		editor.prependSaveFunction((ActionEvent eh) -> {
			try {
				double x_value = Double.parseDouble(x.getText());
				double y_value = Double.parseDouble(y.getText());

				point.getPoint().setLocation(x_value, y_value);
			} catch (NumberFormatException e) {
				config.tui.error("pointComponent: validate: failed to parse double numbers: " + e.getMessage());
			}
		});

		editor.prependResetFunction(() -> {
			x.setText(String.valueOf(point.getX()));
			y.setText(String.valueOf(point.getY()));
		});

		if (name != null) {
			super.getChildren().add(
				new WrapLabel(name)
			);
		}

		super.getChildren().add(
			new HBox(x, y)
		);
	}
}
