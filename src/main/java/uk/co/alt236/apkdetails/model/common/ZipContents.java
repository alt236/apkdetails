package uk.co.alt236.apkdetails.model.common;

import uk.co.alt236.apkdetails.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipContents {
    private final File file;
    private final List<Entry> entryList;
    private ZipFile zipFile;

    public ZipContents(final File file) {
        this.file = file;
        this.entryList = new ArrayList<>();
    }

    private synchronized void parseZipFile() {
        if (entryList.isEmpty()) {
            try {
                final ZipFile zipFile = new ZipFile(file);
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

    public List<Entry> getEntries() {
        parseZipFile();
        return Collections.unmodifiableList(entryList);
    }

    public List<Entry> getEntries(Predicate<Entry> predicate) {
        parseZipFile();
        return entryList
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public synchronized InputStream getInputStream(final Entry entry) {
        if (zipFile == null) {
            try {
                zipFile = new ZipFile(file);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        final ZipEntry zipEntry = zipFile.getEntry(entry.getName());
        try {
            return new BufferedInputStream(zipFile.getInputStream(zipEntry));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        StreamUtils.close(zipFile);
    }

    public File getFile() {
        return file;
    }
}
