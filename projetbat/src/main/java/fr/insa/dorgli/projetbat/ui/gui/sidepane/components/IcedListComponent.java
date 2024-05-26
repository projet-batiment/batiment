package fr.insa.dorgli.projetbat.ui.gui.sidepane.components;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.Name;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.Editor;
import java.util.Collection;
import javafx.scene.layout.VBox;

// IcedList => ne fait qu'afficher la liste qu'il reçoit
public class IcedListComponent extends VBox {
	private final Collection<SelectableId> objects;
	private final VBox items;

	public final void update() {
		items.getChildren().clear();

		final int maxNameLength = 23;
		if (objects.isEmpty()) {
			items.getChildren().add(new WrapLabel("(aucun composant)"));
		} else {
			for (SelectableId each: objects) {
				String eachName;
				if (each instanceof Name named)
					eachName = named.getNom();
				else
					eachName = each.toStringShort();

				if (eachName.length() > maxNameLength)
					eachName = eachName.substring(0, maxNameLength - 2) + "...";
				items.getChildren().add(new WrapLabel(eachName));
			}
		}
	};

	public IcedListComponent(Config config, Editor editor, Collection<SelectableId> objects) {
		this(config, editor, objects, null);
	}

	public IcedListComponent(Config config, Editor editor, Collection<SelectableId> objects, String title) {
		this.objects = objects;
		this.items = new VBox();

		if (title != null) {
			super.getChildren().add(
				new WrapLabel(title + " :")
			);
		}

		super.getChildren().add(items);

		update();
	}
}
