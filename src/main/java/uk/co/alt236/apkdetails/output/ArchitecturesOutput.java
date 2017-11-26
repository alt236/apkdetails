package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.ArchitectureRepository;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

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
    public void output(OutputCollector printer) {
        final ArchitectureRepository archs = new ArchitectureRepository(zipContents);
        final List<String> jniArchitectures = archs.getJniArchitectures();


        printer.add("Native architectures");
        printer.startKeyValueSection();
        printer.addKv("JNI ArchitectureRepository", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));
        printer.endKeyValueSection();
    }
}
