package uk.co.alt236.apkdetails.model;

import parser.arsc.ARSCFile;
import parser.axml.res.IntReader;
import uk.co.alt236.apkdetails.model.common.Entry;
import uk.co.alt236.apkdetails.model.common.ZipContents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Resources {
    private static final String ASSETS_DIRECTORY = "assets/";
    private static final String DRAWABLES_DIRECTORY_PREFIX = "res/drawable";
    private static final String LAYOUTS_DIRECTORY_PREFIX = "res/layout";
    private static final String RAW_DIRECTORY_PREFIX = "res/raw";

    private final ZipContents zipContents;
    public Resources(ZipContents zipContents) {
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


    public long test() {

        final Entry entry = zipContents.getEntries(entry1 -> entry1.getName().equals("resources.arsc")).get(0);
        final InputStream is = zipContents.getInputStream(entry);
        try {
            final ARSCFile arscFile = new ARSCFile(new IntReader(is, false));
            System.out.println(Arrays.toString(arscFile.pkges));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }


    private long getNumberOfResources(String prefix) {
        return zipContents.getEntries(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
                .stream()
                .map(entry -> entry.getName().substring(entry.getName().lastIndexOf("/")))
                .collect(Collectors.toSet())
                .size();
    }
}
