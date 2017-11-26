package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.Architectures;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.util.List;

public class ArchitecturesOutput implements Output {
    private final ZipContents zipContents;

    public ArchitecturesOutput(final ZipContents zipContents) {
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
        final Architectures archs = new Architectures(zipContents);
        final List<String> jniArchitectures = archs.getJniArchitectures();


        printer.add("Native architectures");
        printer.startKeyValueSection();
        printer.addKv("JNI Architectures", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));
        printer.endKeyValueSection();
    }
}
