package uk.co.alt236.apkdetails.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInfo {
    private final File file;

    public FileInfo(File file) {
        this.file = file;
    }

    public long getFileSize() {
        return file.length();
    }

    public String getMd5() {
        String retVal;
        try {
            FileInputStream fis = new FileInputStream(file);
            retVal = DigestUtils.md5Hex(fis);
            fis.close();
        } catch (IOException e) {
            retVal = "";
        }

        return retVal;
    }

    public String getSha1() {
        String retVal;
        try {
            FileInputStream fis = new FileInputStream(file);
            retVal = DigestUtils.sha1Hex(fis);
            fis.close();
        } catch (IOException e) {
            retVal = "";
        }

        return retVal;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
}
