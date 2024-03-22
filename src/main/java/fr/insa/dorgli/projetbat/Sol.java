package main.java.fr.insa.dorgli.projetbat;

import java.io.*;

/**
réécriture réaméliorée et corrigée par Elio Poletti, STH 2023
version améliorée par Raphaël Schruoffeneger ; MIQ2 INSA Strasbourg 2013 
*/

/**
 * Sol
 */
public class Sol {

    ///// read_line
    ///// read a line as a String (throws IOError)
    //
    // simple: read_line_catch(), s()
    //
    // declarations:
    //   legacy: S() -> read_line_catch
    //   helper: read_line()
    //   actual: read_line(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static String S() {
        return read_line_catch();
    }
    public static String read_line_catch() {
        do {
            try {
                return read_line(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer une chaine de caracteres correcte");
            }
        } while (true);
    }
    public static String read_line() throws IOException {
        return read_line(null);
    }
    public static String read_line(String prompt) throws IOException {
        String out = new String();
        char c;

        if (prompt != null)
            System.out.print(prompt);

        do {
            c = read_char();

            if (c == '\n' || c == '\r')
                return out;

            out += c;
        } while (true);
    }

    ///// read_lines
    ///// read multiple lines as a String (throws IOError or IllegalArgumentException)
    //
    // simple: read_lines_catch(int number)         returns an empty string in case of error
    //
    // declarations:
    //   helper:   read_lines(int number)
    //   helper:   read_lines(int number, String prompt)
    //   helper:   read_lines(int number, String prompt, boolean prompt_index)
    //   helper:   read_lines(String eof)
    //   helper:   read_lines(String eof, String prompt)
    //   helper:   read_lines(String eof, String prompt, boolean prompt_index)
    //   helper:   read_lines(int number, String eof, String prompt)
    //   actual:   read_lines(int number, String eof, String prompt, boolean prompt_index)
    //  warning!
    //   NOT DECLARED:  "" read_lines(int number, String NOT_EOF_BUT_PROMPT) "" !!!
    //
    // arguments:
    //   number: max number of read lines, 0 means has no limit
    //   eof: triggers the end of the input when a line matching it is read, null means no eof-string
    //      note: number and eof can be set in the same time
    //
    //   prompt: a potential prompt to display on each line, null means no prompt
    //   prompt_index: whether to display the line index in each prompt
    //
    //   when prompt_index is true, escapes are made for the following sequences:
    //      &i -> the current line index (1-based)
    //      && -> the &i sequence (escaped)
    //   ex: "line &i&&: " -> "line 1&i: ", "line 2&i: ", ...
    //   several escapes are of course allowed (matching is performed with String.replaceAll)

    // simple/catch
    public static String read_lines_catch(int number) {
        do {
            try {
                return read_lines(number, null, null, true);
            } catch (IOException e) {
                return "";
            }
        } while (true);
    }

    // number-based
    public static String read_lines(int number) throws IOException, IllegalArgumentException {
        return read_lines(number, null, null, true);
    }
    public static String read_lines(int number, String prompt) throws IOException, IllegalArgumentException {
        return read_lines(number, null, prompt, true);
    }
    public static String read_lines(int number, String prompt, boolean prompt_index) throws IOException, IllegalArgumentException {
        return read_lines(number, null, prompt, prompt_index);
    }

    // eof-based
    public static String read_lines(String eof) throws IOException, IllegalArgumentException {
        return read_lines(0, eof, null, true);
    }
    public static String read_lines(String eof, String prompt) throws IOException, IllegalArgumentException {
        return read_lines(0, eof, prompt, true);
    }
    public static String read_lines(String eof, String prompt, boolean prompt_index) throws IOException, IllegalArgumentException {
        return read_lines(0, eof, prompt, prompt_index);
    }

    // prompt_index = false
    public static String read_lines(int number, String eof, String prompt) throws IOException, IllegalArgumentException {
        return read_lines(number, eof, prompt, true);
    }

    // actual function
    public static String read_lines(int number, String eof, String prompt, boolean prompt_index) throws IOException, IllegalArgumentException {
        if (number == 0 && eof == null)
            throw new IllegalArgumentException("No end limit was given to read_lines, expected at least 'number' or 'eof'");

        String out = new String();

        for (int counter = 0; number == 0 || counter < number; counter++) {
            String line = read_line(prompt_index ? prompt.replaceAll("&i", String.valueOf(counter+1)).replaceAll("&&", "&i") : prompt);

            if (eof != null && line.equals(eof))
                return out;

            out += line + '\n';
        }

        return out.substring(0, out.length() - 1); // get rid of the trailing newline
    }

    ///// read_int
    ///// read a line as an int (throws IOError or NumberFormatException)
    //
    // simple: read_int_catch(), i()
    //
    // declarations:
    //   legacy: i() -> read_int_catch
    //   helper: read_int()
    //   actual: read_int(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static int i() {
        return read_int_catch();
    }
    public static int read_int_catch() {
        do {
            try {
                return read_int(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un entier correct");
            }
        } while (true);
    }

    public static int read_int() throws IOException, NumberFormatException {
        return read_int(null);
    }
    public static int read_int(String prompt) throws IOException, NumberFormatException {
        return Integer.parseInt(read_line(prompt));
    }

    ///// read_byte
    ///// read a line as a byte (throws IOError or NumberFormatException)
    //
    // simple: read_byte_catch(), b()
    //
    // declarations:
    //   legacy: b() -> read_byte_catch
    //   helper: read_byte()
    //   actual: read_byte(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static byte b() {
        return read_byte_catch();
    }
    public static byte read_byte_catch() {
        do {
            try {
                return read_byte(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un byte correct");
            }
        } while (true);
    }

    public static byte read_byte() throws IOException, NumberFormatException {
        return read_byte(null);
    }
    public static byte read_byte(String prompt) throws IOException, NumberFormatException {
        return Byte.parseByte(read_line(prompt));
    }

    ///// read_short
    ///// read a line as a short (throws IOError or NumberFormatException)
    //
    // simple: read_short_catch(), s()
    //
    // declarations:
    //   legacy: s() -> read_short_catch
    //   helper: read_short()
    //   actual: read_short(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static short s() {
        return read_short_catch();
    }
    public static short read_short_catch() {
        do {
            try {
                return read_short(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un short correct");
            }
        } while (true);
    }

    public static short read_short() throws IOException, NumberFormatException {
        return read_short(null);
    }
    public static short read_short(String prompt) throws IOException, NumberFormatException {
        return short.parseshort(read_line(prompt));
    }

    ///// read_long
    ///// read a line as a long (throws IOError or NumberFormatException)
    //
    // simple: read_long_catch(), l()
    //
    // declarations:
    //   legacy: l() -> read_long_catch
    //   helper: read_long()
    //   actual: read_long(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static long l() {
        return read_long_catch();
    }
    public static long read_long_catch() {
        do {
            try {
                return read_long(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un long entier correct");
            }
        } while (true);
    }

    public static long read_long() throws IOException, NumberFormatException {
        return read_long(null);
    }
    public static long read_long(String prompt) throws IOException, NumberFormatException {
        return Long.parseLong(read_line(prompt));
    }

    ///// read_float
    ///// read a line as a float (throws IOError or NumberFormatException)
    //
    // simple: read_float_catch(), f()
    //
    // declarations:
    //   legacy: f() -> read_float_catch
    //   helper: read_float()
    //   actual: read_float(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static float f() {
        return read_float_catch();
    }
    public static float read_float_catch() {
        do {
            try {
                return read_float(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un flottant correct");
            }
        } while (true);
    }

    public static float read_float() throws IOException, NumberFormatException {
        return read_float(null);
    }
    public static float read_float(String prompt) throws IOException, NumberFormatException {
        return Float.parseFloat(read_line(prompt));
    }

    ///// read_double
    ///// read a line as a double (throws IOError or NumberFormatException)
    //
    // simple: read_double_catch(), d()
    //
    // declarations:
    //   legacy: d() -> read_double_catch
    //   helper: read_double()
    //   actual: read_double(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static double d() {
        return read_double_catch();
    }
    public static double read_double_catch() {
        do {
            try {
                return read_double(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un flottant double correct");
            }
        } while (true);
    }

    public static double read_double() throws IOException, NumberFormatException {
        return read_double(null);
    }
    public static double read_double(String prompt) throws IOException, NumberFormatException {
        return Double.parseDouble(read_line(prompt));
    }

    ///// read_char
    ///// read a line as a char (throws IOError or NumberFormatException)
    //
    // simple: read_char_catch(), c()
    //
    // declarations:
    //   legacy: c() -> read_char_catch
    //   helper: read_char()
    //   actual: read_char(String prompt)
    //
    // arguments:
    //   prompt: a potential prompt to display on each line, null means no prompt

    public static char c() {
        return read_char_catch();
    }
    public static char read_char_catch() {
        do {
            try {
                return read_char(null);
            } catch (IOException e) {
                System.err.println("Veuillez entrer un caractere correct");
            }
        } while (true);
    }

    public static char read_char() throws IOException, NumberFormatException {
        return read_char(null);
    }
    public static char read_char(String prompt) throws IOException, NumberFormatException {
        return (char)System.in.read();
    }




    ///// tmp main function buffer
    public static void main(String[] args) {
        try {
            System.out.println(read_lines(1, "EOF", "hi&i: ", true));
        } catch (Error e) {
            System.out.println("catched error: " + e.getMessage());
        }
    }
}
