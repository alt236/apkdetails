package uk.co.alt236.apkdetails.repo;

import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

import java.util.List;
import java.util.stream.Collectors;

public class ContentSizeRepository {

    private final ZipContents zipContents;

    public ContentSizeRepository(ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    public List<Entry> getLargestFiles(int noOfFiles) {
        return zipContents
                .getEntries()
                .stream()
                .sorted((f1, f2) -> Long.compare(f2.getFileSize(), f1.getFileSize()))
                .limit(noOfFiles)
                .collect(Collectors.toList());
    }

    public List<Entry> getLargestResources(int noOfFiles) {
        return zipContents
                .getEntries(entry -> entry.getName().startsWith("res/"))
                .stream()
                .sorted((f1, f2) -> Long.compare(f2.getFileSize(), f1.getFileSize()))
                .limit(noOfFiles)
                .collect(Collectors.toList());
    }
}
