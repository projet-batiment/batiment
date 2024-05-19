package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.*;

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
	private final HashSet<BObject> viewSelectedElements;
	private ActionState actionState;

	private Creator creator;

	public State(Config config) {
		this.config = config;

		this.currentBatiment = null;
		this.currentNiveau = null;

		this.viewRootElement = null;
		this.viewSelectedElements = new HashSet<>();
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

	public HashSet<BObject> getSelectedElements() {
		return viewSelectedElements;
	}
	public boolean isSelectedElement(BObject elm) {
		return viewSelectedElements.contains(elm);
	}
	public void addSelectedElement(BObject elm) {
		viewSelectedElements.add(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void removeSelectedElement(BObject elm) {
		viewSelectedElements.remove(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void setSelectedElement(BObject elm) {
		clearSelectedElement();
		viewSelectedElements.add(elm);
		config.getMainWindow().getSidePaneContainer().update();
	}
	public void clearSelectedElement() {
		viewSelectedElements.clear();
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