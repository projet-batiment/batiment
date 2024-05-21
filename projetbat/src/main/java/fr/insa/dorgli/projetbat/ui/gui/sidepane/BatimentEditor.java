package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;

public class BatimentEditor extends NameDescEditor {
	public BatimentEditor(Config config, Batiment batiment) {
		super(config, "Batiment", batiment);

		super.initialize();
	}
}
