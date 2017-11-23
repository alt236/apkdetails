package uk.co.alt236.apkdetails.print.section.sectionbuilder;

import uk.co.alt236.apkdetails.print.section.values.Value;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private final List<KV<String, Value>> entries = new ArrayList<>();
    private boolean enabled;

    public void startBuilding() {
        enabled = true;
    }

    public Section build() {
        enabled = false;
        int sectionPadding = 0;

        for (final KV<String, Value> entry : entries) {
            if (sectionPadding < entry.first.length()) {
                sectionPadding = entry.first.length();
            }
        }

        final List<KV<String, Value>> copiedEntries = new ArrayList<>(entries);
        entries.clear();

        return new Section(sectionPadding, copiedEntries);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void add(KV<String, Value> entry) {
        entries.add(entry);
    }
}
