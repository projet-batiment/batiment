package fr.insa.dorgli.projetbat.ui.gui;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.NameDesc;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.util.ArrayList;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomBar extends Pane {
	private final Config config;
	private final Label label;

	ArrayList<String> fields;

	/**
	 * checks for empty names
	 * shortens the string if it's too long
	 * 
	 * @param incoming
	 * @return 
	 */
	private String processString(String incoming) {
		if (incoming.isBlank())
			return "(sans nom)";
		else {
			final int maxStringLength = 20;
			if (incoming.length() >= maxStringLength)
				incoming = incoming.substring(0, maxStringLength - 2) + "...";

			return incoming;
		}
	}

	public void update() {
		fields = new ArrayList<>();

		boolean addProjectName = true;

		if (config.controller.state.getCurrentBatiment() instanceof Batiment batiment) {
			fields.add(processString(batiment.getNom()));

			if (config.controller.state.getViewRootElement() instanceof Niveau niveau) {
				fields.add(processString(niveau.getNom()));
			}

			Set<SelectableId> elements = config.controller.state.getSelectedElements();
			if (elements.size() == 1) {
				SelectableId selected = elements.iterator().next();
				String selectedString = "Édit. ";
				if (selected instanceof NameDesc nameDesc)
					selectedString += nameDesc.getNom();
				else
					selectedString += selected.toStringShort();

				fields.add(selectedString);
				addProjectName = false;

			} else if (! elements.isEmpty()) {
				fields.add("Sél. multiple");
				addProjectName = false;
			}
		}

		if (addProjectName) {
			fields.add(0, "Projet : " + processString(config.project.getNom()));
		}

		if (config.tui.logLevelGreaterOrEqual(TUI.LogLevel.LOG))
			fields.add("State : " + config.controller.state.getActionState());

		label.setText(String.join(" > ", fields));
	}

	public BottomBar(Config config) {
		this.config = config;
		this.label = new Label();

		super.getChildren().add(label);
	}
}
