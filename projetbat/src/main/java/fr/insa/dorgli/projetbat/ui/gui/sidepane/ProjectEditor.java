package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Project;

public class ProjectEditor extends NameDescEditor {
	public ProjectEditor(Config config, Project project) {
		super(config, "Projet", project);

		super.initialize();
	}
}
