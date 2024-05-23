package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import javafx.scene.control.Button;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class EmptyProject extends SmartTab {
	public EmptyProject(Config config) {
		super(config, "(Project vide)");

		Button createBatiment = new Button("Créer un bâtiment");
		createBatiment.setOnAction(eh -> {
			config.controller.menuButtonCreateOne(new Batiment());
		});

		Button openFile = new Button("Ouvrir un fichier");
		openFile.setOnAction(eh -> {
			config.controller.openFile();
		});

		super.prependInitFunction((Pane pane) -> 
			pane.getChildren().addAll(
				new WrapLabel("Ce projet ne contient aucun élément !"),
				new WrapLabel("Vous pouvez commencer par créer un bâtiment, ou par ouvrir un fichier de sauvegarde."),
				new HBox(
					createBatiment,
					openFile
				)
			)
		);

		super.initialize();
	}
}
