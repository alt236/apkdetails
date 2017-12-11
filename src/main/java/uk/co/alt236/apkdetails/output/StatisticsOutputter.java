package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.output.sections.*;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;

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
                         DexRepository dexRepository,
                         boolean verbose) {

        final File apk = outputPathFactory.getApk();
        final Output fileInfoOutput = new FileInfoOutput(apk, fileSizeFormatter);
        final Output manifestInfoOutput = new ManifestInfoOutput(manifestRepository, colorizer, verbose);
        final Output dexInfoOutput = new DexInfoOutput(zipContents);
        final Output resOutput = new ResourcesOutput(zipContents);
        final Output archOutput = new ArchitecturesOutput(zipContents, verbose);
        final Output signingInfoOutput = new SigningInfoOutput(apk, colorizer);
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

        Logger.get().out(collector.toString());
    }

    private void collect(OutputCollector outputCollector, Output output, final boolean enabled) {
        if (enabled) {
            output.output(outputCollector);
            outputCollector.addNewLine();
        }
    }
}
