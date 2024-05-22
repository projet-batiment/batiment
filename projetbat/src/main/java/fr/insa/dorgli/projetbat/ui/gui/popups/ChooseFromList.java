package fr.insa.dorgli.projetbat.ui.gui.popups;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import javafx.scene.control.ChoiceDialog;

public class ChooseFromList<T> {
	final ChoiceDialog<Entry> dialog;

	private abstract class Entry {
		public final T value;

		private Entry(T value) {
			this.value = value;
		}

		@Override
		public abstract String toString();
	}
	private class IndexedEntry extends Entry {
		public final int index;
		private final Function<T, String> generateText;

		public IndexedEntry(int index, T value, Function<T,String> generateText) {
			super(value);
			this.index = index;
			this.generateText = generateText;
		}

		@Override
		public String toString() {
			String text = generateText.apply(value);
			return (text.isBlank() ? "<vide>" : text);
		}
	}
	private class DefaultEntry extends Entry {
		public DefaultEntry() {
			super(null);
		}

		@Override
		public String toString() {
			return "Sélectionner une option...";
		}
	}


	public T perform() {
		dialog.showAndWait();
		return dialog.getSelectedItem().value;
	}

	public ChooseFromList(T defaultObject, Set<T> objects, Function<T,String> generateText) {
		this(defaultObject, objects, generateText, "Choisir une option :");
	}
	public ChooseFromList(T defaultObject, Set<T> objects, Function<T,String> generateText, String headerText) {
		Entry defaultEntry = new DefaultEntry();
		HashSet<Entry> entries = new HashSet<>();

		ArrayList<T> list = new ArrayList<>(objects);
		for (int i = 0; i < list.size(); i++) {
			T each = list.get(i);
			Entry eachEntry = new IndexedEntry(i + 1, list.get(i), generateText);
			entries.add(eachEntry);

			if (each == defaultObject)
				defaultEntry = eachEntry;
		}

		dialog = new ChoiceDialog(defaultEntry, entries);
		dialog.setTitle("Choisir une option...");
		dialog.setHeaderText(headerText);
	}
}