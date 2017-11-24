package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.ContentSize;
import uk.co.alt236.apkdetails.model.common.Entry;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

public class ContentSizeOutput implements Output {
    private final ZipContents zipContents;
    private final int noOfFiles;

    public ContentSizeOutput(final ZipContents zipContents,
                             final int noOfFiles) {
        this.zipContents = zipContents;
        this.noOfFiles = noOfFiles;
    }

    @Override
    public void output(SectionedKvPrinter printer) {
        final ContentSize contentSize = new ContentSize(zipContents);

        printer.add("Largest files in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSize.getLargestFiles(noOfFiles)) {
            printer.addKv(entry.getName(), entry.getFileSize());
        }

        printer.endKeyValueSection();
        printer.addNewLine();

        printer.add("Largest res in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSize.getLargestResources(noOfFiles)) {
            printer.addKv(entry.getName(), entry.getFileSize());
        }

        printer.endKeyValueSection();
    }
}
