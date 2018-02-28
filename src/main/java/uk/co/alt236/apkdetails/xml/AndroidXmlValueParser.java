package uk.co.alt236.apkdetails.xml;

public class AndroidXmlValueParser {

    public long parseLong(final String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int parseInt(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean parseBoolean(final String value) {

        if ("-1".equals(value)) {
            return true;
        } else if ("0".equals(value)) {
            return false;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    public String parseString(String value) {
        return value == null ? "" : value.trim();
    }
}
