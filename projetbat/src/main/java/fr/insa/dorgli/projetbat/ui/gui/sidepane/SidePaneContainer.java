package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.types.*;
import java.util.HashSet;
import javafx.scene.layout.Pane;

public class SidePaneContainer extends Pane {
	private final Config config;
	SidePane sidePane;

 	// on pourrait faire avec qch comme sidePane.getObject mais flemme avec les génériques...
	private SelectableId lastSelectedObject;
	private HashSet<SelectableId> lastSelectedList;

	public SidePaneContainer(Config config) {
		this.config = config;
		this.setMaxWidth(200);

		singleObject(null, true);
	}

	public final void update() {
		HashSet<SelectableId> selectedElements = config.controller.state.getSelectedElements();

		if (selectedElements.isEmpty()) {
			singleObject(null);
		} else if (selectedElements.size() == 1) {
			singleObject(selectedElements.iterator().next());
		} else {
			multipleObjects(selectedElements);
		}
	}

	private void multipleObjects(HashSet<SelectableId> objects) {
		if (objects.equals(lastSelectedList) && lastSelectedObject == null) {
			sidePane.update();
			return;
		}

		lastSelectedObject = null;
		lastSelectedList = (HashSet<SelectableId>) objects.clone();

		sidePane = new MultiSidePane(config, objects);
		sidePane.setMaxWidth(this.getMaxWidth());
		this.getChildren().setAll(sidePane);
	}

	private void singleObject(SelectableId object) {
		singleObject(object, false);
	}
	private void singleObject(SelectableId object, boolean forceNew) {
		if (!forceNew && object == lastSelectedObject && lastSelectedList == null) {
			sidePane.update();
			return;
		}

		lastSelectedObject = object;
		lastSelectedList = null;

		switch (object) {
			case Devis devis -> {
				config.tui.debug("useObject: is a Devis");
				sidePane = new DevisPane(config, devis);
			}
			case Piece piece -> {
				config.tui.debug("useObject: is a Piece");
				sidePane = new PiecePane(config, piece);
			}
			case Mur mur -> {
				config.tui.debug("useObject: is a mur");
				sidePane = new MurPane(config, mur);
			}
			case Point point -> {
				config.tui.debug("useObject: is a Point");
				sidePane = new PointPane(config, point);
			}
			case null, default -> {
				config.tui.debug("useObject: sth else...");
				sidePane = new Empty();
			}

		}

		sidePane.setMaxWidth(this.getMaxWidth());
		this.getChildren().setAll(sidePane);
	}
}
