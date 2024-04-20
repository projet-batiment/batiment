package fr.insa.dorgli.projetbat;

// Terminal User Interface
// gère l'interface utilisateur dans le terminal
// pour l'instant, ne s'occupe que du logging et des niveaux de verbosité
// à terme : intégrer ce qui est réellement utile de Lire dans cette classe, de façon plus propre ? 
public class TUI {
	public enum LogLevel {
		QUIET,
		NORMAL,
		LOG,
		DEBUG,
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

	private LogLevel logLevel = LogLevel.NORMAL;
	private int errCounter = 0;

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

			case NORMAL:
			default:
				return 0;
		}
	}

	private boolean logLevelGreaterOrEqual(LogLevel compareLevel) {
		return logLevelValue(compareLevel) <= logLevelValue(logLevel);
	}

	public void println(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.QUIET))
			System.out.println(msg);
	}

	public void error(String msg) {
		errCounter ++;
		if (logLevelGreaterOrEqual(LogLevel.NORMAL))
			System.err.println(red("ERR: ") + msg);
	}
	public void error(String where, String msg) {
		error(where + ": " + msg);
	}

	public void log(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.LOG))
			System.out.println(yellow("LOG: ") + msg);
	}
	public void log(String where, String msg) {
		log(where + ": " + msg);
	}

	public void debug(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.DEBUG))
			System.err.println(green("DBG: ") + msg);
	}
	public void debug(String where, String msg) {
		debug(where + ": " + msg);
	}
	public void begin(String where) {
		debug(blue("=== BEGIN === ") + where);
	}
	public void ended(String where) {
		debug(blue("=== ENDED === ") + where);
	}
}