package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.FileInfo;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

import java.io.File;

public class FileInfoOutput implements Output {

    @Override
    public void output(SectionedKvPrinter printer, File file) {
        final FileInfo apkContents = new FileInfo(file);

        printer.add("File Info");
        printer.startKeyValueSection();
        printer.addKv("APK", apkContents.getPath());
        printer.addKv("MD5", apkContents.getMd5());
        printer.addKv("SHA1", apkContents.getSha1());
        printer.endKeyValueSection();
    }
}
