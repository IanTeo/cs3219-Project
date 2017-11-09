package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void containsIgnoreCaseVenue() {
        // same letters different casing - match found
        assertTrue(StringUtil.containsIgnoreCaseVenue("fOo", "FoO"));
        assertTrue(StringUtil.containsIgnoreCaseVenue("FoO", "fOo"));

        // one of the strings can be found in the other string after it is delimited by whitespaces and punctuation - match found
        assertTrue(StringUtil.containsIgnoreCaseVenue("  bar @ FOO @ BAZ  ", "foo"));
        assertTrue(StringUtil.containsIgnoreCaseVenue("FOO", "BAR @ foo @ baz "));

        // none of the strings can be found in the other string after it is delimited by whitespaces and punctuation - no match found
        assertFalse(StringUtil.containsIgnoreCaseVenue("ic", "icSE"));
        assertFalse(StringUtil.containsIgnoreCaseVenue("icSE", "ic"));

        // empty string present - no match found
        assertFalse(StringUtil.containsIgnoreCaseVenue("foo", ""));
        assertFalse(StringUtil.containsIgnoreCaseVenue("", "foo"));
    }
}
