package uk.co.alt236.apkdetails.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkContents {
    private final String path;
    private final List<Entry> entryList;

    public ApkContents(String path) {
        this.path = path;
        this.entryList = new ArrayList<>();
    }

    public long getNumberOfAssets() {
        parseZipFile();
        return entryList
                .stream()
                .filter(entry -> !entry.isDirectory() && entry.getName().startsWith("assets/"))
                .count();
    }

    private synchronized void parseZipFile() {
        if (entryList.isEmpty()) {
            try {
                final ZipFile zipFile = new ZipFile(path);
                final Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    final ZipEntry zipEntry = entries.nextElement();
                    entryList.add(new Entry(zipEntry));
                }

                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Entry {
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
    }
}
