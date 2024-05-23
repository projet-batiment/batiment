package fr.insa.dorgli.projetbat.core.control;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableRoot;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.ui.gui.Direction;

import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none
		MULTI_SELECT,

		CREATE,
	}

	private final Config config;

	private Batiment currentBatiment;
//	public Niveau currentNiveau;
	private DrawableRoot viewRootElement;

	private final HashSet<SelectableId> selectedElements;
	private final HashSet<Devis> devisList;
	private ActionState actionState;

	private Creator creator;

	public State(Config config) {
		this.config = config;

		this.currentBatiment = null;
//		this.currentNiveau = null;

		this.viewRootElement = null;
		this.selectedElements = new HashSet<>();
		this.devisList = new HashSet<>();
		this.actionState = ActionState.DEFAULT;

		this.creator = null;
	}

	public ActionState getActionState() {
		return actionState;
	}

	public void setActionState(ActionState newState) {
		config.tui.diveWhere("state/setActionState");
		config.tui.debug("new state " + newState);

		switch (newState) {
			case CREATE -> {
				config.tui.error("setAction CREATE shouldn't be called directly");
			}

			case MULTI_SELECT, DEFAULT
				-> actionState = newState;

			default -> {
				config.tui.error("unknown state: " + newState + ": fallback to DEFAULT");
				actionState = ActionState.DEFAULT;
			}
		}

		config.getMainWindow().getBottomBar().update();
		config.tui.popWhere();
	}

	private void updatedSelection() {
		config.controller.updateSidePaneSelection();
		config.controller.updateBottomBar();
	}
	private void updatedView() {
		updatedSelection();
		config.getMainWindow().getCanvasContainer().moveView(Direction.FIT); // implies redraw
	}

	public HashSet<SelectableId> getSelectedElements() {
		return selectedElements;
	}
	public boolean isSelectedElement(SelectableId elm) {
		return selectedElements.contains(elm);
	}
	public void toggleSelectedElement(SelectableId elm) {
		if (selectedElements.contains(elm))
			selectedElements.remove(elm);
		else
			selectedElements.add(elm);

		updatedSelection();
	}
	public void removeSelectedElement(SelectableId elm) {
		selectedElements.remove(elm);
		updatedSelection();
	}
	public void setSelectedElement(SelectableId elm) {
		clearSelectedElement();
		selectedElements.add(elm);
		updatedSelection();
	}
	public void clearSelectedElement() {
		selectedElements.clear();
		updatedSelection();
	}

	public void addDevis(Devis devis) {
		devisList.add(devis);
		config.getMainWindow().getSidePane().addDevis(devis);
	}
	public void removeDevis(Devis devis) {
		devisList.remove(devis);
		config.getMainWindow().getSidePane().removeDevis(devis);
	}

	public DrawableRoot getViewRootElement() {
		return viewRootElement;
	}

	public void setViewRootElement(DrawableRoot viewRootElement) {
		this.viewRootElement = viewRootElement;
		updatedView();
	}

	public Batiment getCurrentBatiment() {
		return currentBatiment;
	}

	public void setCurrentBatiment(Batiment currentBatiment) {
		this.currentBatiment = currentBatiment;
		updatedView();
	}


	public <T extends BObject> Creator create(T object) {
		actionState = ActionState.CREATE;
		config.getMainWindow().getBottomBar().update();
		creator = new Creator(object);
		return creator;
	}
	public Creator getCreator() {
		return creator;
	}

	public void endCreation() {
		this.creator = null;
		setActionState(ActionState.DEFAULT);
		config.tui.debug("state/endCreation: cleared creator, set actionState to DEFAULT");
	}
}