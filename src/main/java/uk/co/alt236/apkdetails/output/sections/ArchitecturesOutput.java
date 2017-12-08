package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.architectures.Architecture;
import uk.co.alt236.apkdetails.repo.architectures.ArchitectureRepository;
import uk.co.alt236.apkdetails.repo.common.EntryUtils;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

import java.util.List;

public class ArchitecturesOutput implements Output {
    private final ZipContents zipContents;
    private final boolean verbose;

    public ArchitecturesOutput(final ZipContents zipContents, final boolean verbose) {
        this.zipContents = zipContents;
        this.verbose = verbose;
    }

    @Override
    public void output(OutputCollector printer) {
        final ArchitectureRepository archs = new ArchitectureRepository(zipContents);
        final List<Architecture> jniArchitectures = archs.getJniArchitectures();

        printer.add("Native architectures");
        printer.startKeyValueSection();
        printer.addKv("JNI Architectures", jniArchitectures.isEmpty()
                ? "none"
                : toString(jniArchitectures));

        if (verbose && !jniArchitectures.isEmpty()) {
            for (final Architecture architecture : jniArchitectures) {
                printer.addKv(architecture.getName(), EntryUtils.toListOfNames(architecture.getFiles()));
            }
        }

        printer.endKeyValueSection();
    }

    private String toString(final List<Architecture> list) {
        final StringBuilder sb = new StringBuilder();
        for (final Architecture architecture : list) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append(architecture.getName());
        }
        return sb.toString();
    }
}
