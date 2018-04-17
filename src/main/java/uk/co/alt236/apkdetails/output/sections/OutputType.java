package uk.co.alt236.apkdetails.output.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public enum OutputType {
    ARCHITECTURES,
    FILE_INFO,
    BUILD_CONFIG,
    CONTENT_SIZE,
    DEX_INFO,
    MANIFEST,
    RESOURCES,
    CERTIFICATES;

    public static OutputType fromString(final String string) {
        for (final OutputType type : OutputType.values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }

        return null;
    }

    public static String getAllTypesAsString() {
        final List<String> items = new ArrayList<>();
        for (final OutputType type : OutputType.values()) {
            items.add(type.toString().toLowerCase(Locale.ROOT));
        }

        Collections.sort(items);
        return "[" + String.join(", ", items) + "]";
    }
}
