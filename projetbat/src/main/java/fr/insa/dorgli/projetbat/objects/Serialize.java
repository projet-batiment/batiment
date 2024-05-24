package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SequencedCollection;
import java.util.stream.Collectors;

public class Serialize {
	public final Config config;
	private final BufferedWriter writer;
	private final ArrayList<String> sections;

	private int ioErrorCounter = 0;

	public Serialize(File file, Config config) throws IOException {
		this.config = config;
		this.writer = new BufferedWriter(new FileWriter(file));
		this.sections = new ArrayList<>();
	}

	private void line(String line) {
		try {
			writer.write(line);
			writer.newLine();
			config.tui.debug("serialize: wrote line '" + line + "'");
		} catch (IOException ex) {
			ioErrorCounter++;
			config.tui.warn("serialize: IOException occured when writing line '" + line + "'");
		}
	}

	/**
	 * adds a new section statement and updates section path
	 * @param section
	 */
	public void section(String section) {
		sections.add(section);
		line(section);
	}

	/**
	 * adds a new OBJECTS section and updates section path
	 * @param objectsType
	 */
	public void objectsSection(String objectsType) {
		section("OBJECTS:" + objectsType);
	}

	/**
	 * adds a new INNER section and updates section path
	 * @param objectsType
	 */
	public void innerSection(String objectsType) {
		section("INNER:" + objectsType);
	}

	/**
	 * adds a new PROP line
	 * @param propName
	 */
	public void prop(String propName) {
		line("PROP:" + propName);
	}

	/**
	 * adds an EOS statement according to the current section path and updates section path
	 */
	public void eos() {
		if (sections.isEmpty())
			throw new IllegalAccessError("Serializer: no open section to be closed!");

		String last = sections.removeLast();
		line("EOS:" + last);
	}

	/**
	 * adds an EOS:Entry statement according to the current section path
	 */
	public void eoEntry() {
		line("EOS:Entry");
	}

	/**
	 * adds a line composed of CSV-combined escaped strings
	 * @param values any SequencedCollection of Strings
	 */
	private void csvFromStrings(SequencedCollection<String> values) {
		config.tui.debug("serialize: csv: ");
		for (String each: values) {
			config.tui.debug(each);
		}
		line(values.stream()
		    .map(each -> EscapeStrings.escapeString(each))
		    .collect(Collectors.joining(","))
		);
	}

	/**
	 * adds a line composed of the CSV-combined escaped strings of the given objects
	 * @param values 3-dot Object array
	 */
	public void csv(Object... values) {
		csv(Arrays.asList(values));
	}

	/**
	 * adds a line composed of the CSV-combined escaped strings of the given objects
	 * @param values 3-dot Object array
	 */
	public void csv(SequencedCollection<Object> values) {
		line(values.stream()
		    .map(each -> EscapeStrings.escapeString(String.valueOf(each)) )
		    .collect(Collectors.joining(","))
		);
	}

	public int getIoErrorCounter() {
		return ioErrorCounter;
	}
}