package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.BObject;
import java.util.HashSet;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MultiSidePane extends SidePane {
	HashSet<BObject> objects;
	Config config;

	private class Editor extends VBox {
		public Editor(Config config) {
			super.getChildren().add( new Label(objects.size() + " objects sélectionnés :") );

			for (BObject each: objects) {
				super.getChildren().add( new Label(each.toStringShort()) );
			}
		}
	}

	@Override
	public final void update() {
		super.editorTab.setContent(new Editor(config));
	}

	public MultiSidePane(Config config, HashSet<BObject> objects) {
		this.config = config;
		this.objects = objects;

		update();
	}
}
