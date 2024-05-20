package fr.insa.dorgli.projetbat.core.control;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.concrete.Mur;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;
import fr.insa.dorgli.projetbat.objects.concrete.Drawable;
import fr.insa.dorgli.projetbat.objects.SelectableId;

import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none
		MULTI_SELECT,

		CREATE_MUR,
	}

	private final Config config;

	public Batiment currentBatiment;
	public Niveau currentNiveau;

	public Drawable viewRootElement;
	private final HashSet<SelectableId> selectedElements;
	private ActionState actionState;

	private Creator creator;

	public State(Config config) {
		this.config = config;

		this.currentBatiment = null;
		this.currentNiveau = null;

		this.viewRootElement = null;
		this.selectedElements = new HashSet<>();
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
			case CREATE_MUR -> {
				creator = new Creator(new Mur());
				actionState = ActionState.CREATE_MUR;
				config.tui.debug("createMur: initialised mur creation");
			}

			case MULTI_SELECT, DEFAULT
				-> actionState = newState;

			default -> {
				config.tui.error("unknown state: " + newState + ": fallback to DEFAULT");
				actionState = ActionState.DEFAULT;
			}
		}

		config.tui.popWhere();
	}

	public HashSet<SelectableId> getSelectedElements() {
		return selectedElements;
	}
	public boolean isSelectedElement(SelectableId elm) {
		return selectedElements.contains(elm);
	}
	public void addSelectedElement(SelectableId elm) {
		selectedElements.add(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void removeSelectedElement(SelectableId elm) {
		selectedElements.remove(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void setSelectedElement(SelectableId elm) {
		clearSelectedElement();
		selectedElements.add(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void clearSelectedElement() {
		selectedElements.clear();
		config.getMainWindow().getSidePaneContainer().update();
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