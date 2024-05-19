package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.ui.gui.Direction;
import fr.insa.dorgli.projetbat.objects.*;
import fr.insa.dorgli.projetbat.ui.TUI;
import java.awt.geom.Point2D;
import java.io.File;
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
		this.state = new State(config);
	}

	public void openFile(ActionEvent event) {
		config.tui.diveWhere("controller:openFile");

		config.tui.debug("reading file after ActionEvent: " + event.toString());

		FileChooser fileChooser = new FileChooser();
		File f = fileChooser.showOpenDialog(config.getMainStage());
		if (f != null) {
			Deserialize.Result result = new Deserialize(config).deserializeFile(f);
			switch (result.status) {
				case SUCCESS -> {
					config.project = result.project;
					config.tui.log("successfully loaded the project!");

					// TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
					state.viewRootElement = result.project.firstViewRootElement;

					config.getMainWindow().getCanvasContainer().moveView(Direction.FIT); // implies a redraw
				}

				case FILE_NOT_FOUND -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fichier introuvable");
					alert.setHeaderText("Le fichier indéqué n'existe pas !");
					alert.setContentText(f.toString());

					alert.showAndWait();
				}

				case PARSE_ERROR -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de lecture");
					alert.setHeaderText("Le fichier de sauvegarde spécifié est erroné !");

					String errorMessages = "Le fichier n'a pas pu être lu pour les raisons suivantes :";
					for (int i = 0; i < result.messages.length; i++) {
						errorMessages += "\n" + (i+1) + ") " + result.messages[i];
					}
					alert.setContentText(errorMessages);

					alert.showAndWait();
				}

				case UNEXPECTED_ERROR -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur inattendue");
					alert.setHeaderText("Une erreur inattendue est survennue");
					alert.setContentText(result.exception.getMessage());

					alert.showAndWait();
				}
			}

			if (config.tui.getErrCounter() > 0) {
				config.tui.error("deserialization failed");
				config.tui.clearErrCounter();
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
		config.getMainWindow().getCanvasContainer().moveView(direction);
	}

	public void canvasMouseClicked(MouseEvent event, Point2D.Double mousePositionData) {
		config.tui.log("controller: a click occurred in the canvasContainer at (" + mousePositionData.getX() + ":" + mousePositionData.getY() + ")");
		switch (state.getActionState()) {
			case CREATE_MUR -> {
				Mur newMur = (Mur)state.getCreator().object;
				config.tui.diveWhere("controller: click/createMur/" + state.getCreator().step);

				if (state.viewRootElement instanceof Niveau currentNiveau) {
					Point point;
					Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked();
					if (closestObject instanceof Point closestPoint) {
						point = closestPoint;
					} else {
						point = config.project.objects.createPoint(mousePositionData.getX(), mousePositionData.getY(), currentNiveau);
						config.project.objects.put(point);
					}
					config.tui.log("point is " + point);

					switch(state.getCreator().next()) {
						case 0 -> {
							newMur.setPointDebut(point);

							config.project.objects.put(newMur);
							config.tui.debug("added mur to the objects: " + newMur.toStringShort());

							state.viewSelectedElements.clear();
							state.viewSelectedElements.add(newMur);
						}
						case 1 -> {
							newMur.setPointFin(point);
							config.tui.debug("created mur: " + newMur);

							currentNiveau.getOrpheans().add(newMur);
							config.tui.debug("created mur is set as orphean of " + currentNiveau);

							state.endCreation();
							config.getMainWindow().getCanvasContainer().redraw();
						}
					}
				} else {
					config.tui.error("unknown viewRootElement type: expecting Niveau, got "
					    + (state.viewRootElement == null ? "(null)" : state.viewRootElement.toStringShort() )
					);
				}

				config.tui.popWhere();
			}

			default -> {
				Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked();
				if (closestObject == null) {
					config.tui.log("controller: no object to be focused");
					state.viewSelectedElements.clear();
					config.getMainWindow().getCanvasContainer().redraw();
				} else {
					config.tui.log("controller: focusing object " + closestObject.getId() + " now: " + closestObject.toString());
					state.viewSelectedElements.clear();
					state.viewSelectedElements.add(closestObject);
					config.getMainWindow().getCanvasContainer().redraw();
				}
				config.getMainWindow().getSidePaneContainer().useObject(closestObject);
			}
		}
	}

	public void canvasMouseMoved(MouseEvent eh, Point2D.Double mousePositionData) {
		switch (state.getActionState()) {
			case State.ActionState.CREATE_MUR -> {
				if (state.getCreator().step == 1) config.getMainWindow().getCanvasContainer().redraw();
			}
		}
	}

	public void setLogLevel(TUI.LogLevel newLevel) {
		config.tui.setLogLevel(newLevel);
		config.getMainWindow().getCanvasContainer().redraw();
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

	public void createMur() {
		state.setActionState(State.ActionState.CREATE_MUR);
	}
}