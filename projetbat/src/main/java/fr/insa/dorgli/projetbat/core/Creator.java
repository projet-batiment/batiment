package fr.insa.dorgli.projetbat.core;

import fr.insa.dorgli.projetbat.objects.BObject;

public class Creator<T extends BObject> {
	public final T object;
	public int step;

	public Creator(T object) {
		this.step = 0;
		this.object = object;
	}

	public int next() {
		return step++;
	}
}
