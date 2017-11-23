package uk.co.alt236.apkdetails.print.section.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MultiLineValue implements Value {
    private final List<SimpleValue> values;

    public MultiLineValue(final Collection<?> collection) {
        values = new ArrayList<>(collection.size());
        for (final Object object : collection) {
            values.add(new SimpleValue(object));
        }
    }

    public List<SimpleValue> getValues() {
        return Collections.unmodifiableList(values);
    }
}
