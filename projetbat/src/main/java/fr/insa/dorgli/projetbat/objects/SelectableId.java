package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.objects.concrete.Objects;
import fr.insa.dorgli.projetbat.utils.FancyToStrings;

public abstract class SelectableId extends FancyToStrings {
	private int id;

	public SelectableId(int id) {
		this.id = id;
	}

	public SelectableId() {
		this.id = -1;
	}

	public int getId() {
		return id;
	};

	public void setId(int id) throws IllegalArgumentException {
		if (this.id == -1) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("Already got an id: " + this.id + " != -1 (" + id + ")");
		}
	};

	public abstract String serialize(Objects objects);

	@Override
	public String toStringShort() {
		return this.getClass().getSimpleName() + "#" + id;
	}
}
