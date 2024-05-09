package fr.insa.dorgli.projetbat.objects;

import java.util.ArrayList;

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

	/// toString utils

	protected String toStringIndent(int depth) {
		String indent = new String();
		for (int i = 0; i < depth; i++) {
			indent += "    ";
		}
		return indent;
	}

	protected String toStringSections(int depth, String... elements) {
		String indent = toStringIndent(depth);
		return indent + toStringShort() + " {\n"
		    + indent + "    " + String.join(",\n" + indent + "    ", elements)
		    + "\n" + indent + "}";
	}

	protected String toStringField(String name, String field) {
		return name + ": " + field;
	}
	protected String toStringFieldText(String name, String field) {
		return toStringField(name, "'" + field + "'");
	}

	protected String toStringArrayList(ArrayList<BObject> arr) {
		String out = "[ ";
		for (BObject object: arr) {
			out += object.toStringShort() + ", ";
		}
		return out + "]";
	}
}