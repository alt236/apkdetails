package uk.co.alt236.apkdetails;

import org.apache.commons.cli.*;
import uk.co.alt236.apkdetails.cli.CommandHelpPrinter;
import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.cli.JarDetails;
import uk.co.alt236.apkdetails.cli.OptionsBuilder;
import uk.co.alt236.apkdetails.resources.Strings;

import java.util.List;

public class Main {

    private Main() {
        //NOOP
    }

    public static void main(String[] args) {
        final Strings strings = new Strings();
        final CommandLineOptions cliOptions = parseArgs(strings, args);

        if (cliOptions != null) {
            final ApkFileFilter apkFileFilter = new ApkFileFilter();
            final List<String> files = apkFileFilter.getFiles(cliOptions.getInput());

            if (files.isEmpty()) {
                System.err.println("No valid files found");
                System.exit(1);
            } else {
                new ApkDetails().printDetails(cliOptions, files);
            }
        }
    }

    private static CommandLineOptions parseArgs(Strings strings, String[] args) {
        final CommandLineParser parser = new DefaultParser();
        final Options options = new OptionsBuilder(strings).compileOptions();
        final CommandLineOptions retVal;

        if (args.length == 0) {
            final JarDetails jarDetails = new JarDetails(Main.class);
            new CommandHelpPrinter(strings, jarDetails, options).printHelp();
            retVal = null;
        } else {
            CommandLine line = null;

            try {
                line = parser.parse(options, args);
            } catch (final ParseException exp) {
                final String message = exp.getMessage();
                System.err.println(message);
                System.exit(1);
            }

            retVal = new CommandLineOptions(line);
        }

        return retVal;
    }
}
