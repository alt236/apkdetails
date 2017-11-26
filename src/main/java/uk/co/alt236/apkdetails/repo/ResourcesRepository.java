package uk.co.alt236.apkdetails.repo;

import uk.co.alt236.apkdetails.repo.common.ZipContents;

import java.util.stream.Collectors;

public class ResourcesRepository {
    private static final String ASSETS_DIRECTORY = "assets/";
    private static final String DRAWABLES_DIRECTORY_PREFIX = "res/drawable";
    private static final String LAYOUTS_DIRECTORY_PREFIX = "res/layout";
    private static final String RAW_DIRECTORY_PREFIX = "res/raw";

    private final ZipContents zipContents;

    public ResourcesRepository(ZipContents zipContents) {
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

    private long getNumberOfResources(String prefix) {
        return zipContents.getEntries(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
                .stream()
                .map(entry -> entry.getName().substring(entry.getName().lastIndexOf("/")))
                .collect(Collectors.toSet())
                .size();
    }
}
