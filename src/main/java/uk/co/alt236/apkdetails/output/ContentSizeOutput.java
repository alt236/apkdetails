package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.ContentSizeRepository;
import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
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
    public void output(OutputCollector printer) {
        final ContentSizeRepository contentSizeRepository = new ContentSizeRepository(zipContents);

        listLargestFiles(printer, contentSizeRepository);
        listLargestRes(printer, contentSizeRepository);
    }


    private void listLargestFiles(OutputCollector printer, ContentSizeRepository contentSizeRepository) {
        printer.add("Largest files in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSizeRepository.getLargestFiles(noOfFiles)) {
            printer.addKv(entry.getName(), fileSizeFormatter.format(entry.getFileSize()));
        }

        printer.endKeyValueSection();
        printer.addNewLine();
    }

    private void listLargestRes(OutputCollector printer, ContentSizeRepository contentSizeRepository) {
        printer.add("Largest Resources in APK");
        printer.startKeyValueSection();

        for (final Entry entry : contentSizeRepository.getLargestResources(noOfFiles)) {
            printer.addKv(entry.getName(), fileSizeFormatter.format(entry.getFileSize()));
        }

        printer.endKeyValueSection();
        printer.addNewLine();
    }
}
