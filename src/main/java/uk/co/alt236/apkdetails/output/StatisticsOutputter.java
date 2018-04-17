package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.output.sections.*;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;
import java.util.EnumSet;

public class StatisticsOutputter {

    private final FileSizeFormatter fileSizeFormatter;
    private final Colorizer colorizer;

    public StatisticsOutputter(final FileSizeFormatter fileSizeFormatter,
                               final Colorizer colorizer) {
        this.fileSizeFormatter = fileSizeFormatter;
        this.colorizer = colorizer;
    }

    public void doOutput(OutputPathFactory outputPathFactory,
                         ZipContents zipContents,
                         AndroidManifestRepository manifestRepository,
                         EnumSet<OutputType> enabledOutputs,
                         boolean verbose) {

        final File apk = outputPathFactory.getApk();
        final Output fileInfoOutput = new FileInfoOutput(apk, fileSizeFormatter);
        final Output manifestInfoOutput = new ManifestInfoOutput(manifestRepository, colorizer, verbose);
        final Output dexInfoOutput = new DexInfoOutput(zipContents);
        final Output resOutput = new ResourcesOutput(zipContents);
        final Output archOutput = new ArchitecturesOutput(zipContents, verbose);
        final Output signingInfoOutput = new SigningInfoOutput(apk, colorizer);
        final Output contentSizeOutput = new ContentSizeOutput(zipContents, fileSizeFormatter, 10);
        final Output buildConfigFileOutput = new BuildConfigInfoOutput(zipContents, verbose);

        final OutputCollector collector = new OutputCollector();
        collector.addSectionLine();

        collect(collector, fileInfoOutput, enabledOutputs);
        collect(collector, manifestInfoOutput, enabledOutputs);
        collect(collector, resOutput, enabledOutputs);
        collect(collector, archOutput, enabledOutputs);
        collect(collector, dexInfoOutput, enabledOutputs);
        collect(collector, signingInfoOutput, enabledOutputs);
        collect(collector, buildConfigFileOutput, enabledOutputs);
        collect(collector, contentSizeOutput, enabledOutputs);

        Logger.get().out(collector.toString());
    }

    private void collect(final OutputCollector outputCollector,
                         final Output output,
                         final EnumSet<OutputType> enabledOutputs) {

        if (enabledOutputs.contains(output.getOutputType())) {
            output.output(outputCollector);
            outputCollector.addNewLine();
        }
    }
}
