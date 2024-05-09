package fr.insa.dorgli.projetbat;

import fr.insa.dorgli.projetbat.objects.BObject;
import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none

		SELECT,
		EDIT_OBJECT,

		// below: create visually, with the mouse on the canvas
		CREATE_MUR,
		CREATE_PIECE,
	}

	public BObject viewRootElement;
	public HashSet<BObject> viewSelectedElements;
	public ActionState actionState;

	public State() {
		this.viewRootElement = null;
		this.viewSelectedElements = new HashSet<>();
		this.actionState = ActionState.DEFAULT;
	}
}