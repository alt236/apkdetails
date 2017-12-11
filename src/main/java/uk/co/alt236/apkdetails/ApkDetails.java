package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.output.FilePathFactory;
import uk.co.alt236.apkdetails.output.FilesOutputter;
import uk.co.alt236.apkdetails.output.StatisticsOutputter;
import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.print.file.FileWriter;
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

        final FilesOutputter filesOutputter = new FilesOutputter(fileSizeFormatter, colorizer);
        final StatisticsOutputter statsOutputter = new StatisticsOutputter(fileSizeFormatter, colorizer);

        for (final String apkFile : files) {
            final File file = new File(apkFile);
            final FilePathFactory filePathFactory = new FilePathFactory(file, cli.getOutputDirectory());
            setupLogger(filePathFactory.getMainOutputPath());

            final ZipContents zipContents = new ZipContents(file);
            final AndroidManifestRepository manifestRepository = new AndroidManifestRepository(file);

            statsOutputter.doOutput(filePathFactory, zipContents, manifestRepository, verbose);
            filesOutputter.doOutput(filePathFactory, zipContents, manifestRepository, verbose);

            zipContents.close();
        }

        Logger.get().close();
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
}
