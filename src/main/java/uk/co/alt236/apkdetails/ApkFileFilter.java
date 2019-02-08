package uk.co.alt236.apkdetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

class ApkFileFilter {

    List<String> getFiles(final String source,
                          boolean recursive) {
        final File file = new File(source);
        return getFiles(file, recursive);
    }

    List<String> getFiles(final File file,
                          boolean recursive) {
        final List<String> retVal = new ArrayList<>();

        if (file.exists()) {
            if (file.isDirectory()) {
                final int depth = recursive ? Integer.MAX_VALUE : 1;
                retVal.addAll(getContainedFiles(file, depth));
            } else {
                retVal.add(file.getAbsolutePath());
            }
        }

        Collections.sort(retVal);
        return retVal;
    }

    private List<String> getContainedFiles(final File directory,
                                           int depth) {
        final List<String> retVal = new ArrayList<>();

        try (Stream<Path> stream = Files.find(Paths.get(directory.toURI()),
                depth,
                this::isApk)) {

            stream.forEach(path -> retVal.add(path.toAbsolutePath().toString()));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return retVal;
    }

    private boolean isApk(Path path,
                          BasicFileAttributes fileAttr) {
        return fileAttr.isRegularFile()
                && path.toString().toLowerCase(Locale.US).endsWith(".apk");
    }
}
