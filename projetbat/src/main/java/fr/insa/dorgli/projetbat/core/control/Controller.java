package fr.insa.dorgli.projetbat.core.control;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Project;
import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.ui.gui.Direction;
import fr.insa.dorgli.projetbat.objects.*;
import fr.insa.dorgli.projetbat.objects.HasPrice;
import fr.insa.dorgli.projetbat.ui.TUI;
import fr.insa.dorgli.projetbat.ui.gui.popups.ChooseFromList;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
		config.project = new Project();
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
					state.setViewRootElement(result.project.viewRootElement);
					state.setCurrentBatiment(result.project.currentBatiment);

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
		    + "version:" + Config.maximumSavefileVersion + "\n"
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
			case CREATE -> {
				if (state.getCreator().object instanceof Mur newMur) {
					config.tui.diveWhere("controller: click/createMur/" + state.getCreator().step);

					if (state.getViewRootElement() instanceof Niveau currentNiveau) {
						Point point;
						Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked();
						if (closestObject instanceof Point closestPoint) {
							point = closestPoint;
						} else {
							point = new Point(-1, mousePositionData.getX(), mousePositionData.getY(), currentNiveau);
						}
						config.tui.log("point is " + point);

						switch(state.getCreator().next()) {
							case 0 -> {
								newMur.setPointDebut(point);

								Point endPoint = new Point();
								endPoint.getPoint().setLocation(point.getPoint());
								endPoint.setNiveau(currentNiveau);
								newMur.setPointFin(endPoint);

								config.tui.debug("added the new mur and its points to the objects: " + newMur.toStringShort());

								state.setSelectedElement(newMur);
							}
							case 1 -> {
								newMur.setPointFin(point);
								config.tui.debug("created mur: " + newMur);

								config.project.objects.put(newMur.getPointDebut());
								currentNiveau.getOrpheans().remove(newMur.getPointDebut());
								config.project.objects.put(newMur.getPointFin());
								currentNiveau.getOrpheans().remove(newMur.getPointFin());
								config.project.objects.put(newMur);
								currentNiveau.getOrpheans().add(newMur);

								config.tui.debug("created mur is set as orphean of " + currentNiveau.toStringShort());

								state.endCreation();
								config.getMainWindow().getSidePaneContainer().updateSelection();
							}
						}

						redrawCanvas();
					} else {
						// TODO
						config.tui.error("unknown viewRootElement type: expecting Niveau, got "
						    + (state.getViewRootElement() == null ? "(null)" : state.getViewRootElement().toStringShort() )
						);
					}

					config.tui.popWhere();
				}
			}

			case MULTI_SELECT -> {
				Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked();
				if (closestObject != null) {
					config.tui.log("controller: toggling (multi) selected object " + closestObject.toStringShort() + " now");
					state.toggleSelectedElement(closestObject);
					redrawCanvas();
				}
			}

			default -> {
				Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked();
				if (closestObject == null) {
					config.tui.debug("controller: no object to be focused");
					state.clearSelectedElement();
					redrawCanvas();
				} else {
					if (event.isControlDown()) {
						config.tui.debug("controller: ctrl-click: going into multi-select with object " + closestObject.toStringShort()+ " now");
						state.setActionState(State.ActionState.MULTI_SELECT);
						canvasMouseClicked(event, mousePositionData);
					} else {
						config.tui.debug("controller: selecting object " + closestObject.toStringShort()+ " now");
						state.setSelectedElement(closestObject);
						redrawCanvas();
					}
				}
			}
		}
	}

	public void canvasMouseMoved(MouseEvent eh, Point2D.Double mousePositionData) {
		if (state.getActionState() == State.ActionState.CREATE && state.getCreator().object instanceof Mur)
			redrawCanvas();
	}

	public void setLogLevel(TUI.LogLevel newLevel) {
		config.tui.setLogLevel(newLevel);
		config.getMainWindow().getCanvasContainer().redraw();
	}

	public void devisTotal() {
		Devis devis = new Devis(config.project);
		config.project.objects.put(devis);
		config.tui.log("controller: selecting new devis " + devis.toStringShort() + " now");
		state.addDevis(devis);
	}

	public void devisSelection() {
		HashSet<HasPrice> havePrice = (HashSet<HasPrice>) ((HashSet<?>) state.getSelectedElements().stream()
		    .filter((SelectableId each) -> each instanceof HasPrice).collect(Collectors.toSet()) );

		Devis devis = new Devis(havePrice);
		config.tui.log("controller: selecting new devis " + devis.toStringShort() + " now");
		state.addDevis(devis);
	}

	public void saveDevis() {
		config.tui.error("controller: TODO: saveDevis");
	}

	public void menuClickedApropos() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("À propos");
		alert.setHeaderText("À propos : " + Config.applicationName);
		alert.setContentText("Veuillez poser votre question à l'un des créateurs de ce projet, il aura sûrement réponse à votre question !");

		alert.showAndWait();
	}
	public void menuClickedHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Aide");
		alert.setHeaderText("Aide");
		alert.setContentText("Veuillez poser votre question à l'un des créateurs de ce projet, il aura sûrement réponse à votre question !");

		alert.showAndWait();
	}

	public void menuButtonCreate(BObject object) {
		switch (object) {
			case Mur mur -> state.create(object);

			case Piece piece -> {
				if (state.getViewRootElement() instanceof Niveau currentNiveau) {
					if (state.getSelectedElements().size() >= 3) {
						boolean allMurs = true;
						for (SelectableId each: state.getSelectedElements()) {
							if (! (each instanceof Mur || each instanceof Point)) allMurs = false; 
						}

						if (allMurs) {
							HashSet<Mur> murs = new HashSet<>();
							HashSet<Point> points = new HashSet<>();

							for (SelectableId each: state.getSelectedElements()) {
								if (each instanceof Mur mur) {
									murs.add(mur);
									points.add(mur.getPointDebut());
									points.add(mur.getPointFin());
									currentNiveau.getOrpheans().remove(mur);
								} else if (each instanceof Point point) {
									points.add(point);
									currentNiveau.getOrpheans().remove(point);
								}
							}

							piece.setMurs(new ArrayList(murs));
							piece.setPoints(new ArrayList(points));

							config.project.objects.put(piece);
							currentNiveau.getPieces().add(piece);
							config.tui.debug("created piece added to " + currentNiveau.toStringShort());

							state.setSelectedElement(piece);
							config.getMainWindow().getCanvasContainer().redraw();
						} else {
							config.tui.error("controller/create: pour créer une pièce, il ne faut sélectionner QUE des MURS et des POINTS");
							new Alert(AlertType.INFORMATION, "Pour créer une pièce, il ne faut sélectionner que des murs et des points.").showAndWait();
						}
					} else {
						config.tui.error("controller/create: pour créer une pièce, il faut sélectionner AU MOINS 3 murs ou points");
						new Alert(AlertType.INFORMATION, "Pour créer une pièce, il faut sélectionner au moins 3 murs ou points.").showAndWait();
					}
				} else {
					// TODO
					config.tui.error("unknown viewRootElement type: expecting Niveau, got "
					    + (state.getViewRootElement() == null ? "(null)" : state.getViewRootElement().toStringShort() )
					);
				}
			}

			default -> {
				if (object instanceof Niveau niveau) {
					if (state.getCurrentBatiment() == null) {
						new Alert(AlertType.INFORMATION, "Veuillez sélectionner un bâtiment pour lequel créer un niveau").showAndWait();
						return;
					}
					state.setViewRootElement(niveau);
				} else if (object instanceof Batiment batiment) {
					state.setCurrentBatiment(batiment);
				}

				config.project.objects.put(object);
				state.setSelectedElement(object);
//				state.create(object);

				redrawCanvas();
			}
		}
	}

	public void menuButtonMultiSelect(boolean multiselect) {
		state.setActionState(multiselect ? State.ActionState.MULTI_SELECT : State.ActionState.DEFAULT);
	}

	public void selectFromList(String itemsName, Set<SelectableId> list) {
		SelectableId selected = chooseFromList(itemsName, list);
		if (selected != null)
			state.setSelectedElement(selected);
	}
	public SelectableId chooseFromList(String itemsName, Set<SelectableId> list) {
		return chooseFromList(itemsName, null, list);
	}
	public SelectableId chooseFromList(String itemsName, SelectableId defaultItem, Set<SelectableId> list) {
		if (list.isEmpty()) {
			config.tui.error("il n'y a pas de " + itemsName + " à sélectionner");
			new Alert(AlertType.INFORMATION, "Il n'y a pas de " + itemsName + " à sélectionner dans le projet.").showAndWait();
			return null;
		} else {
			ChooseFromList<SelectableId> chooser = new ChooseFromList(defaultItem, list, new Function<SelectableId, String>() {
				@Override
				public String apply(SelectableId object) {
					if (object instanceof NameDesc nameDesc)
						return nameDesc.getNom();
					else
						return object.toStringShort();
				}
			}, "Choisissez un/e " + itemsName);
			return chooser.perform();
		}
	}

	public void menuButtonNiveauToRoot() {
		Set niveaux = config.project.objects.getNiveaux();
		for (SelectableId each: config.project.objects.getAll().values()) {
			if (each instanceof Niveau niveau && state.getCurrentBatiment().getNiveaux().contains(niveau))
				niveaux.add(each);
		}

		DrawableRoot niveau = (DrawableRoot) chooseFromList("niveau", state.getViewRootElement(), niveaux);
		if (niveau != null){
			state.setViewRootElement(niveau);
		} else
			config.tui.log("no selection");
	}

	public void menuButtonChooseBatiment() {
		Set batiments = config.project.objects.getBatiments();
		Batiment batiment = (Batiment) chooseFromList("batiment", state.getCurrentBatiment(), batiments);
		if (batiment != null) {
			state.setCurrentBatiment(batiment);
			new Alert(AlertType.WARNING, "TODO: changer les niveaux et les bâtiments ensemble").showAndWait();
		} else
			config.tui.log("no selection");
	}

	public void menuButtonSelectCurrentNiveau() {
		DrawableRoot niveau = state.getViewRootElement();
		if (niveau != null)
			state.setSelectedElement(niveau);
		else
			new Alert(AlertType.INFORMATION, "Il n'y a pas de niveau actuel").showAndWait();
	}
	public void menuButtonSelectCurrentBatiment() {
		Batiment batiment = state.getCurrentBatiment();
		if (batiment != null)
			state.setSelectedElement(batiment);
		else
			new Alert(AlertType.INFORMATION, "Il n'y a pas de bâtiment actuel").showAndWait();
	}
	public void menuButtonSelectCurrentProject() {
		state.setSelectedElement(config.project);
	}

	public void redrawCanvas() {
		config.getMainWindow().getCanvasContainer().redraw();
	}
}