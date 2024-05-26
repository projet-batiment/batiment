package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import javafx.scene.control.Button;
import fr.insa.dorgli.projetbat.ui.gui.sidepane.components.WrapLabel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

		Button tutorial = new Button("Mini-tutoriel");
		tutorial.setOnAction(eh -> {
			config.controller.menuClickedTutor();
		});

		WrapLabel noObjects = new WrapLabel("Ce projet ne contient aucun élément !");
		noObjects.setTextFill(Color.RED);

		super.prependInitFunction((Pane pane) -> 
			pane.getChildren().addAll(
				noObjects,
				new WrapLabel("Vous pouvez commencer par créer un bâtiment, ou par ouvrir un fichier de sauvegarde."),
				new WrapLabel("N'oubliez pas de créer les Types des objects que vous créez !"),
				new WrapLabel("Les actions de création, d'édition et d'affichage sont en grande partie disponibles dans le menu en haut."),
				new HBox(
					createBatiment,
					openFile
				),
				tutorial
			)
		);

		super.initialize();
	}
}
