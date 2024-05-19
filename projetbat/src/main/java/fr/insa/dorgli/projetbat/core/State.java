package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.Drawable;
import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none

		// below: create visually, with the mouse on the canvas
		CREATE_MUR_1,
		CREATE_MUR_2,
	}

	public Drawable viewRootElement;
	public HashSet<Drawable> viewSelectedElements;
	public ActionState actionState;

	public State() {
		this.viewRootElement = null;
		this.viewSelectedElements = new HashSet<>();
		this.actionState = ActionState.DEFAULT;
	}
}