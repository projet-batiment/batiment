package fr.insa.dorgli.projetbat.ui.gui.sidepane.components;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.Editor;
import java.util.Collection;
import javafx.scene.layout.VBox;

// IcedList => ne fait qu'afficher la liste qu'il reçoit
public class IcedListComponent extends VBox {
	public IcedListComponent(Config config, Editor editor, Collection<SelectableId> objects) {
		this(config, editor, objects, null);
	}

	public IcedListComponent(Config config, Editor editor, Collection<SelectableId> objects, String title) {
		if (title != null) {
			super.getChildren().add(
				new WrapLabel(title)
			);
		}

		final int maxNameLength = 23;
		for (SelectableId each: objects) {
			String eachName;
			if (each instanceof NameDesc nameDesc)
				eachName = nameDesc.getNom();
			else
				eachName = each.toStringShort();

			if (eachName.length() > maxNameLength)
				eachName = eachName.substring(0, maxNameLength - 2) + "...";
			super.getChildren().add(new WrapLabel(eachName));
		}
	}
}
