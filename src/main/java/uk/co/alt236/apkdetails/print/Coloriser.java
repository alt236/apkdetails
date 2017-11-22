package uk.co.alt236.apkdetails.print;


public final class Coloriser {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static String error(final String input) {
        return colorise(ANSI_RED, input);
    }

    public static String error(final int input) {
        return error(String.valueOf(input));
    }

    public static String error(final long input) {
        return error(String.valueOf(input));
    }

    public static String error(final boolean input) {
        return error(String.valueOf(input));
    }

    private static String colorise(final String color, final String text) {
        if (isWindows()) {
            return text;
        } else {
            return color + text + ANSI_RESET;
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
