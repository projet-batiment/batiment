package fr.insa.dorgli.projetbat;

import fr.insa.dorgli.projetbat.gui.Direction;
import fr.insa.dorgli.projetbat.gui.Drawable;
import fr.insa.dorgli.projetbat.gui.MainPane;
import fr.insa.dorgli.projetbat.objects.Niveau;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class Controller {
	private enum ActionState {
		DEFAULT, // none

		SELECT,
		EDIT_OBJECT,

		CREATE_VISUALLY, // with the mouse & on the canvas
	}

	private MainPane mainPane;
	private Config config;

	private ActionState actionState;

	public Controller(Config config, MainPane mainPane) {
		this.config = config;
		this.mainPane = mainPane;
		this.actionState = ActionState.DEFAULT;
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
						mainPane.getCanvasContainer().getDrawingContext().setRootObject(currentNiveau);
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

	public void moveCanvasView(Direction direction) {
		mainPane.getCanvasContainer().moveView(direction);
	}

	public void canvasClicked(MouseEvent event) {
		config.tui.log("controller: a click occurred in the canvasContainer at (" + event.getX() + ":" + event.getY() + ")");
		if (actionState == ActionState.CREATE_VISUALLY) {
			config.tui.log("controller: TODO: clicked canvas in CREATE_VISUALLY mode");
		} else {
			Drawable closestObject = config.getMainPane().getCanvasContainer().getClosestLinked(event.getX(), event.getY());
			if (closestObject == null) {
				config.tui.log("controller: no object to be focused");
			} else {
				config.tui.log("controller: focusing object " + closestObject.getId() + " now: " + closestObject.toString());
				config.getMainPane().getCanvasContainer().getDrawingContext().setSelectedObject(closestObject);
				config.getMainPane().getCanvasContainer().redraw();
			}
		}
	}
}