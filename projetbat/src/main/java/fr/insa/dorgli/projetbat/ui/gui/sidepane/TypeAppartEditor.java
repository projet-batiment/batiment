package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.types.TypeAppart;

public class TypeAppartEditor extends NameDescEditor {
	public TypeAppartEditor(Config config, TypeAppart typeAppart) {
		super(config, "TypeAppart", typeAppart);

		super.initialize();
	}
}
