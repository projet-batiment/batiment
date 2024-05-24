package fr.insa.dorgli.projetbat.utils;

import java.util.Collection;

public class StructuredToString {
	protected final int depth;
	private String indent;
	private String out;

	public StructuredToString(String title) {
		this(0, title, true);
	}

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

	public static class OfFancyToStrings extends StructuredToString {
		public OfFancyToStrings(String title) {
			super(title);
		}

		public OfFancyToStrings(int depth, String title) {
			super(depth, title);
		}

		public OfFancyToStrings(int depth, String title, boolean indentFirst) {
			super(depth, title, indentFirst);
		}

		public OfFancyToStrings(FancyToStrings object) {
			super(object.toStringShort());
		}

		public OfFancyToStrings(int depth, FancyToStrings object) {
			super(depth, object.toStringShort());
		}

		public OfFancyToStrings(int depth, FancyToStrings object, boolean indentFirst) {
			super(depth, object.toStringShort(), indentFirst);
		}

		public OfFancyToStrings field(String name, FancyToStrings value) {
			String text;
			if (value == null)
				text = "(null)";
			else
				text = value.toString(depth + 1);

			return (OfFancyToStrings) super.field(name, text);
		}

		@Override
		public OfFancyToStrings field(String name, String value) {
			return (OfFancyToStrings) super.field(name, value);
		}

		@Override
		public OfFancyToStrings textField(String name, String value) {
			return (OfFancyToStrings) super.textField(name, value);
		}

		public OfFancyToStrings fieldShortCollection(String name, Collection<FancyToStrings> arr) {
			String value;

			if (arr == null) {
				value = "(null)";
			} else {
				value = arr.size() + " [ ";
				for (FancyToStrings object: arr) {
					value += object.toStringShort() + ", ";
				}
				value += " ]";
			}

			super.field(name, value);
			return this;
		}

		public OfFancyToStrings field(String name, Collection<FancyToStrings> arr) {
			String value = arr.size() + " [ \n";
			for (FancyToStrings object: arr) {
				value += object.toString(super.depth + 2, true) + ",\n";
			}
			value += super.getIndent() + "    ]";

			field(name, value);
			return this;
		}
	}
}
