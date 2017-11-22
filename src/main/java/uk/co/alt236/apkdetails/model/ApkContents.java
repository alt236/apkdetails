package uk.co.alt236.apkdetails.model;

import uk.co.alt236.apkdetails.model.common.Entry;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkContents {
    private static final String JNI_DIRECTORY = "lib/";
    private static final String ASSETS_DIRECTORY = "assets/";
    private static final String DRAWABLES_DIRECTORY_PREFIX = "res/drawable";
    private static final String LAYOUTS_DIRECTORY_PREFIX = "res/layout";
    private static final String RAW_DIRECTORY_PREFIX = "res/raw";

    private final File file;
    private final List<Entry> entryList;

    public ApkContents(File file) {
        this.file = file;
        this.entryList = new ArrayList<>();
    }

    public long getNumberOfAssets() {
        parseZipFile();
        return entryList
                .stream()
                .filter(entry -> !entry.isDirectory() && entry.getName().startsWith(ASSETS_DIRECTORY))
                .count();
    }

    public long getNumberOfDrawableRes() {
        return getNumberOfResources(DRAWABLES_DIRECTORY_PREFIX);
    }

    public long getNumberOfRawRes() {
        return getNumberOfResources(RAW_DIRECTORY_PREFIX);
    }

    public long getNumberOfLayoutRes() {
        return getNumberOfResources(LAYOUTS_DIRECTORY_PREFIX);
    }

    public List<String> getJniArchitectures() {
        parseZipFile();

        final List<Entry> libFiles = entryList
                .stream()
                .filter(entry -> entry.getName().startsWith(JNI_DIRECTORY))
                .collect(Collectors.toList());

        final Set<String> architectures = new HashSet<>();

        for (final Entry entry : libFiles) {
            final String cleanName = entry.getName().substring(JNI_DIRECTORY.length(), entry.getName().length());

            if (!entry.isDirectory()) {
                architectures.add(cleanName.substring(0, cleanName.indexOf("/")));
            }
        }

        final List<String> retVal = new ArrayList<>(architectures);
        Collections.sort(retVal);
        return retVal;
    }

    private long getNumberOfResources(String prefix) {
        parseZipFile();
        return entryList
                .stream()
                .filter(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
                .map(entry -> entry.getName().substring(entry.getName().lastIndexOf("/")))
                .collect(Collectors.toSet())
                .size();
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


}
