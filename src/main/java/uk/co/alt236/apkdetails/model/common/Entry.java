package uk.co.alt236.apkdetails.model.common;

import java.util.zip.ZipEntry;

public class Entry {
    private final String name;
    private final boolean directory;


    public Entry(ZipEntry zipEntry) {
        name = zipEntry.getName();
        directory = zipEntry.isDirectory();
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                '}';
    }
}