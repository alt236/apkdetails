package uk.co.alt236.apkdetails.util.date;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class IsoISO8601DateParserTest {


    @Test
    public void test1() {
        final Date date = new Date(1511738666196L);
        assertEquals("2017-11-26T23:24:26+0000", IsoISO8601DateParser.toIsoDateString(date));
    }

    @Test
    public void test2() {
        final Date date = new Date(Integer.MAX_VALUE * 1000L);
        assertEquals("2038-01-19T03:14:07+0000", IsoISO8601DateParser.toIsoDateString(date));
    }

    @Test
    public void test3() {
        final Date date = new Date(0);
        assertEquals("1970-01-01T00:00:00+0000", IsoISO8601DateParser.toIsoDateString(date));
    }
}