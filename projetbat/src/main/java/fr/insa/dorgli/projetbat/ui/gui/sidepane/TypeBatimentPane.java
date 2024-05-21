package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeBatiment;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class TypeBatimentPane extends SidePane {
	TypeBatiment typeBatiment;
	Config config;

	private class TypeBatimentEditor extends NameDescEditor {
		public TypeBatimentEditor(Config config) {
			super(config, "TypeBatiment", typeBatiment);

                }
        }

	@Override
	public final void update() {
		TypeBatimentEditor editThis = new TypeBatimentEditor(config);
		editThis.initialize();
		super.addTab(editThis);
	}

	public TypeBatimentPane(Config config, TypeBatiment typeBatiment) {
		this.config = config;
		this.typeBatiment = typeBatiment;

		update();
	}
}