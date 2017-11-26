package uk.co.alt236.apkdetails.repo;

import uk.co.alt236.apkdetails.util.Hasher;

import java.io.File;

public class FileInfoRepository {
    private final File file;
    private final Hasher hasher;

    public FileInfoRepository(File file) {
        this.file = file;
        this.hasher = new Hasher();
    }

    public long getFileSize() {
        return file.length();
    }

    public String getMd5() {
        return hasher.md5Hex(file);
    }

    public String getSha1() {
        return hasher.sha1Hex(file);
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
}
