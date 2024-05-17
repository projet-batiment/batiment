package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.ui.gui.Direction;
import fr.insa.dorgli.projetbat.objects.Drawable;
import fr.insa.dorgli.projetbat.objects.Niveau;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class Controller {
	private final Config config;

	public final State state;

	public Controller(Config config) {
		this.config = config;
		this.state = new State();
	}

	public void openFile(ActionEvent event) {
		config.tui.diveWhere("controller:openFile");

		config.tui.debug("reading file after ActionEvent: " + event.toString());

		FileChooser fileChooser = new FileChooser();
		File f = fileChooser.showOpenDialog(config.getMainStage());
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
						config.getMainPane().getCanvasContainer().getDrawingContext().setRootObject(currentNiveau);
						config.tui.debug("set the currentNiveau to " + currentNiveau.toString());
					}
					config.getMainPane().getCanvasContainer().moveView(Direction.FIT); // implies a redraw
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
				config.tui.debug("debugging an error:");
				ex.printStackTrace(System.out);

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
		config.getMainPane().getCanvasContainer().moveView(direction);
	}

	public void canvasClicked(MouseEvent event) {
		config.tui.log("controller: a click occurred in the canvasContainer at (" + event.getX() + ":" + event.getY() + ")");
		switch (state.actionState) {
			case State.ActionState.CREATE_MUR, State.ActionState.CREATE_PIECE
			    -> config.tui.log("controller: TODO: clicked canvas in CREATE_VISUALLY mode");

			default -> {
				Drawable closestObject = config.getMainPane().getCanvasContainer().getClosestLinked(event.getX(), event.getY());
				if (closestObject == null) {
					config.tui.log("controller: no object to be focused");
					config.getMainPane().getCanvasContainer().getDrawingContext().setSelectedObject(null); // implies redraw
				} else {
					config.tui.log("controller: focusing object " + closestObject.getId() + " now: " + closestObject.toString());
					config.getMainPane().getCanvasContainer().getDrawingContext().setSelectedObject(closestObject); // implies redraw
				}
			}
		}
	}

	public void setLogLevel(TUI.LogLevel newLevel) {
		config.tui.setLogLevel(newLevel);
		config.getMainPane().getCanvasContainer().redraw();
	}

	public void devisTotal() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Devis total");
		alert.setHeaderText("Devis total - " + config.project.projectName);
		alert.setContentText("Prix calculé : " + "TODO!!!");

		alert.showAndWait();
	}

	public void devisFocused() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Devis de l'objet actuel");
		alert.setHeaderText("Devis de l'objet actuel - " + config.project.projectName);
		alert.setContentText("Prix calculé : " + "TODO!!!");

		alert.showAndWait();
	}

	public void saveDevis() {
		config.tui.error("controller: TODO: saveDevis");
	}

	public void menuClickedHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Aide");
		alert.setHeaderText(Config.applicationName);
		alert.setContentText("Veuillez poser votre question à l'un des créateurs de ce projet, il aura sûrement réponse à votre question !");

		alert.showAndWait();
	}
}