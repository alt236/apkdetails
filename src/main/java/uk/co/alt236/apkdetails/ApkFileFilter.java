package uk.co.alt236.apkdetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApkFileFilter {

    public List<String> getFiles(final String source) {
        final File file = new File(source);
        return getFiles(file);
    }

    public List<String> getFiles(final File file) {
        final List<String> retVal = new ArrayList<>();

        if (file.exists()) {
            if (file.isDirectory()) {
                final File[] files = file.listFiles(
                        (dir, name) -> name.toLowerCase(Locale.US).endsWith(".apk"));

                if (files != null) {
                    for (final File apk : files) {
                        retVal.add(apk.getAbsolutePath());
                    }
                }
            } else {
                retVal.add(file.getAbsolutePath());
            }
        }

        return retVal;
    }
}
