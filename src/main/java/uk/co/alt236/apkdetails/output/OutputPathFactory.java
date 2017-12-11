package uk.co.alt236.apkdetails.output;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class OutputPathFactory {
    private final File apk;
    private final String outputFolder;

    public OutputPathFactory(@Nonnull File apk, @Nullable final String outputFolder) {
        this.apk = apk;
        this.outputFolder = outputFolder;
    }

    @Nonnull
    public File getApk() {
        return apk;
    }

    @Nullable
    public File getMainLog() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + ".txt");
        }
    }

    @Nullable
    public File getManifestFile() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + "_manifest.xml");
        }
    }

    @Nullable
    public File getClassTreeFile() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + "_class_tree.txt");
        }
    }

    @Nullable
    public File getClassListFile() {
        if (outputFolder == null) {
            return null;
        } else {
            return new File(outputFolder, apk.getName() + "_class_list.txt");
        }
    }
}
