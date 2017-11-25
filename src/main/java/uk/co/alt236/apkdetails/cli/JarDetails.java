package uk.co.alt236.apkdetails.cli;

import java.io.File;

public class JarDetails {
    public final Class<?> clazz;

    public JarDetails(final Class clazz) {
        this.clazz = clazz;
    }

    public String getJarVersion() {
        return String.valueOf(clazz.getPackage().getImplementationVersion());
    }

    public String getJarName() {
        final File
                f =
                new File(clazz.getProtectionDomain().getCodeSource().getLocation().toString());
        return f.getName();
    }
}
