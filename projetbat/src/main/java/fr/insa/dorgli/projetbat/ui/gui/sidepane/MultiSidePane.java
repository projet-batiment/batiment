package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import java.util.HashSet;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class MultiSidePane extends SidePane {
	HashSet<SelectableId> objects;
	Config config;

	private class MultiSelectEditor extends Tab {
		public MultiSelectEditor(Config config) {
			VBox ourVBox = new VBox();
			super.setContent(ourVBox);

			ourVBox.getChildren().add( new Label(objects.size() + " objects sélectionnés :") );

			for (SelectableId each: objects) {
				ourVBox.getChildren().add( new Label(each.toStringShort()) );
			}
		}
	}

	@Override
	public final void update() {
		super.addTab(new MultiSelectEditor(config));
	}

	public MultiSidePane(Config config, HashSet<SelectableId> objects) {
		this.config = config;
		this.objects = objects;

		update();
	}
}
