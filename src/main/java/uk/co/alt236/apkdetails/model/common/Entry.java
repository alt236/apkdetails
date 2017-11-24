package uk.co.alt236.apkdetails.model.common;

import java.util.zip.ZipEntry;

public class Entry {
    private final String name;
    private final boolean directory;
    private final long fileSize;

    public Entry(ZipEntry zipEntry) {
        name = zipEntry.getName();
        directory = zipEntry.isDirectory();
        fileSize = zipEntry.getSize();
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                ", fileSize=" + fileSize +
                '}';
    }
}