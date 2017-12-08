package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.output.sections.*;
import uk.co.alt236.apkdetails.print.file.FileWriter;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;
import java.util.List;

class ApkDetails {

    public void printDetails(final CommandLineOptions cli,
                             final List<String> files) {

        final boolean isSaveToFileEnabled = cli.getOutputDirectory() != null;
        Logger.get().out("APK Files: " + files.size());

        final boolean verbose = cli.isVerbose();
        final boolean humanReadableFileSizes = cli.isHumanReadableFileSizes();

        final FileSizeFormatter fileSizeFormatter = new FileSizeFormatter(humanReadableFileSizes);
        final Colorizer colorizer = new Colorizer(!isSaveToFileEnabled);

        for (final String apkFile : files) {
            final File file = new File(apkFile);
            final OutputFilenames outputFilenames = new OutputFilenames(cli.getOutputDirectory(), file);
            setupLogger(outputFilenames.getMainOutputPath());

            final ZipContents zipContents = new ZipContents(file);
            final AndroidManifestRepository manifestRepository = new AndroidManifestRepository(file);

            final Output fileInfoOutput = new FileInfoOutput(file, fileSizeFormatter);
            final Output manifestInfoOutput = new ManifestInfoOutput(manifestRepository, colorizer, verbose);
            final Output dexInfoOutput = new DexInfoOutput(zipContents);
            final Output resOutput = new ResourcesOutput(zipContents);
            final Output archOutput = new ArchitecturesOutput(zipContents, verbose);
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


            saveManifest(manifestRepository, outputFilenames);
            Logger.get().out(collector.toString());
            zipContents.close();
        }

        Logger.get().close();
    }

    private void collect(OutputCollector outputCollector, Output output, final boolean enabled) {
        if (enabled) {
            output.output(outputCollector);
            outputCollector.addNewLine();
        }
    }


    private void saveManifest(final AndroidManifestRepository repository,
                              final OutputFilenames outputFilenames) {
        final File manifestFile = outputFilenames.getManifestOutputPath();
        if (manifestFile != null) {
            FileWriter writer = new FileWriter(manifestFile);
            String xml;

            try {
                xml = repository.getXml();
            } catch (IllegalStateException e) {
                xml = e.getMessage();
            }

            writer.outln(xml);
            writer.close();
        }
    }

    private void setupLogger(final File outputFile) {
        if (outputFile == null) {
            new Logger.Builder()
                    .build();
        } else {
            new Logger.Builder()
                    .withFileWriter(new FileWriter(outputFile))
                    .build();
        }
    }

    private static class OutputFilenames {
        private final File apk;
        private final String outputFolder;

        private OutputFilenames(final String outputFolder, File apk) {
            this.apk = apk;
            this.outputFolder = outputFolder;
        }

        public File getMainOutputPath() {
            if (outputFolder == null) {
                return null;
            } else {
                return new File(outputFolder, apk.getName() + ".txt");
            }
        }

        public File getManifestOutputPath() {
            if (outputFolder == null) {
                return null;
            } else {
                return new File(outputFolder, apk.getName() + "_manifest.txt");
            }
        }
    }
}
