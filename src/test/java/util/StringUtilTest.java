package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import logic.model.Category;
import logic.utility.MapUtility;
import model.Paper;
import org.junit.Test;

import java.util.*;

public class StringUtilTest {
    @Test
    public void containsIgnoreCaseVenue() {
        // same letters different casing - match found
        assertTrue(StringUtil.containsMatchIgnoreCaseAndPunctuation("fOo", "FoO"));
        assertTrue(StringUtil.containsMatchIgnoreCaseAndPunctuation("FoO", "fOo"));

        // searchStr has exact match in the given str, delimited by white space and punctuation
        assertTrue(StringUtil.containsMatchIgnoreCaseAndPunctuation("  bar @ FOO @ BAZ  ", "foo"));

        // The opposite of above should not work. searchStr cannot be found in str
        assertFalse(StringUtil.containsMatchIgnoreCaseAndPunctuation("FOO", "BAR @ foo @ baz "));

        // none of the strings can be found in the other string after it is delimited by whitespaces and punctuation - no match found
        assertFalse(StringUtil.containsMatchIgnoreCaseAndPunctuation("ic", "icSE"));
        assertFalse(StringUtil.containsMatchIgnoreCaseAndPunctuation("icSE", "ic"));

        // empty string present - no match found
        assertFalse(StringUtil.containsMatchIgnoreCaseAndPunctuation("foo", ""));
        assertFalse(StringUtil.containsMatchIgnoreCaseAndPunctuation("", "foo"));
    }
}
