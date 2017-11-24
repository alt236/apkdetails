package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.CommandLine;

public class CommandLineOptions {

    private final CommandLine commandLine;

    public CommandLineOptions(final CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public boolean isVerbose() {
        return commandLine.hasOption(OptionsBuilder.ARG_VERBOSE_LONG);
    }

    public String getInput() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_INPUT_LONG);
    }
}
