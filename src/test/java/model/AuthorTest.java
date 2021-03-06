package model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.junit.Test;
import util.PaperBuilder;

public class AuthorTest {
    private static final Paper PAPER = new PaperBuilder().withId("123").build();

    @Test
    public void test() {
        Set<Author> authors = new HashSet<>();

        // unique identifiers
        Author baz = new Author("123", "baz");
        Author bar = new Author("bar");
        authors.addAll(Arrays.asList(baz, bar));
        assertEquals("123", baz.getId());
        assertEquals("bar", bar.getId());
        assertEquals(new HashSet<>(Arrays.asList(baz, bar)), authors);

        // authors are different if they have different papers
        Author bazWithPaper = new Author("123", "baz");
        bazWithPaper.addPaper(PAPER);
        authors.add(bazWithPaper);
        assertEquals("123", bazWithPaper.getId());
        assertEquals(new HashSet<>(Arrays.asList(bar, baz, bazWithPaper)), authors);

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("id", "123");
        expectedJson.put("name", "baz");
        expectedJson.put("paperCount", 1);
        JSONObject actualJson = bazWithPaper.toJson();
        assertEquals(expectedJson, actualJson);
    }
}
