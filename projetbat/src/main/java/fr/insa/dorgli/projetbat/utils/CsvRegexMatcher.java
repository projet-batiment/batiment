package fr.insa.dorgli.projetbat.utils;

public class CsvRegexMatcher {
	// pour faciliter la lecture des regex ci-dessous
	// normalement, java interprète ces variables comme des constantes de compilation
	// pour la suite, flemme de faire des pseudo-optimisations dans java...
	private static final String REGEX_INT = "[0-9]+";
	private static final String REGEX_DOUBLE = "[0-9]+(\\.[0-9]+)?";
	private static final String REGEX_CSV_STRING = "[^,]*"; // NB: un String peut être vide, d'où le *

	public final String regex;

	/**
	 * @param order regex aliases in the desired order
	 *   NOTE: lowercase = onetime, uppercase = multiple times
	 *   letter   name                     actual regex             notes
	 *   i        REGEX_INT	               [0-9]+
	 *   d 	      REGEX_DOUBLE             [0-9]+(\\.[0-9]+)?       point separator, optionnal
	 *   s        REGEX_CSV_STRING         [^,]*                    empty strings accepted
	 */
	public CsvRegexMatcher(String order) {
		String regexBuffer = new String();

		for (char c: order.toCharArray()) {
			String pattern;

			switch (Character.toLowerCase(c)) {
				case 'i' -> pattern = REGEX_INT;
				case 'd' -> pattern = REGEX_DOUBLE;
				case 's' -> pattern = REGEX_CSV_STRING;

				default -> throw new AssertionError();
			}

			if (Character.isLowerCase(c)) {
				if (! regexBuffer.isEmpty())
					regexBuffer += ",";

				regexBuffer += pattern;
			} else {
				if (regexBuffer.isEmpty())
					regexBuffer += pattern;

				regexBuffer += "(," + pattern + ")*";
			}
		}

		regex = regexBuffer;
	}

	public boolean matches(String value) {
		return value.matches(String.join(",", regex));
	}
}