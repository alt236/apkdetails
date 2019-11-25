package uk.co.alt236.apkdetails.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import uk.co.alt236.apkdetails.output.sections.OutputType;
import uk.co.alt236.apkdetails.resources.Strings;

public class OptionsBuilder {

    /*package*/ static final String ARG_INPUT = "i";
    /*package*/ static final String ARG_INPUT_LONG = "input";

    /*package*/ static final String ARG_RECURSIVE = "r";
    /*package*/ static final String ARG_RECURSIVE_LONG = "recursive";

    /*package*/ static final String ARG_OUTPUT = "o";
    /*package*/ static final String ARG_OUTPUT_LONG = "output";

    /*package*/ static final String ARG_VERBOSE = "v";
    /*package*/ static final String ARG_VERBOSE_LONG = "verbose";

    /*package*/ static final String ARG_HUMAN_READABLE_SIZES = "h";
    /*package*/ static final String ARG_HUMAN_READABLE_SIZES_LONG = "human";

    /*package*/ static final String ARG_SHOW_ONLY = "s";
    /*package*/ static final String ARG_SHOW_ONLY_LONG = "show-only";

    /*package*/ static final String ARGS_PACKAGE_FILTER = "p";
    /*package*/ static final String ARGS_PACKAGE_FILTER_LONG = "package-filter";

    /*package*/ static final String ARG_PRINT_CLASS_LIST = "print-class-list";
    /*package*/ static final String ARG_PRINT_CLASS_TREE = "print-class-tree";
    /*package*/ static final String ARG_PRINT_CLASS_GRAPH = "print-class-graph";
    /*package*/ static final String ARG_PRINT_MANIFEST = "print-manifest";

    private final Strings strings;

    public OptionsBuilder(Strings strings) {
        this.strings = strings;
    }

    public Options compileOptions() {
        final Options options = new Options();

        options.addOption(createOptionInput());
        options.addOption(createOptionOutput());
        options.addOption(createOptionVerbose());
        options.addOption(createOptionRecursive());
        options.addOption(createOptionHumanReadableSizes());
        options.addOption(createShowOnly());
        options.addOption(createPackageFilter());


        final OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(createPrintClassGraph());
        optionGroup.addOption(createPrintClassList());
        optionGroup.addOption(createPrintClassTree());
        optionGroup.addOption(createPrintManifest());

        options.addOptionGroup(optionGroup);
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

    private Option createOptionRecursive() {
        final String desc = strings.getString("cli_cmd_input_recursive");
        return Option.builder(ARG_RECURSIVE)
                .longOpt(ARG_RECURSIVE_LONG)
                .hasArg(false)
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

    private Option createPrintClassList() {
        final String desc = strings.getString("cli_cmd_print_class_list");
        return Option.builder()
                .longOpt(ARG_PRINT_CLASS_LIST)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createPrintClassTree() {
        final String desc = strings.getString("cli_cmd_print_class_tree");
        return Option.builder()
                .longOpt(ARG_PRINT_CLASS_TREE)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createPrintClassGraph() {
        final String desc = strings.getString("cli_cmd_print_class_graph");
        return Option.builder()
                .longOpt(ARG_PRINT_CLASS_GRAPH)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createPrintManifest() {
        final String desc = strings.getString("cli_cmd_print_manifest");
        return Option.builder()
                .longOpt(ARG_PRINT_MANIFEST)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createPackageFilter() {
        final String desc = strings.getString("cli_cmd_package_filter");
        return Option.builder(ARGS_PACKAGE_FILTER)
                .longOpt(ARGS_PACKAGE_FILTER_LONG)
                .hasArg(true)
                .required(false)
                .desc(desc)
                .build();
    }

}
