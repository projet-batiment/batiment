package fr.insa.dorgli.projetbat.utils;

public abstract class FancyToStrings {
	public abstract String toStringShort();
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
}
