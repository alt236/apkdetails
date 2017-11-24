package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import uk.co.alt236.apkdetails.resources.Strings;

import java.util.ResourceBundle;

public class CommandHelpPrinter {
    private static final String BINARY_PLACEHOLDER = "##BINARY##";

    private final Strings strings;
    private final String binaryName;
    private final Options options;

    public CommandHelpPrinter(final Strings strings,
                              final Options options,
                              final String binaryName) {
        this.strings = strings;
        this.binaryName = binaryName;
        this.options = options;
    }

    public void printHelp() {
        final ResourceBundle labels = new Strings();

        final String header = labels.getString("cli_help_message_header")
                .replaceAll(BINARY_PLACEHOLDER, binaryName);
        final String footer = labels.getString("cli_help_message_footer")
                .replaceAll(BINARY_PLACEHOLDER, binaryName);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(binaryName, header, options, footer, true);
    }
}
