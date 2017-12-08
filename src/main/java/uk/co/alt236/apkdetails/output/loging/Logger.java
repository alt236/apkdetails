package uk.co.alt236.apkdetails.output.loging;

import uk.co.alt236.apkdetails.print.file.FileWriter;

public class Logger {
    private static Logger instance;

    private final FileWriter fileWriter;

    private Logger(Builder builder) {
        fileWriter = builder.fileWriter;
    }

    public synchronized static Logger get() {
        if (instance == null) {
            new Builder().build();
        }
        return instance;
    }

    public void out(final String text) {
        System.out.println(text);

        if (fileWriter != null) {
            fileWriter.outln(text);
        }
    }

    public void err(final String text) {
        System.err.println(text);

        if (fileWriter != null) {
            fileWriter.errln(text);
        }
    }

    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

    public static final class Builder {
        private FileWriter fileWriter;

        public Builder() {
        }

        public Builder withFileWriter(FileWriter val) {
            fileWriter = val;
            return this;
        }

        public void build() {
            if (instance != null) {
                instance.close();
            }
            instance = new Logger(this);
        }
    }
}
