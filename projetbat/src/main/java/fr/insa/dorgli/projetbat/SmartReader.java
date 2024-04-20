package fr.insa.dorgli.projetbat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.FileReader;

public class SmartReader {
	private final LineNumberReader reader;
	private final TUI tui;

	public SmartReader(TUI tui, String path) throws FileNotFoundException {
		this.tui = tui;
		this.reader = new LineNumberReader(new FileReader(path));
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
		final String where = "smartReader/readLine";
		String line;
		do {
			line = reader.readLine();
			if (line == null) {
				tui.debug("smartReader/readLine: reached EOF");
				return new ReadResult(ReadState.EOF, null);
			} else if (line.startsWith("EOS")) {
				String[] splitted = line.split(":", 2);
				if (splitted.length > 1) {
					tui.debug("smartReader/readLine: reached EOS '" + splitted[1] + "'");
					return new ReadResult(ReadState.EOS, splitted[1]);
				} else {
					tui.debug("smartReader/readLine: reached anonymous EOS");
					return new ReadResult(ReadState.EOS, line);
				}
			} else {
				tui.debug(where, "read line '" + line.replaceAll("\r", "\\r").replaceAll("\n", "\\n") + "'");
			}
		} while (line.isBlank());
		tui.debug(where, "returning line '" + line.replaceAll("\r", "\\r").replaceAll("\n", "\\n") + "'");
		return new ReadResult(ReadState.LINE, line);
	}
}