package fr.insa.dorgli.projetbat.objects;

import java.util.Arrays;
import java.util.HashSet;

public abstract class BObject extends SelectableId {
	protected final HashSet<BObject> parents = new HashSet<>();
	// est ce que l'objet en construction est prêt?
	public abstract boolean ready();

	public BObject(int id) {
		super(id);
	}
	public BObject() { }

	public HashSet<BObject> getParents() {
		return parents;
	}

	public final void addParents(BObject... objects) {
		parents.addAll(Arrays.asList(objects));
	}
	public final void removeParents(BObject... objects) {
		parents.removeAll(Arrays.asList(objects));
	}
}