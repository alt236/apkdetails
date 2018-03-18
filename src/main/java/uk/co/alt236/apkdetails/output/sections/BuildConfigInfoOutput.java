package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.buildconfig.BuildConfigRepository;
import uk.co.alt236.apkdetails.repo.buildconfig.model.BuildConfigFile;
import uk.co.alt236.apkdetails.repo.buildconfig.model.Field;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

public class BuildConfigInfoOutput implements Output {
    private final ZipContents zipContents;
    private final boolean verbose;

    public BuildConfigInfoOutput(final ZipContents zipContents, final boolean verbose) {
        this.zipContents = zipContents;
        this.verbose = verbose;
    }

    @Override
    public void output(OutputCollector printer) {
        final BuildConfigRepository repo = new BuildConfigRepository(zipContents);

        printer.add("BuildConfigs");
        printer.startKeyValueSection();
        printer.addKv("BuildConfig Files", repo.getBuildConfigFiles().size());

        if (verbose && !repo.getBuildConfigFiles().isEmpty()) {
            int count = 0;
            for (final BuildConfigFile file : repo.getBuildConfigFiles()) {
                count++;

                final String prefix = "BuildConfig " + count + " ";

                printer.addKv(prefix + "Class", file.getType());
                for (final Field field : file.getFields()) {
                    printer.addKv("  " + field.getName(), field.getValue());
                }
            }
        }

        printer.endKeyValueSection();
    }
}
