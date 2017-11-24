package uk.co.alt236.apkdetails;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.output.ApkInfoOutput;
import uk.co.alt236.apkdetails.output.FileInfoOutput;
import uk.co.alt236.apkdetails.output.ManifestInfoOutput;
import uk.co.alt236.apkdetails.output.SigningInfoOutput;
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
            final SectionedKvPrinter printer = new SectionedKvPrinter();

            printer.addSectionLine();

            new FileInfoOutput().output(printer, file);
            printer.addNewLine();

            new ManifestInfoOutput(verbose).output(printer, file);
            printer.addNewLine();

            new ApkInfoOutput().output(printer, file);
            printer.addNewLine();

            new SigningInfoOutput().output(printer, file);
            printer.print();
        }
    }
}
