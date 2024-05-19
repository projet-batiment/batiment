package fr.insa.dorgli.projetbat.objects;

public abstract class BObject {
	private int id;

	public BObject(int id) {
		this.id = id;
	}

	public BObject() {
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

	// est ce que l'objet en construction est prêt?
	public abstract boolean ready();

	public abstract String serialize(Objects objects);

	public abstract String toString(int depth, boolean indentFirst);
	@Override
	public String toString() {
		return toString(0);
	}
	public String toString(int depth) {
		return toString(depth, false);
	}
	public String toString(boolean indentFirst) {
		return toString(0, indentFirst);
	}

	public String toStringShort() {
		return this.getClass().getSimpleName() + "#" + id;
	}
}