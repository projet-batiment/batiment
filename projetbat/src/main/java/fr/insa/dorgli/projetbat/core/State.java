package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.*;

import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none

		CREATE_MUR,
	}

	private final Config config;

	public Batiment currentBatiment;
	public Niveau currentNiveau;

	public Drawable viewRootElement;
	public HashSet<Drawable> viewSelectedElements;
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
			default -> {
				actionState = ActionState.DEFAULT;
				config.tui.debug("reset to DEFAULT");
			}
		}

		config.tui.popWhere();
	}

	public Creator getCreator() {
		return creator;
	}

	public void endCreation() {
		endCreation(ActionState.DEFAULT);
	}
	public void endCreation(ActionState newState) {
		this.creator = null;
		config.tui.debug("endCreation: cleared creator, setting actionState " + newState);
		setActionState(newState);
	}
}