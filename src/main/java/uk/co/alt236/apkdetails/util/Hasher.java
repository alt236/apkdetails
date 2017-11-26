package uk.co.alt236.apkdetails.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Hasher {

    public String md5Hex(final byte[] input) {
        return DigestUtils.md5Hex(input);
    }

    public String md5Hex(final File input) {
        String retVal;
        try {
            FileInputStream fis = new FileInputStream(input);
            retVal = DigestUtils.md5Hex(fis);
            fis.close();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return retVal;
    }

    public String sha1Hex(final byte[] input) {
        return DigestUtils.sha1Hex(input);
    }

    public String sha1Hex(final File input) {
        String retVal;
        try {
            FileInputStream fis = new FileInputStream(input);
            retVal = DigestUtils.sha1Hex(fis);
            fis.close();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return retVal;
    }

}
