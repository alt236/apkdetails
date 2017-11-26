package uk.co.alt236.apkdetails.util.date;

import org.junit.Test;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class UtcDateFormatterTest {

    @Test
    public void testConstructor1() {
        final DateFormat dateFormat = new UtcDateFormatter("yyyy");
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testConstructor2() {
        final DateFormat dateFormat = new UtcDateFormatter("yyyy", new DateFormatSymbols());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testConstructor3() {
        final DateFormat dateFormat = new UtcDateFormatter("yyyy", Locale.CHINESE);
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZone_invalid() {
        final DateFormat dateFormat = new UtcDateFormatter("yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    public void testSetTimeZone_valid() {
        final DateFormat dateFormat = new UtcDateFormatter("yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}