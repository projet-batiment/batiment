package fr.insa.dorgli.projetbat;

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
					mainPane.getCanvasContainer().redraw();
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

	public void zoomIn() {
		CanvasContainer canvas = mainPane.getCanvasContainer();
		canvas.zoomScale(0.5);
		canvas.redraw();
	}

	public void zoomOut() {
		CanvasContainer canvas = mainPane.getCanvasContainer();
		canvas.zoomScale(2);
		canvas.redraw();
	}

//	public void zoomOut() {
//		mainPane.setZoneModelVue(mainPane.getZoneModelVue().scale(2));
//		mainPane.redrawAll();
//	}
//
//	public void zoomFit() {
//		mainPane.fitAll();
//		mainPane.getCanvasContainer().redraw();
//	}
}