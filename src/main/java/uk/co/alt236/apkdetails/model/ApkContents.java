package uk.co.alt236.apkdetails.model;

import uk.co.alt236.apkdetails.model.common.Entry;
import uk.co.alt236.apkdetails.model.common.ZipContents;

import java.util.*;
import java.util.stream.Collectors;

public class ApkContents {
    private static final String JNI_DIRECTORY = "lib/";
    private static final String ASSETS_DIRECTORY = "assets/";
    private static final String DRAWABLES_DIRECTORY_PREFIX = "res/drawable";
    private static final String LAYOUTS_DIRECTORY_PREFIX = "res/layout";
    private static final String RAW_DIRECTORY_PREFIX = "res/raw";

    private final ZipContents zipContents;

    public ApkContents(ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    public long getNumberOfAssets() {
        return zipContents.getEntries()
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
        final List<Entry> libFiles = zipContents
                .getEntries(entry -> entry.getName().startsWith(JNI_DIRECTORY));

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
        return zipContents.getEntries(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
                .stream()
                .map(entry -> entry.getName().substring(entry.getName().lastIndexOf("/")))
                .collect(Collectors.toSet())
                .size();
    }
}
