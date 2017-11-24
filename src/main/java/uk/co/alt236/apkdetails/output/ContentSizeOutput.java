package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.ContentSize;
import uk.co.alt236.apkdetails.model.common.Entry;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

public class ContentSizeOutput implements Output {
    private final ZipContents zipContents;
    private final FileSizeFormatter fileSizeFormatter;
    private final int noOfFiles;

    public ContentSizeOutput(final ZipContents zipContents,
                             final FileSizeFormatter fileSizeFormatter,
                             final int noOfFiles) {
        this.fileSizeFormatter = fileSizeFormatter;
        this.zipContents = zipContents;
        this.noOfFiles = noOfFiles;
    }

    @Override
    public void output(SectionedKvPrinter printer) {
        final ContentSize contentSize = new ContentSize(zipContents);

        listLargestFiles(printer, contentSize);
        listLargestRes(printer, contentSize);
    }


    private void listLargestFiles(SectionedKvPrinter printer, ContentSize contentSize) {
        printer.add("Largest files in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSize.getLargestFiles(noOfFiles)) {
            printer.addKv(entry.getName(), fileSizeFormatter.format(entry.getFileSize()));
        }

        printer.endKeyValueSection();
        printer.addNewLine();
    }

    private void listLargestRes(SectionedKvPrinter printer, ContentSize contentSize) {
        printer.add("Largest Resources in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSize.getLargestResources(noOfFiles)) {
            printer.addKv(entry.getName(), fileSizeFormatter.format(entry.getFileSize()));
        }

        printer.endKeyValueSection();
        printer.addNewLine();
    }
}
