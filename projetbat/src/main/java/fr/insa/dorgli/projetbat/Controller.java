package fr.insa.dorgli.projetbat;

import fr.insa.dorgli.projetbat.gui.Direction;
import fr.insa.dorgli.projetbat.gui.MainPane;
import fr.insa.dorgli.projetbat.objects.Niveau;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class Controller {
	private MainPane mainPane;
	private Config config;

	public Controller(Config config, MainPane mainPane) {
		this.config = config;
		this.mainPane = mainPane;
	}

	public void openFile(ActionEvent event) {
		config.tui.diveWhere("controller:openFile");

		config.tui.debug("reading file after ActionEvent: " + event.toString());

		FileChooser fileChooser = new FileChooser();
		File f = fileChooser.showOpenDialog(this.mainPane.getMainStage());
		if (f != null) {
			try {
				Deserialize deserializer = new Deserialize(config);
				Project newProject = deserializer.deserializeFile(f);
				if (config.tui.getErrCounter() > 0) {
					config.tui.error("deserialization failed");
					config.tui.clearErrCounter();
				} else {
					config.project = newProject;
					config.tui.log("successfully loaded the project!");
					if (! config.project.objects.niveaux.isEmpty()) {
						// toute l'efficacité de java en une ligne pour avoir le premier niveau :
						Niveau currentNiveau = config.project.objects.niveaux.values().iterator().next();
						mainPane.getCanvasContainer().setCurrentNiveau(currentNiveau);
						config.tui.debug("set the currentNiveau to " + currentNiveau.toString());
					}
					mainPane.getCanvasContainer().moveView(Direction.FIT); // implies a redraw
				}

			} catch (FileNotFoundException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fichier introuvable");
				alert.setHeaderText("Le fichier de sauvegarde n'existe pas !");
				alert.setContentText(ex.getLocalizedMessage());

				alert.showAndWait();

			} catch (Exception ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur de lecture");
				alert.setHeaderText("Une erreur est survenue lors de la lecture du fichier");
				alert.setContentText(ex.getMessage() + "\n\n" + ex.getLocalizedMessage());

				alert.showAndWait();
			}
		}

		config.tui.popWhere();
	}

	public void saveFile(ActionEvent event) {
		config.tui.log("saveFile: echoing the savefile:");

		String out = "FILE\n"
		    + "version:" + config.maximumSavefileVersion + "\n"
		    + "projectName:" + config.project.projectName + "\n"
		    + "projectDescription:" + config.project.projectDescription + "\n"
		    + "EOS:FILE\n\n"
		;

		out += config.project.objects.serialize();
		config.tui.println(out);
	}

	public void moveCanvasView(Direction direction) {
		mainPane.getCanvasContainer().moveView(direction);
	}
}