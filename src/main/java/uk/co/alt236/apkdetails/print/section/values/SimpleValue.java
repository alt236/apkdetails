package uk.co.alt236.apkdetails.print.section.values;

public class SimpleValue implements Value {
    public String string;

    public SimpleValue(final Object value) {
        string = (String.valueOf(value));
    }

    public SimpleValue(final int value) {
        string = (String.valueOf(value));
    }

    public SimpleValue(final boolean value) {
        string = String.valueOf(value);
    }

    public SimpleValue(final float value) {
        string = String.valueOf(value);
    }

    public SimpleValue(final double value) {
        string = String.valueOf(value);
    }

    public SimpleValue(final long value) {
        string = String.valueOf(value);
    }

    public String getValue() {
        return string;
    }
}
