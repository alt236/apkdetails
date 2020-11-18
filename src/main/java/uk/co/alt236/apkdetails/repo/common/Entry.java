package uk.co.alt236.apkdetails.repo.common;

import java.util.zip.ZipEntry;

public class Entry {
    private final String name;
    private final String fileName;
    private final boolean directory;
    private final long fileSize;

    public Entry(ZipEntry zipEntry) {
        name = zipEntry.getName();
        fileName = getFileName(name);
        directory = zipEntry.isDirectory();
        fileSize = zipEntry.getSize();
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return fileName;
    }

    public boolean isDirectory() {
        return directory;
    }

    public long getFileSize() {
        return fileSize;
    }

    private String getFileName(String path) {
        if (!path.contains("/")) {
            return path;
        } else {
            return path.substring(path.lastIndexOf("/") + 1);
        }
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