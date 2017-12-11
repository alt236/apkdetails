package uk.co.alt236.apkdetails.output;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class FilePathFactory {
    private final File apk;
    private final String outputFolder;

    public FilePathFactory(@Nonnull File apk, @Nullable final String outputFolder) {
        this.apk = apk;
        this.outputFolder = outputFolder;
    }

    @Nonnull
    public File getApk() {
        return apk;
    }

    @Nullable
    public File getMainOutputPath() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + ".txt");
        }
    }

    @Nullable
    public File getManifestOutputPath() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + "_manifest.xml");
        }
    }
}
