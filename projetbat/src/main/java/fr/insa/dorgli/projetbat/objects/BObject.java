package fr.insa.dorgli.projetbat.objects;

public abstract class BObject {
	private final int id;

	public BObject(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	};

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