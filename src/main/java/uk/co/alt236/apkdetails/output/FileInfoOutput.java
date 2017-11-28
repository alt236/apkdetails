package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.FileInfoRepository;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;

public class FileInfoOutput implements Output {
    private final File file;
    private final FileSizeFormatter fileSizeFormatter;

    public FileInfoOutput(File file,
                          FileSizeFormatter fileSizeFormatter) {
        this.file = file;
        this.fileSizeFormatter = fileSizeFormatter;
    }

    @Override
    public void output(OutputCollector printer) {
        final FileInfoRepository apkContents = new FileInfoRepository(file);

        printer.add("File Info");
        printer.startKeyValueSection();
        printer.addKv("Path", apkContents.getPath());
        printer.addKv("Size", fileSizeFormatter.format(apkContents.getFileSize()));
        printer.addKv("MD5", apkContents.getMd5());
        printer.addKv("SHA1", apkContents.getSha1());
        printer.addKv("SHA256", apkContents.getSha256());
        printer.endKeyValueSection();
    }
}
