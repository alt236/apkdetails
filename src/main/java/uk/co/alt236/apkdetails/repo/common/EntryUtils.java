package uk.co.alt236.apkdetails.repo.common;

import java.util.List;
import java.util.stream.Collectors;

public final class EntryUtils {

    public static List<String> toListOfNames(final List<Entry> entries) {
        return entries
                .stream()
                .map(Entry::getName)
                .collect(Collectors.toList());
    }
}
