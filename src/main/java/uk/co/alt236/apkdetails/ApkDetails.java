package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.output.*;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.io.File;
import java.util.List;

class ApkDetails {

    public void printDetails(final CommandLineOptions commandLineOptions,
                             final List<String> files) {
        System.out.println("APK Files: " + files.size());

        final boolean verbose = commandLineOptions.isVerbose();

        for (final String apkFile : files) {
            final File file = new File(apkFile);
            final ZipContents zipContents = new ZipContents(file);

            final SectionedKvPrinter printer = new SectionedKvPrinter();
            printer.addSectionLine();

            new FileInfoOutput(file).output(printer);
            printer.addNewLine();

            new ManifestInfoOutput(file, verbose).output(printer);
            printer.addNewLine();

            new ApkInfoOutput(zipContents).output(printer);
            printer.addNewLine();

            new DexInfoOutput(zipContents).output(printer);
            printer.addNewLine();

            new SigningInfoOutput(file).output(printer);
            printer.print();

            zipContents.close();
        }
    }
}
