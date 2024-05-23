package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import java.util.HashSet;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MultiSelectTab extends SmartTab {
	public MultiSelectTab(Config config, HashSet<SelectableId> objects) {
		super(config, "Sélection");

		VBox ourVBox = new VBox();
		for (SelectableId each: objects) {
			ourVBox.getChildren().add( new WrapLabel(each.toStringShort()) );
		}

		super.prependInitFunction((Pane pane) -> 
			pane.getChildren().addAll(
				new WrapLabel(objects.size() + " objects sélectionnés :"),
				ourVBox
			)
		);

		super.initialize();
	}
}