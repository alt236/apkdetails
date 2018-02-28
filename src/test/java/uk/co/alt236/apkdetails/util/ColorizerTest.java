package uk.co.alt236.apkdetails.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColorizerTest {
    private static final String INPUT_STRING = "FOOBAR";
    private static final int INPUT_STRING_LENGTH = INPUT_STRING.length();
    private static final int INPUT_STRING_LENGTH_FORMATTED = INPUT_STRING_LENGTH + 9;

    @Test
    public void errorStringEnabledNotWindows() throws Exception {
        final boolean enabled = true;
        final boolean windows = false;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.error(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH_FORMATTED, res.length());
        assertTrue(res.startsWith(Colorizer.ANSI_RED));
        assertTrue(res.endsWith(Colorizer.ANSI_RESET));
    }

    @Test
    public void errorStringEnabledWindows() throws Exception {
        final boolean enabled = true;
        final boolean windows = true;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.error(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH, res.length());
    }

    @Test
    public void errorStringNotEnabledNotWindows() throws Exception {
        final boolean enabled = false;
        final boolean windows = false;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.error(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH, res.length());
    }

    @Test
    public void importantStringEnabledNotWindows() throws Exception {
        final boolean enabled = true;
        final boolean windows = false;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.important(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH_FORMATTED, res.length());
        assertTrue(res.startsWith(Colorizer.ANSI_YELLOW));
        assertTrue(res.endsWith(Colorizer.ANSI_RESET));
    }

    @Test
    public void importantStringEnabledWindows() throws Exception {
        final boolean enabled = true;
        final boolean windows = true;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.important(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH, res.length());
    }

    @Test
    public void importantStringNotEnabledNotWindows() throws Exception {
        final boolean enabled = false;
        final boolean windows = false;
        final Colorizer cut = new Colorizer(enabled, windows);

        final String res = cut.important(INPUT_STRING);
        assertEquals(INPUT_STRING_LENGTH, res.length());
    }

}