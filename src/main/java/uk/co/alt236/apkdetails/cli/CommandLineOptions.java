package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.CommandLine;
import uk.co.alt236.apkdetails.output.sections.OutputType;

import java.util.EnumSet;

public class CommandLineOptions {

    private final CommandLine commandLine;
    private final EnumSet<OutputType> selectedOutputs;

    public CommandLineOptions(final CommandLine commandLine) {
        this.commandLine = commandLine;
        this.selectedOutputs = parseSelectedOutputs();
    }

    public boolean isVerbose() {
        return commandLine.hasOption(OptionsBuilder.ARG_VERBOSE_LONG);
    }

    public boolean isHumanReadableFileSizes() {
        return commandLine.hasOption(OptionsBuilder.ARG_HUMAN_READABLE_SIZES_LONG);
    }

    public String getInput() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_INPUT_LONG);
    }

    public String getOutputDirectory() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_OUTPUT_LONG);
    }

    public EnumSet<OutputType> getSelectedOutputs() {
        return selectedOutputs;
    }

    public boolean isPrintClassList() {
        return commandLine.hasOption(OptionsBuilder.ARG_PRINT_CLASS_LIST);
    }

    public boolean isPrintClassTree() {
        return commandLine.hasOption(OptionsBuilder.ARG_PRINT_CLASS_TREE);
    }

    public boolean isPrintClassGraph() {
        return commandLine.hasOption(OptionsBuilder.ARG_PRINT_CLASS_GRAPH);
    }

    public boolean isPrintManifest() {
        return commandLine.hasOption(OptionsBuilder.ARG_PRINT_MANIFEST);
    }

    private EnumSet<OutputType> parseSelectedOutputs() {
        final String[] stringArgs = commandLine.getOptionValues(OptionsBuilder.ARG_SHOW_ONLY_LONG);

        final EnumSet<OutputType> retVal;

        if (stringArgs == null || stringArgs.length == 0) {
            retVal = EnumSet.allOf(OutputType.class);
        } else {
            retVal = EnumSet.noneOf(OutputType.class);

            for (final String arg : stringArgs) {
                if (arg.isEmpty()) { // This is to skip any spaces after a comma e.g. "a, b"
                    continue;
                }

                final OutputType outputType = OutputType.fromString(arg);
                if (outputType == null) {
                    System.err.println("Unknown output: '" + arg + "'. Valid outputs are: " + OutputType.getAllTypesAsString());
                    System.exit(-1);
                } else {
                    retVal.add(OutputType.fromString(arg));
                }
            }
        }

        return retVal;
    }
}
