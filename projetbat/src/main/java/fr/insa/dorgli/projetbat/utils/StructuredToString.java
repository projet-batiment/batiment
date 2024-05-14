package fr.insa.dorgli.projetbat.utils;

import fr.insa.dorgli.projetbat.objects.BObject;
import java.util.Collection;

public class StructuredToString {
	protected final int depth;
	private String indent;
	private String out;

	public StructuredToString(int depth, String title) {
		this(depth, title, true);
	}

	public StructuredToString(int depth, String title, boolean indentFirst) {
		this.depth = depth;
		this.indent = new String();
		for (int i = 0; i < depth; i++) {
			this.indent += "    ";
		}

		this.out = (indentFirst ? indent : "") + title + " {";
	}

	private void newLine() {
		out += "\n" + indent;
	}
	protected void newField() {
		newLine();
		out += "    ";
	}

	public StructuredToString field(String name, String value) {
		newField();
		out += name + ": " + value;
		return this;
	}

	public StructuredToString textField(String name, String value) {
		field(name, "'" + value + "'");
		return this;
	}

	public String getValue() {
		return out + "\n" + indent + "}";
	}

	public String getIndent() {
		return indent;
	}

	public static class OfBObject extends StructuredToString {
		public OfBObject(int depth, String title) {
			super(depth, title);
		}

		public OfBObject(int depth, String title, boolean indentFirst) {
			super(depth, title, indentFirst);
		}

		@Override
		public OfBObject field(String name, String value) {
			return (OfBObject) super.field(name, value);
		}

		@Override
		public OfBObject textField(String name, String value) {
			return (OfBObject) super.textField(name, value);
		}

		public OfBObject fieldShortCollection(String name, Collection<BObject> arr) {
			String value = arr.size() + " [ ";
			for (BObject object: arr) {
				value += object.toStringShort() + ", ";
			}
			value += " ]";

			super.field(name, value);
			return this;
		}

		public OfBObject field(String name, Collection<BObject> arr) {
			String value = arr.size() + " [ \n";
			for (BObject object: arr) {
				value += object.toString(super.depth + 2, true) + ",\n";
			}
			value += super.getIndent() + "    ]";

			field(name, value);
			return this;
		}
	}
}
