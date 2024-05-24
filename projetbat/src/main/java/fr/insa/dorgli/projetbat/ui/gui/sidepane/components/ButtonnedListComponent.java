package fr.insa.dorgli.projetbat.ui.gui.sidepane.components;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.Editor;
import fr.insa.dorgli.projetbat.utils.VoidConsumer;
import java.util.Collection;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonnedListComponent extends IcedListComponent {
	private VoidConsumer addObject;
	private VoidConsumer removeObject;
	private VoidConsumer editObject;

	public void setAddObject(VoidConsumer addObject) {
		this.addObject = addObject;
	}

	public void setRemoveObject(VoidConsumer removeObject) {
		this.removeObject = removeObject;
	}

	public void setEditObject(VoidConsumer editObject) {
		this.editObject = editObject;
	}

	public ButtonnedListComponent(Config config, Editor editor, Collection<SelectableId> objects) {
		this(config, editor, objects, "");
	}

	public ButtonnedListComponent(Config config, Editor editor, Collection<SelectableId> objects, String title) {
		super(config, editor, objects, title);

		Button removeButton = new Button("Enlever");
		removeButton.setOnAction(eh -> removeObject.function());

		Button addButton = new Button("Ajouter");
		addButton.setOnAction(eh -> addObject.function());

		Button editButton = new Button("Éditer");
		editButton.setOnAction(eh -> editObject.function());

		super.getChildren().add(
			new HBox(
				new WrapLabel("Gérer" + (title == null ? "" : " les " + title) + " :"),
				addButton,
				editButton,
				removeButton
			)
		);
	}
}
