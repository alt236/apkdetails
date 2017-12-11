package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.print.file.FileWriter;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;

public class FilesOutputter {

    private final FileSizeFormatter fileSizeFormatter;
    private final Colorizer colorizer;

    public FilesOutputter(final FileSizeFormatter fileSizeFormatter,
                          final Colorizer colorizer) {
        this.fileSizeFormatter = fileSizeFormatter;
        this.colorizer = colorizer;
    }

    public void doOutput(FilePathFactory filePathFactory,
                         ZipContents zipContents,
                         AndroidManifestRepository manifestRepository,
                         boolean verbose) {

        saveManifest(filePathFactory, manifestRepository);
    }


    private void saveManifest(final FilePathFactory filePathFactory,
                              final AndroidManifestRepository repository) {
        final File manifestFile = filePathFactory.getManifestOutputPath();
        if (manifestFile != null) {
            Logger.get().out("Saving manifest as " + manifestFile);
            FileWriter writer = new FileWriter(manifestFile);
            String xml;

            try {
                xml = repository.getXml();
            } catch (IllegalStateException e) {
                xml = e.getMessage();
            }

            writer.outln(xml);
            writer.close();
        }
    }
}
