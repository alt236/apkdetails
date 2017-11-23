package uk.co.alt236.apkdetails.print.section;

final class Padding {
    public static String padRight(final String s, final int n) {
        return String.format("%1$-" + n + "s", s);
    }
}
