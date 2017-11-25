package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import uk.co.alt236.apkdetails.resources.Strings;

public class CommandHelpPrinter {
    private static final String BINARY_PLACEHOLDER = "##BINARY##";
    private static final String VERSION_PLACEHOLDER = "##VERSION##";

    private final Strings strings;
    private final Options options;
    private final JarDetails jarDetails;

    public CommandHelpPrinter(final Strings strings,
                              final JarDetails jarDetails,
                              final Options options) {
        this.strings = strings;
        this.jarDetails = jarDetails;
        this.options = options;
    }

    public void printHelp() {
        final String header = replacePlaceholders(strings.getString("cli_help_message_header"));
        final String footer = replacePlaceholders(strings.getString("cli_help_message_footer"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(jarDetails.getJarName(), header, options, footer, true);
    }

    private String replacePlaceholders(final String baseString) {
        return baseString
                .replaceAll(BINARY_PLACEHOLDER, jarDetails.getJarName())
                .replaceAll(VERSION_PLACEHOLDER, jarDetails.getJarVersion());
    }
}
