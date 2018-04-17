package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import uk.co.alt236.apkdetails.output.sections.OutputType;
import uk.co.alt236.apkdetails.resources.Strings;

public class OptionsBuilder {

    /*package*/ static final String ARG_INPUT = "i";
    /*package*/ static final String ARG_INPUT_LONG = "input";

    /*package*/ static final String ARG_OUTPUT = "o";
    /*package*/ static final String ARG_OUTPUT_LONG = "output";

    /*package*/ static final String ARG_VERBOSE = "v";
    /*package*/ static final String ARG_VERBOSE_LONG = "verbose";

    /*package*/ static final String ARG_HUMAN_READABLE_SIZES = "h";
    /*package*/ static final String ARG_HUMAN_READABLE_SIZES_LONG = "human";

    /*package*/ static final String ARG_SHOW_ONLY = "s";
    /*package*/ static final String ARG_SHOW_ONLY_LONG = "show-only";

    private final Strings strings;

    public OptionsBuilder(Strings strings) {
        this.strings = strings;
    }

    public Options compileOptions() {
        final Options options = new Options();

        options.addOption(createOptionInput());
        options.addOption(createOptionOutput());
        options.addOption(createOptionVerbose());
        options.addOption(createOptionHumanReadableSizes());
        options.addOption(createShowOnly());

        return options;
    }

    private Option createOptionInput() {
        final String desc = strings.getString("cli_cmd_input");
        return Option.builder(ARG_INPUT)
                .longOpt(ARG_INPUT_LONG)
                .hasArg()
                .required(true)
                .desc(desc)
                .build();
    }

    private Option createOptionOutput() {
        final String desc = strings.getString("cli_cmd_output");
        return Option.builder(ARG_OUTPUT)
                .longOpt(ARG_OUTPUT_LONG)
                .hasArg()
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createOptionVerbose() {
        final String desc = strings.getString("cli_cmd_input_verbose");
        return Option.builder(ARG_VERBOSE)
                .longOpt(ARG_VERBOSE_LONG)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createOptionHumanReadableSizes() {
        final String desc = strings.getString("cli_cmd_human_readable_sizes");
        return Option.builder(ARG_HUMAN_READABLE_SIZES)
                .longOpt(ARG_HUMAN_READABLE_SIZES_LONG)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createShowOnly() {
        final String validOptions = OutputType.getAllTypesAsString();
        final String desc = strings.getString("cli_cmd_show_only")
                .replace("##SHOW_ONLY##", validOptions);

        return Option.builder(ARG_SHOW_ONLY)
                .longOpt(ARG_SHOW_ONLY_LONG)
                .hasArgs()
                .valueSeparator(',')
                .required(false)
                .desc(desc)
                .build();
    }
}
