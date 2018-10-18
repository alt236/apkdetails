package uk.co.alt236.apkdetails.print.writer;

import java.io.StringWriter;

public class StringCollector implements Writer {

    private final StringWriter stringWriter;
    private final String lineSeparator;

    public StringCollector() {
        stringWriter = new StringWriter();
        lineSeparator = System.getProperty("line.separator");
    }

    @Override
    public void outln(String string) {
        stringWriter.append(string);
        stringWriter.append(lineSeparator);
    }

    @Override
    public void errln(String string) {
        stringWriter.append(string);
        stringWriter.append(lineSeparator);
    }

    @Override
    public void close() {
        // NOOP
    }

    public String getCollectedString() {
        return stringWriter.getBuffer().toString();
    }
}
