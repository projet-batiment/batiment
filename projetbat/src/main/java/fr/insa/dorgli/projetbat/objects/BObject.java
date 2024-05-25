package fr.insa.dorgli.projetbat.objects;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BObject extends SelectableId {
	protected final ArrayList<SelectableId> parents = new ArrayList<>();
	// est ce que l'objet en construction est prêt?
	public abstract boolean ready();

	public BObject(int id) {
		super(id);
	}
	public BObject() { }

	public ArrayList<SelectableId> getParents() {
		return parents;
	}

	public final void addParents(SelectableId... objects) {
		parents.addAll(Arrays.asList(objects));
	}
	public final void removeParents(SelectableId... objects) {
		parents.removeAll(Arrays.asList(objects));
	}
}