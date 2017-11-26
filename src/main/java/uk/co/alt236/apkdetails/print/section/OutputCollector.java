package uk.co.alt236.apkdetails.print.section;

import uk.co.alt236.apkdetails.print.section.sectionbuilder.KV;
import uk.co.alt236.apkdetails.print.section.sectionbuilder.Section;
import uk.co.alt236.apkdetails.print.section.sectionbuilder.SectionBuilder;
import uk.co.alt236.apkdetails.print.section.values.MultiLineValue;
import uk.co.alt236.apkdetails.print.section.values.SimpleValue;
import uk.co.alt236.apkdetails.print.section.values.Value;

import java.util.Collection;

public class OutputCollector {
    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static String SECTION_LINE = "------------------------------";
    private final StringBuilder sb = new StringBuilder();
    private final SectionBuilder sectionBuilder = new SectionBuilder();
    private final LabelFactory labelFactory = new LabelFactory();
    private int padding = 10;

    public void add(final String value) {
        sb.append(value);
        addNewLine();
    }

    public void add(final int value) {
        add(String.valueOf(value));
    }

    public void addKv(final String label, final boolean value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final String value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final float value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final double value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final long value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final int value) {
        addKv(label, new SimpleValue(value));
    }

    public void addKv(final String label, final Collection<?> value) {
        addKv(label, new MultiLineValue(value));
    }

    public void addKv(final String label, final Value value) {
        if (sectionBuilder.isEnabled()) {
            sectionBuilder.add(new KV<>(label, value));
        } else {
            addKvInternal(padding, label, value);
        }
    }

    private void addKvInternal(final int padding, final String label, final Value value) {
        if (value instanceof SimpleValue) {
            sb.append(labelFactory.getLabel(padding, label));
            sb.append(((SimpleValue) value).getValue());
            addNewLine();
        } else if (value instanceof MultiLineValue) {
            int count = 0;
            for (final SimpleValue line : ((MultiLineValue) value).getValues()) {
                if (count == 0) {
                    addKvInternal(padding, label, line);
                } else {
                    sb.append(labelFactory.getBlankLabel(padding));
                    sb.append(line.getValue());
                    addNewLine();
                }
                count++;
            }
        }

    }

    public void addNewLine() {
        sb.append(NEW_LINE);
    }

    public void addSectionLine() {
        sb.append(SECTION_LINE);
        addNewLine();
    }

    public void addWithValueAsNewLine(final String label, final String value) {
        addKv(label, NEW_LINE + Padding.padRight("", 5) + value);
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }

    public void startKeyValueSection() {
        sectionBuilder.startBuilding();
    }

    public void endKeyValueSection() {
        final Section section = sectionBuilder.build();

        for (final KV<String, Value> entry : section.getEntries()) {
            addKvInternal(
                    section.getSectionPadding(),
                    entry.first,
                    entry.second);
        }
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}