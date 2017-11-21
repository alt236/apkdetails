package uk.co.alt236.apkdetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class KVPrinter {
    private final List<String> keys;
    private final List<String> values;

    public KVPrinter() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void add(String key, long value) {
        keys.add(key);
        values.add(String.valueOf(value));
    }

    public void add(String key, boolean value) {
        keys.add(key);
        values.add(String.valueOf(value));
    }

    public void add(String key, String value) {
        keys.add(key);
        values.add(value);
    }

    public void print() {
        final Optional<String> maxString = keys.stream().max(Comparator.comparingInt(String::length));
        final int maxStringSize = maxString.get().length();

        for (int i = 0; i < keys.size(); i++) {
            final String key = keys.get(i);
            final String value = values.get(i);

            System.out.println(padRight(key, maxStringSize) + ": " + value);
        }
    }

    private String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
}
