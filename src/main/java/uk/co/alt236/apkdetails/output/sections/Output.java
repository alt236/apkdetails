package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;

public interface Output {
    void output(OutputCollector printer);

    OutputType getOutputType();
}
