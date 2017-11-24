package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.ApkContents;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.util.List;

public class ApkInfoOutput implements Output {

    private final ZipContents zipContents;

    public ApkInfoOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

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
    public void output(SectionedKvPrinter printer) {
        final ApkContents fileInfo = new ApkContents(zipContents);
        final List<String> jniArchitectures = fileInfo.getJniArchitectures();


        printer.add("APK Contents");
        printer.startKeyValueSection();
        printer.addKv("Res Raw", fileInfo.getNumberOfRawRes());
        printer.addKv("Res Layouts", fileInfo.getNumberOfLayoutRes());
        printer.addKv("Res Drawables", fileInfo.getNumberOfDrawableRes());
        printer.addKv("Assets", fileInfo.getNumberOfAssets());
        printer.addKv("JNI Architectures", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));
        printer.endKeyValueSection();
    }
}
