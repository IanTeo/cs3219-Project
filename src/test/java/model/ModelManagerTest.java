package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import util.PaperBuilder;

public class ModelManagerTest {
    private final Model model = new ModelManager();
    private static final Author foo = new Author("1", "foo");
    private static final Author bar = new Author("BaR");
    private static final Author barWithId = new Author("2", "bar");
    private static final Author barWithPaper = new Author("BaR");
    private static final Paper paper = new PaperBuilder().withId("1").withAuthors(barWithPaper).build();

    static {
        barWithPaper.addPaper(paper);
    }

    @Test
    public void author() {
        // hasAuthor() and addAuthor(Author) adds by using id if it is present.
        model.addAuthor(foo);
        assertTrue(model.hasAuthor("1"));
        assertFalse(model.hasAuthor("foo"));

        // addAuthor(Author) adds author with same names but different id
        model.addAuthor(bar);
        model.addAuthor(barWithId);
        assertTrue(model.hasAuthor("BaR"));
        assertTrue(model.hasAuthor("2"));

        // Authors-related functions are case-insensitive
        model.addAuthor(barWithPaper);
        assertTrue(model.hasAuthor("bar"));
        assertEquals(bar, model.getAuthor("BAR"));

        // addAuthor(Author) doesn't add authors with same identifiers
        Author repeatId = new Author("2", "foo");
        model.addAuthor(repeatId);
        assertEquals(barWithId, model.getAuthor("2"));
        assertFalse(model.hasAuthor("foo"));
    }
}