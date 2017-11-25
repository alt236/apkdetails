package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.output.*;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;
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

        for (final String apkFile : files) {
            final File file = new File(apkFile);
            final ZipContents zipContents = new ZipContents(file);

            final Output fileInfoOutput = new FileInfoOutput(file, fileSizeFormatter);
            final Output manifestInfoOutput = new ManifestInfoOutput(file, verbose);
            final Output apkInfoOutput = new ApkInfoOutput(zipContents);
            final Output dexInfoOutput = new DexInfoOutput(zipContents);
            final Output signingInfoOutput = new SigningInfoOutput(file);
            final Output contentSizeOutput = new ContentSizeOutput(zipContents, fileSizeFormatter, 10);

            final SectionedKvPrinter printer = new SectionedKvPrinter();
            printer.addSectionLine();

            output(printer, fileInfoOutput, true);
            output(printer, manifestInfoOutput, true);
            output(printer, apkInfoOutput, true);
            output(printer, dexInfoOutput, true);
            output(printer, signingInfoOutput, true);
            output(printer, contentSizeOutput, verbose);

            printer.print();
            zipContents.close();
        }
    }

    private void output(SectionedKvPrinter printer, Output output, final boolean enabled) {
        if (enabled) {
            output.output(printer);
            printer.addNewLine();
        }
    }
}
