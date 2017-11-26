package uk.co.alt236.apkdetails.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileSizeFormatterTest {

    @Test
    public void formatsWhenEnabled() {
        final FileSizeFormatter formatter = new FileSizeFormatter(true);

        assertEquals("0 bytes", formatter.format(0));
        assertEquals("7 EB", formatter.format(Long.MAX_VALUE));
        assertEquals("976 KB", formatter.format(1000000));
        assertEquals("1 MB", formatter.format(1048576));
    }

    @Test
    public void doesNotFormatWhenDisabled() {
        final FileSizeFormatter formatter = new FileSizeFormatter(false);

        assertEquals("0", formatter.format(0));
        assertEquals(String.valueOf(Long.MAX_VALUE), formatter.format(Long.MAX_VALUE));
        assertEquals(String.valueOf(1000000), formatter.format(1000000));
        assertEquals(String.valueOf(1048576), formatter.format(1048576));
    }
}