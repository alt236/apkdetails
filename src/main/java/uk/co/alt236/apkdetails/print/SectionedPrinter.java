package uk.co.alt236.apkdetails.print;

import java.util.ArrayList;
import java.util.List;

public class SectionedPrinter {
    private static final String BULLET_CHARACTER = Character.toString('\u2022');
    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static String SECTION_LINE = "------------------------------";
    private final List<KVEntry<String, String>> keyValueSectionItems = new ArrayList<>();
    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean keyValueSectionEntryEnabled;
    private int padding = 10;

    private static String padRight(final String s, final int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public void add(final String value) {
        stringBuilder.append(value);
        addNewLine();
    }

    public void add(final int value) {
        add(String.valueOf(value));
    }

    public void addKv(final String label, final boolean value) {
        addKv(label, String.valueOf(value));
    }

    public void addKv(final String label, final float value) {
        addKv(label, String.valueOf(value));
    }

    public void addKv(final String label, final long value) {
        addKv(label, String.valueOf(value));
    }

    public void addKv(final String label, final int value) {
        addKv(label, String.valueOf(value));
    }

    public void addKv(final String label, final String value) {
        if (keyValueSectionEntryEnabled) {
            keyValueSectionItems.add(new KVEntry<>(label, value));
        } else {
            addKvInternal(padding, label, value);
        }
    }

    private void addKvInternal(final int padding, final String label, final String value) {
        stringBuilder.append(' ');
        stringBuilder.append(BULLET_CHARACTER);
        stringBuilder.append(padRight(label, padding));
        stringBuilder.append(": ");
        stringBuilder.append(value == null ? "null" : value);
        addNewLine();
    }

    public void addNewLine() {
        stringBuilder.append(NEW_LINE);
    }

    public void addSectionLine() {
        stringBuilder.append(SECTION_LINE);
        addNewLine();
    }

    public void addWithValueAsNewLine(final String label, final String value) {
        addKv(label, NEW_LINE + padRight("", 5) + value);
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }

    public void startKeyValueSection() {
        keyValueSectionEntryEnabled = true;
    }

    public void endKeyValueSection() {
        keyValueSectionEntryEnabled = false;
        int sectionPadding = 0;

        for (final KVEntry<String, String> entry : keyValueSectionItems) {
            if (sectionPadding < entry.first.length()) {
                sectionPadding = entry.first.length();
            }
        }

        for (final KVEntry<String, String> KVEntry : keyValueSectionItems) {
            addKvInternal(sectionPadding, KVEntry.first, KVEntry.second);
        }

        keyValueSectionItems.clear();
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void print() {
        System.out.println(toString());
    }

    private static class KVEntry<K, V> {
        private final K first;
        private final V second;


        private KVEntry(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }
}