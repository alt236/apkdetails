package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.ApkContents;
import uk.co.alt236.apkdetails.print.SectionedKvPrinter;

import java.io.File;
import java.util.List;

public class ApkInfoOutput implements Output {

    private static String toString(final List<?> list) {
        final StringBuilder sb = new StringBuilder();
        for (final Object object : list) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append(object);
        }
        return sb.toString();
    }

    @Override
    public void output(SectionedKvPrinter printer, File file) {
        final ApkContents fileInfo = new ApkContents(file);
        final List<String> jniArchitectures = fileInfo.getJniArchitectures();


        printer.add("APK Contents");
        printer.startKeyValueSection();
        printer.addKv("Assets", fileInfo.getNumberOfAssets());
        printer.addKv("Unique Raw", fileInfo.getNumberOfRawRes());
        printer.addKv("Unique Layouts", fileInfo.getNumberOfLayoutRes());
        printer.addKv("Unique Drawables", fileInfo.getNumberOfDrawableRes());
        printer.addKv("JNI Architectures", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));
        printer.endKeyValueSection();
    }
}
