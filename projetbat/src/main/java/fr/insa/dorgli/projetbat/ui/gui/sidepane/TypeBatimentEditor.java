package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeBatiment;

public class TypeBatimentEditor extends NameDescEditor {
	public TypeBatimentEditor(Config config, TypeBatiment typeBatiment) {
		super(config, "TypeBatiment", typeBatiment);

		super.initialize();
	}
}