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

	private LogLevel logLevel = LogLevel.NORMAL;

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
		if (logLevelGreaterOrEqual(LogLevel.NORMAL))
			System.err.println("ERR: " + msg);
	}

	public void log(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.LOG))
			System.out.println("LOG: " + msg);
	}

	public void debug(String msg) {
		if (logLevelGreaterOrEqual(LogLevel.DEBUG))
			System.err.println("DBG: " + msg);
	}
}