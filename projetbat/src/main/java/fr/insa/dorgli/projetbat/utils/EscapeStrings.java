package fr.insa.dorgli.projetbat.utils;

public class EscapeStrings {
	// pour plus de flexibilité
	// IMPORTANT!!!
	private static final String ESCAPE_SEQUENCES_HELPER = "&";
	private static final String[] ESCAPE_SEQUENCES_REAL = { ",", "\n", "\r" };
	private static final String[] ESCAPE_SEQUENCES_ESCAPED = { "&c", "&n", "&r" };

	public static String escapeString(String incoming) {
		incoming = incoming.replaceAll(ESCAPE_SEQUENCES_HELPER, ESCAPE_SEQUENCES_HELPER + ESCAPE_SEQUENCES_HELPER);
		for (int i = 0; i < ESCAPE_SEQUENCES_ESCAPED.length; i++)
			incoming = incoming.replaceAll(ESCAPE_SEQUENCES_REAL[i], ESCAPE_SEQUENCES_ESCAPED[i]);

		return incoming;
	}

	public static String unescapeString(String incoming) {
		incoming = incoming.replaceAll(ESCAPE_SEQUENCES_HELPER + ESCAPE_SEQUENCES_HELPER, ESCAPE_SEQUENCES_HELPER);
		for (int i = 0; i < ESCAPE_SEQUENCES_ESCAPED.length; i++)
			incoming = incoming.replaceAll(ESCAPE_SEQUENCES_ESCAPED[i], ESCAPE_SEQUENCES_REAL[i]);

		return incoming;
	}
}
