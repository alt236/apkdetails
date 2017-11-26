package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.output.*;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;
import java.util.List;

class ApkDetails {

    public void printDetails(final CommandLineOptions cli,
                             final List<String> files) {
        System.out.println("APK Files: " + files.size());

        final boolean verbose = cli.isVerbose();
        final boolean humanReadableFileSizes = cli.isHumanReadableFileSizes();

        final FileSizeFormatter fileSizeFormatter = new FileSizeFormatter(humanReadableFileSizes);
        final Colorizer colorizer = new Colorizer(true);

        for (final String apkFile : files) {
            final File file = new File(apkFile);
            final ZipContents zipContents = new ZipContents(file);

            final Output fileInfoOutput = new FileInfoOutput(file, fileSizeFormatter);
            final Output manifestInfoOutput = new ManifestInfoOutput(file, colorizer, verbose);
            final Output dexInfoOutput = new DexInfoOutput(zipContents);
            final Output resOutput = new ResourcesOutput(zipContents);
            final Output archOutput = new ArchitecturesOutput(zipContents);
            final Output signingInfoOutput = new SigningInfoOutput(file, colorizer);
            final Output contentSizeOutput = new ContentSizeOutput(zipContents, fileSizeFormatter, 10);

            final OutputCollector collector = new OutputCollector();
            collector.addSectionLine();

            collect(collector, fileInfoOutput, true);
            collect(collector, manifestInfoOutput, true);
            collect(collector, resOutput, true);
            collect(collector, archOutput, true);
            collect(collector, dexInfoOutput, true);
            collect(collector, signingInfoOutput, true);
            collect(collector, contentSizeOutput, true);
            zipContents.close();

            System.out.println(collector.toString());
        }
    }

    private void collect(OutputCollector outputCollector, Output output, final boolean enabled) {
        if (enabled) {
            output.output(outputCollector);
            outputCollector.addNewLine();
        }
    }
}
