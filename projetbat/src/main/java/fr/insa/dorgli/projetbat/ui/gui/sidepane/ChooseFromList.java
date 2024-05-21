package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import javafx.scene.control.ChoiceDialog;

public class ChooseFromList<T> {
	final ChoiceDialog<Entry> dialog;

	private class Entry {
		public final T value;
		private final Function<T, String> generateText;

		public Entry(T value, Function<T,String> generateText) {
			this.value = value;
			this.generateText = generateText;
		}

		@Override
		public String toString() {
			return generateText.apply(value);
		}
	}

	public T perform() {
		dialog.showAndWait();
		return dialog.getSelectedItem().value;
	}

	public ChooseFromList(T defaultObject, Set<T> objects, Function<T,String> generateText) {
		Entry defaultEntry = new Entry(defaultObject, generateText);
		HashSet<Entry> entries = new HashSet<>();

		for (T each: objects) {
			Entry eachEntry =
				each == defaultObject
				? defaultEntry
				: new Entry(each, generateText);
			entries.add(eachEntry);
		}

		dialog = new ChoiceDialog(defaultEntry, entries);
	}
}
