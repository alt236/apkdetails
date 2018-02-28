package uk.co.alt236.apkdetails.util;


public class Colorizer {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";

    private final boolean enabled;
    private final boolean isWindows;

    public Colorizer(final boolean enabled) {
        this(enabled, isWindows());
    }

    Colorizer(final boolean enabled, final boolean isWindows) {
        this.enabled = enabled;
        this.isWindows = isWindows;
    }

    public String error(final String input) {
        return colorise(ANSI_RED, input);
    }

    public String error(final int input) {
        return error(String.valueOf(input));
    }

    public String error(final long input) {
        return error(String.valueOf(input));
    }

    public String error(final boolean input) {
        return error(String.valueOf(input));
    }

    public String important(final String input) {
        return colorise(ANSI_YELLOW, input);
    }

    public String important(final int input) {
        return important(String.valueOf(input));
    }

    public String important(final long input) {
        return important(String.valueOf(input));
    }

    public String important(final boolean input) {
        return important(String.valueOf(input));
    }

    private String colorise(final String color, final String text) {
        if (!enabled || isWindows) {
            return text;
        } else {
            return color + text + ANSI_RESET;
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
