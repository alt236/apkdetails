package uk.co.alt236.apkdetails.repo.architectures;

import uk.co.alt236.apkdetails.repo.common.Entry;

import java.util.List;

public class Architecture {
    private final String name;
    private final List<Entry> files;

    public Architecture(String name,
                        List<Entry> files) {
        this.name = name;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public List<Entry> getFiles() {
        return files;
    }
}
