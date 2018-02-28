package uk.co.alt236.apkdetails.xml;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AndroidXmlValueParserTest {

    private AndroidXmlValueParser cut;

    @Before
    public void setUp() {
        cut = new AndroidXmlValueParser();
    }

    @Test
    public void parseLongValue() throws Exception {
        // ERROR CASES
        assertEquals(-1, cut.parseLong(""));
        assertEquals(-1, cut.parseLong("aaa"));

        // HAPPY CASES
        assertEquals(Long.MAX_VALUE, cut.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, cut.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void parseIntValue() throws Exception {
        // ERROR CASES
        assertEquals(-1, cut.parseInt(""));
        assertEquals(-1, cut.parseInt("aaa"));

        // HAPPY CASES
        assertEquals(Integer.MAX_VALUE, cut.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, cut.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void parseBooleanValue() throws Exception {
        // HAPPY CASES
        assertEquals(false, cut.parseBoolean("false"));
        assertEquals(true, cut.parseBoolean("true"));

        assertEquals(true, cut.parseBoolean("-1"));
        assertEquals(false, cut.parseBoolean("0"));
    }

    @Test
    public void parseStringValue() throws Exception {

        assertEquals("", cut.parseString(null));
        assertEquals("", cut.parseString(""));
        assertEquals("foo", cut.parseString("foo"));
    }

}