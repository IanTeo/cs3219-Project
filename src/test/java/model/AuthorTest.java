package model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class AuthorTest {
    private static final Paper PAPER = new Paper.PaperBuilder().withId("123").build();

    @Test
    public void hashcode() {
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
    }
}
