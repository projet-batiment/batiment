package fr.insa.dorgli.projetbat;

// Terminal User Interface

import java.util.ArrayList;

// gère l'interface utilisateur dans le terminal
// pour l'instant, ne s'occupe que du logging et des niveaux de verbosité
// à terme : intégrer ce qui est réellement utile de Lire dans cette classe, de façon plus propre ? 
public class TUI {
	public enum LogLevel {
		QUIET,
		NORMAL,
		LOG,
		DEBUG,
		TRACE,
	}

	public TUI(LogLevel level) {
		logLevel = level;
		debug("TUI: initialized TUI instance with loglevel " + logLevel);
	}

	public static String red(String text) {
		return "\033[0;31m" + text + "\033[0m";
	}
	public static String green(String text) {
		return "\033[0;32m" + text + "\033[0m";
	}
	public static String yellow(String text) {
		return "\033[0;33m" + text + "\033[0m";
	}
	public static String blue(String text) {
		return "\033[0;34m" + text + "\033[0m";
	}
	public static String purple(String text) {
		return "\033[0;35m" + text + "\033[0m";
	}
	public static String cyan(String text) {
		return "\033[0;36m" + text + "\033[0m";
	}

	private LogLevel logLevel = LogLevel.NORMAL;
	private int errCounter = 0;
	ArrayList<String> where = new ArrayList<>();

	public void diveWhere(String name) {
		where.add(name);
	}

	public void popWhere() {
		where.removeLast();
	}

	public ArrayList<String> getWhere() {
		return where;
	}

	public int getErrCounter() {
		return errCounter;
	}

	public void clearErrCounter() {
		errCounter = 0;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		log("TUI: logLevel set to " + logLevel);
	}

	public static int logLevelValue(LogLevel level) {
		switch (level) {
			case QUIET:
				return -1;
			case LOG:
				return 1;
			case DEBUG:
				return 2;
			case TRACE:
				return 3;

			case NORMAL:
			default:
				return 0;
		}
	}

	private boolean logLevelGreaterOrEqual(LogLevel compareLevel) {
		return logLevelValue(compareLevel) <= logLevelValue(logLevel);
	}

	public String whereToString() {
		if (where.isEmpty()) {
			return null;
		} else {
			String out;
			if (logLevelGreaterOrEqual(LogLevel.TRACE)) {
				out = where.get(0);
				for (int i = 1; i < where.size(); i++) {
					out += "/" + where.get(i);
				}
			} else {
				out = where.getLast();
			}
			return out;
		}
	}

	public void println(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.QUIET))
			System.out.println(msg);
	}

	private String whereToLog() {
		return blue(whereToString()) + ": ";
	}

	public void error(String msg) {
		errCounter ++;
		if (logLevelGreaterOrEqual(LogLevel.NORMAL))
			System.err.println(red("ERR: ") + whereToLog() + msg);
	}

	public void warn(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.NORMAL))
			System.err.println(red("WRN: ") + whereToLog() + msg);
	}

	public void log(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.LOG))
			System.out.println(yellow("LOG: ") + whereToLog() + msg);
	}

	public void debug(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.DEBUG))
			System.err.println(green("DBG: ") + whereToLog() + msg);
	}
	public void begin() {
		debug(cyan("=== BEGIN === "));
	}
	public void ended(String msg) {
		debug(cyan("=== ENDED === ") + msg);
	}
	public void ended() {
		ended("");
	}
}