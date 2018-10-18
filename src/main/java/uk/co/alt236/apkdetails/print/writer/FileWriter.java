package uk.co.alt236.apkdetails.print.writer;

import uk.co.alt236.apkdetails.util.StreamUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter implements Writer {

    private final Path path;
    private final BufferedWriter writer;

    public FileWriter(File file) {
        this(file.getAbsolutePath());
    }

    public FileWriter(String outputFilePath) {
        this.path = Paths.get(outputFilePath);
        try {
            writer = Files.newBufferedWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void outln(final String string) {
        writeln(string);
    }

    @Override
    public void errln(String string) {
        writeln(string);
    }

    private synchronized void writeln(final String string) {
        try {
            writer.write(string);
            writer.newLine();
        } catch (IOException e) {
            StreamUtils.close(writer);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        try {
            writer.flush();
            StreamUtils.close(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtils.close(writer);
        }
    }
}
