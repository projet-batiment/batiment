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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

	public void openFile() {
		config.tui.diveWhere("controller:openFile");

		if (config.project instanceof Project project && ! project.objects.getAll().isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Ouvrir un fichier");
			alert.setHeaderText("Ouvrir un fichier fermera le projet actuel sans le sauvegarder.");
			alert.setContentText("Continuer quand même ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() != ButtonType.OK)
				return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrir un fichier...");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichier BATM", "*.batm"));

		File saveFile = fileChooser.showOpenDialog(config.getMainStage());
		if (saveFile != null) {
			Deserialize.Result result = new Deserialize(config).deserializeFile(saveFile);
			switch (result.status) {
				case SUCCESS -> {
					config.project = result.project;
					config.project.file = saveFile;
					config.tui.log("successfully loaded the project!");

					state.setViewRootElement(result.project.viewRootElement);
					state.setCurrentBatiment(result.project.currentBatiment);

					redrawCanvasFit();
					updateBottomBar();
					updateSidePaneSelection();
				}

				case FILE_NOT_FOUND -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fichier introuvable");
					alert.setHeaderText("Le fichier '" + saveFile.getName() + "' n'existe pas !");
					alert.setContentText(saveFile.getPath());

					alert.showAndWait();
				}

				case FILE_TOO_OLD -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fichier incompatible");
					alert.setHeaderText("Le fichier '" + saveFile.getName() + "' utilise un format trop ancien !");
					alert.setContentText("C'est un problème... Essayez de réinstaller une version plus ancienne de " + Config.applicationName + ".\n(" + result.project.loadedVersion + " < " + Config.minimumSavefileVersion + ")");

					alert.showAndWait();
				}
				case FILE_TOO_RECENT -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fichier incompatible");
					alert.setHeaderText("Le fichier '" + saveFile.getName() + "' utilise un format trop récent !");
					alert.setContentText("Mettez " + Config.applicationName + " à jour pour utiliser ce fichier.\n(" + result.project.loadedVersion + " > " + Config.maximumSavefileVersion + ")");

					alert.showAndWait();
				}

				case PARSE_ERROR -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de lecture");
					alert.setHeaderText("Le fichier de sauvegarde spécifié est erroné !");

					String errorMessages = "Le fichier n'a pas pu être lu pour les " + result.messages.length + " raisons suivantes :";
					for (int i = 0; i < result.messages.length; i++) {
						errorMessages += "\n" + (i+1) + ") " + result.messages[i];
					}
					alert.setContentText(errorMessages);

					alert.showAndWait();
				}

				case UNEXPECTED_ERROR -> {
					result.exception.printStackTrace(System.out);

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur inattendue");
					alert.setHeaderText("Une erreur inattendue est survennue lors de la lecture du fichier :");
					alert.setContentText(result.exception.getMessage());

					alert.showAndWait();
				}
			}

			if (config.tui.getErrCounter() > 0) {
				config.tui.error("deserialization failed");
				config.tui.clearErrCounter();
			}
		} else {
			config.tui.log("aucun fichier de sauvegarde spécifié: lecture annulée");
		}

		config.tui.popWhere();
	}

	private void saveFile(File saveFile) {
		if (saveFile == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Enregistrer le projet sous...");
			fileChooser.setInitialFileName(config.project.getNom() + ".batm");
			fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichier BATM", "*.batm"));

			saveFile = fileChooser.showSaveDialog(config.getMainStage());
		}

		if (saveFile != null) {
			try {
				Serialize serializer = new Serialize(saveFile, config);
				config.project.serialize(serializer);
				serializer.end();

				if (serializer.getIoErrorCounter() > 0) {
					config.tui.error(serializer.getIoErrorCounter() + " IOExceptions were encountered during the serialization");

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur d'écriture");
					alert.setHeaderText("Le fichier n'a pas pu être écrit correctement.");
					alert.showAndWait();
				} else {
					config.tui.log("saveFileAs: sauvegarde terminée avec succès");
					config.project.file = saveFile;
				}
			} catch (IOException ex) {
				config.tui.error("failed to create serializer: " + ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace(System.out);

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur inattendue");
				alert.setHeaderText("Une erreur inattendue est survennue lors de la sauvegarde :");
				alert.setContentText(ex.getMessage());

				alert.showAndWait();
			}
		}
	}

	public void saveFile() {
		saveFile(config.project.file);
	}

	public void saveFileAs() {
		saveFile(null);
	}

	public void newProject() {
		if (config.project instanceof Project project && ! project.objects.getAll().isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Nouveau projet");
			alert.setHeaderText("Créer un nouveau projet fermera le projet actuel sans le sauvegarder.");
			alert.setContentText("Continuer quand même ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() != ButtonType.OK)
				return;
		}

		config.project = new Project();
		redrawCanvasFit();
		updateBottomBar();
		updateSidePaneSelection();
	}

	public void moveCanvasView(Direction direction) {
		config.getMainWindow().getCanvasContainer().moveView(direction);
	}

	public void canvasMouseClicked(MouseEvent event, Point2D.Double mousePositionData) {
		canvasMouseClicked(event, mousePositionData, state.getActionState());
	}
	public void canvasMouseClicked(MouseEvent event, Point2D.Double mousePositionData, State.ActionState actionState) {
		config.tui.log("controller: a click occurred in the canvasContainer at (" + mousePositionData.getX() + ":" + mousePositionData.getY() + ")");
		switch (actionState) {
			case CREATE, CREATE_SERIE -> {
				if (state.getViewRootElement() instanceof Niveau currentNiveau) {
					mousePositionData.setLocation(
						Math.floor(mousePositionData.getX() * 1000) / 1000,
						Math.floor(mousePositionData.getY() * 1000) / 1000
					);
					Drawable closestObject = config.getMainWindow().getCanvasContainer().getClosestLinked(30);

					if (state.getCreator().object instanceof Mur newMur) {
						config.tui.diveWhere("controller: click/createMur/" + state.getCreator().step);

						Point point;
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
								currentNiveau.removeChildren(newMur.getPointDebut());
								config.project.objects.put(newMur.getPointFin());
								currentNiveau.removeChildren(newMur.getPointFin());
								config.project.objects.put(newMur);
								currentNiveau.addChildren(newMur);

								config.tui.debug("created mur is set as orphean of " + currentNiveau.toStringShort());


								state.endCreation();
								updateSidePaneSelection();
							}
						}

						redrawCanvas();
						config.tui.popWhere();

					} else if (state.getCreator().object instanceof Point newPoint) {
						config.tui.diveWhere("controller: click/createPoint/" + state.getCreator().step);

						if (closestObject instanceof Point closestPoint) {
							config.tui.debug("reusing existing point " + closestObject);
							newPoint = closestPoint;
						} else {
							newPoint.setX(mousePositionData.getX());
							newPoint.setY(mousePositionData.getY());
							newPoint.setNiveau(currentNiveau);
							config.tui.debug("created point: " + newPoint);

							config.project.objects.put(newPoint);
							currentNiveau.addChildren(newPoint);

							config.tui.debug("created point is set as orphean of " + currentNiveau.toStringShort());
						}

						state.setSelectedElement(newPoint);
						state.endCreation();
						updateSidePaneSelection();

						redrawCanvas();
						config.tui.popWhere();
					}

				} else {
					// TODO
					config.tui.error("unknown viewRootElement type: expecting Niveau, got "
					    + (state.getViewRootElement() == null ? "(null)" : state.getViewRootElement().toStringShort() )
					);
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
						canvasMouseClicked(event, mousePositionData, State.ActionState.MULTI_SELECT);
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
		if ( (state.getActionState() == State.ActionState.CREATE || state.getActionState() == State.ActionState.CREATE_SERIE)
			&& state.getCreator().object instanceof Mur && state.getCreator().step == 1)
			redrawCanvas();
	}

	public void setLogLevel(TUI.LogLevel newLevel) {
		config.tui.setLogLevel(newLevel);
		config.getMainWindow().getCanvasContainer().redraw();
		config.getMainWindow().getBottomBar().update();
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
		config.project.objects.put(devis);
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

	public void menuButtonCreateSerie(BObject object) {
		if (object instanceof Mur mur) {
			state.createSerie(object);
		} else {
			config.tui.error("controller: create serie: unimplemented type: " + object.getClass().getSimpleName());
		}

	}
	public void menuButtonCreateOne(BObject object) {
		switch (object) {
			case Mur mur -> state.createOne(object);
			case Point point -> state.createOne(object);

			case Piece piece -> {
				if (state.getViewRootElement() instanceof Niveau currentNiveau) {
					if (state.getSelectedElements().size() >= 3) {
						boolean allMursAndPoints = true;
						for (SelectableId each: state.getSelectedElements()) {
							if (! (each instanceof Mur || each instanceof Point)) allMursAndPoints = false; 
						}

						if (allMursAndPoints) {
							HashSet<Point> points = new HashSet<>();

							for (SelectableId each: state.getSelectedElements()) {
								if (each instanceof Mur mur) {
									mur.addToPiece(piece);
									points.add(mur.getPointDebut());
									points.add(mur.getPointFin());
								} else if (each instanceof Point point) {
									points.add(point);
									currentNiveau.removeChildren(point);
								}
							}

							piece.setPoints(new ArrayList(points));

							config.project.objects.put(piece);
							currentNiveau.addChildren(piece);
							config.tui.debug("created piece added to " + currentNiveau.toStringShort());

							state.setSelectedElement(piece);
							redrawCanvas();
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

			case Appart appart -> {
				if (state.getViewRootElement() instanceof Niveau currentNiveau) {
					if (state.getSelectedElements().size() >= 1) {
						boolean allPieces = true;
						for (SelectableId each: state.getSelectedElements()) {
							if (! (each instanceof Piece)) allPieces = false; 
						}

						if (allPieces) {
							HashSet<Piece> pieces = new HashSet<>();

							for (SelectableId each: state.getSelectedElements()) {
								if (each instanceof Piece piece)
									pieces.add(piece);
							}

							appart.setPieces(new ArrayList(pieces));

							config.project.objects.put(appart);
							currentNiveau.addChildren(appart);
							state.getCurrentBatiment().addChildren(appart);
							config.tui.debug("created appart added to " + currentNiveau.toStringShort() + " and " + state.getCurrentBatiment());

							state.setSelectedElement(appart);
							redrawCanvas();
						} else {
							config.tui.error("controller/create: pour créer un appartement, il ne faut sélectionner QUE des PIECES");
							new Alert(AlertType.INFORMATION, "Pour créer un appartement, il ne faut sélectionner que des pièces.").showAndWait();
						}
					} else {
						config.tui.error("controller/create: pour créer une pièce, il faut sélectionner AU MOINS 3 murs ou points");
						new Alert(AlertType.INFORMATION, "Pour créer un appartement, il faut sélectionner au moins 1 pièce.").showAndWait();
					}
				} else {
					// TODO
					config.tui.error("unknown viewRootElement type: expecting Niveau, got "
					    + (state.getViewRootElement() == null ? "(null)" : state.getViewRootElement().toStringShort() )
					);
					new Alert(AlertType.INFORMATION, "Veuillez afficher un niveau dans le plan sur lequel créer un appartement.").showAndWait();
				}
			}

			default -> {
				if (object instanceof Niveau niveau) {
					if (state.getCurrentBatiment() == null) {
						new Alert(AlertType.INFORMATION, "Veuillez sélectionner un bâtiment pour lequel créer un niveau.").showAndWait();
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
			new Alert(AlertType.INFORMATION, "Il n'y a pas de " + itemsName + " à sélectionner...").showAndWait();
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

	public void refreshUI() {
		redrawCanvas();
		updateSidePaneSelection();
		updateBottomBar();
	}
	public void redrawCanvas() {
		config.getMainWindow().getCanvasContainer().redraw();
	}
	public void redrawCanvasFit() {
		config.getMainWindow().getCanvasContainer().moveView(Direction.FIT);
	}
	public void updateSidePaneSelection() {
		config.getMainWindow().getSidePane().updateSelection();
	}
	public void updateBottomBar() {
		config.getMainWindow().getBottomBar().update();
	}
}