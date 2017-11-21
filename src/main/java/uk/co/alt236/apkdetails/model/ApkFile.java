package uk.co.alt236.apkdetails.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ApkFile {
    private final String path;

    public ApkFile(String path) {
        this.path = path;
    }

    public String getMd5() {
        String retVal;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
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
            FileInputStream fis = new FileInputStream(new File(path));
            retVal = DigestUtils.sha1Hex(fis);
            fis.close();
        } catch (IOException e) {
            retVal = "";
        }

        return retVal;
    }

    public String getPath() {
        return path;
    }
}
