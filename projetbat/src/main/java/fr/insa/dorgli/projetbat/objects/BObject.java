package fr.insa.dorgli.projetbat.objects;

import java.util.Arrays;
import java.util.HashSet;

public abstract class BObject extends SelectableId {
	protected final HashSet<SelectableId> parents = new HashSet<>();
	// est ce que l'objet en construction est prêt?
	public abstract boolean ready();

	public BObject(int id) {
		super(id);
	}
	public BObject() { }

	public HashSet<SelectableId> getParents() {
		return parents;
	}

	public final void addParents(SelectableId... objects) {
		parents.addAll(Arrays.asList(objects));
	}
	public final void removeParents(SelectableId... objects) {
		parents.removeAll(Arrays.asList(objects));
	}
}