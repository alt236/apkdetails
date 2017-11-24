package uk.co.alt236.apkdetails.util;

import org.apache.commons.io.FileUtils;

public class FileSizeFormatter {

    private final boolean format;

    public FileSizeFormatter(final boolean format) {
        this.format = format;
    }

    public String format(final long size) {
        if (format) {
            return FileUtils.byteCountToDisplaySize(size);
        } else {
            return String.valueOf(size);
        }
    }
}
