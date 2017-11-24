package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import uk.co.alt236.apkdetails.resources.Strings;

public class OptionsBuilder {

    /*package*/ static final String ARG_INPUT = "i";
    /*package*/ static final String ARG_INPUT_LONG = "input";

    /*package*/ static final String ARG_VERBOSE = "v";
    /*package*/ static final String ARG_VERBOSE_LONG = "verbose";

    private final Strings strings;

    public OptionsBuilder(Strings strings) {
        this.strings = strings;
    }

    public Options compileOptions() {
        final Options options = new Options();

        options.addOption(createOptionInputJar());
        options.addOption(createOptionVerbose());

        return options;
    }

    private Option createOptionInputJar() {
        final String desc = strings.getString("cli_cmd_input");
        return Option.builder(ARG_INPUT)
                .longOpt(ARG_INPUT_LONG)
                .hasArg()
                .required(true)
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
}
