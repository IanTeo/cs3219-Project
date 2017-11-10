package model;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.junit.Test;
import util.AssertUtil;
import util.PaperBuilder;
import util.SampleData;

public class PaperTest {
    @Test
    public void test() {
        Author[] authors = { SampleData.AUTHOR_1, SampleData.AUTHOR_2 };
        Paper noVenue = new Paper("1", "Preserve CaSe", 2011, authors, "");
        assertEquals("1", noVenue.getId());
        assertEquals("Preserve CaSe", noVenue.getTitle());
        assertEquals(2011, noVenue.getYear());
        assertTrue(noVenue.getAuthors().size() == 2);
        assertEquals("<venue unspecified>", noVenue.getVenue());

        Paper withVenue = new Paper("2", "", 0, new Author[0], "vEnuE");
        assertEquals("2", withVenue.getId());
        assertEquals("", withVenue.getTitle());
        assertEquals(0, withVenue.getYear());
        assertTrue(withVenue.getAuthors().size() == 0);
        assertEquals("vEnuE", withVenue.getVenue());

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("id", "1");
        expectedJson.put("title", "Preserve CaSe");
        expectedJson.put("year", 2011);
        expectedJson.put("authors", "author with papers P1 P2, author with papers P2 P3 P4");
        expectedJson.put("venue", "<venue unspecified>");
        expectedJson.put("citationCount", 0);
        JSONObject actualJson = noVenue.toJson();
        assertEquals(expectedJson, actualJson);
    }
}
