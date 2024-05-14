package fr.insa.dorgli.projetbat.utils;

import fr.insa.dorgli.projetbat.ui.TUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.FileReader;

public class SmartReader {
	private final LineNumberReader reader;
	private final TUI tui;

	public SmartReader(TUI tui, File file) throws FileNotFoundException {
		this.tui = tui;
		this.reader = new LineNumberReader(new FileReader(file));
	}

	public int getLineNumber() {
		return reader.getLineNumber();
	}

	// ReadState
	public enum ReadState {
		LINE,
		EOS,
		EOF
	}

	public class ReadResult {
		private final ReadState state;
		private final String text;

		public ReadResult(ReadState state, String text) {
			this.state = state;
			this.text = text;
		}

		public ReadState getState() { return state; }
		public String getText() { return text; }

		@Override
		public String toString() {
			return "ReadResult { state: " + state + ", text: " + text + " }";
		}
	}

	// readLine
	public ReadResult readLine() throws IOException {
		tui.diveWhere("smartReadLine");
		ReadResult out = null;
		String line;
		do {
			line = reader.readLine();
			if (line == null) {
				tui.debug("reached EOF");
				out = new ReadResult(ReadState.EOF, null);
				break;
			} else if (line.startsWith("EOS")) {
				String[] splitted = line.split(":", 2);
				if (splitted.length > 1) {
					tui.debug("reached EOS '" + splitted[1] + "'");
					out = new ReadResult(ReadState.EOS, splitted[1]);
					break;
				} else {
					tui.debug("reached anonymous EOS");
					out = new ReadResult(ReadState.EOS, line);
					break;
				}
			} else {
				if (line.isBlank())
					tui.debug("read a blank line: reading another line");
				else
					tui.debug("read line '" + line.replaceAll("\r", "\\r").replaceAll("\n", "\\n") + "'");
			}
		} while (line.isBlank());

		if (out == null) {
			out = new ReadResult(ReadState.LINE, line);
		}

		tui.popWhere();
		return out;
	}
}