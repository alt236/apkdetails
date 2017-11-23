package uk.co.alt236.apkdetails.print.section;

class LabelFactory {
    private static final String PREFIX = " ";
    private static final String BULLET_CHARACTER = Character.toString('\u2022');
    private static final String TITLE_SEPARATOR = ": ";

    public String getLabel(final int padding, final String title) {
        return PREFIX + BULLET_CHARACTER + Padding.padRight(title, padding) + TITLE_SEPARATOR;
    }

    public String getBlankLabel(int padding) {
        return Padding.padRight("", (PREFIX + BULLET_CHARACTER).length() + padding) + TITLE_SEPARATOR;
    }
}
