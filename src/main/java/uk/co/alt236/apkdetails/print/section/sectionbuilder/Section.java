package uk.co.alt236.apkdetails.print.section.sectionbuilder;

import uk.co.alt236.apkdetails.print.section.values.Value;

import java.util.List;

public class Section {
    private final int sectionPadding;
    private final List<KV<String, Value>> entries;

    public Section(int sectionPadding, List<KV<String, Value>> entries) {
        this.sectionPadding = sectionPadding;
        this.entries = entries;
    }

    public int getSectionPadding() {
        return sectionPadding;
    }

    public List<KV<String, Value>> getEntries() {
        return entries;
    }
}
