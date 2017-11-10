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
    private static final Paper paper = new PaperBuilder()
            .withId("1").withTitle("paper1").withAuthors(barWithPaper).build();

    static {
        barWithPaper.addPaper(paper);
    }

    @Test
    public void test() {
        // hasAuthor() and addAuthor() adds by using id if it is present.
        model.addAuthor(foo);
        assertTrue(model.hasAuthor("1"));
        assertFalse(model.hasAuthor("foo"));

        // addAuthor() adds author with same names but different id
        model.addAuthor(bar);
        model.addAuthor(barWithId);
        assertTrue(model.hasAuthor("BaR"));
        assertTrue(model.hasAuthor("2"));

        // addAuthor() does not add duplicate author
        model.addAuthor(foo);
        assertTrue(model.getAuthors().size() == 3);

        // Authors-related functions are case-insensitive
        model.addAuthor(barWithPaper);
        assertTrue(model.hasAuthor("bar"));
        assertEquals(bar, model.getAuthor("BAR"));

        // addAuthor() doesn't add authors with same identifiers
        Author repeatId = new Author("2", "foo");
        model.addAuthor(repeatId);
        assertEquals(barWithId, model.getAuthor("2"));
        assertFalse(model.hasAuthor("foo"));

        // hasPaper() can search by id or title
        model.addPaper(paper);
        assertTrue(model.hasPaper("1"));
        assertTrue(model.hasPaper("paper1"));

        // getPaper() able to get correct paper
        assertEquals(paper, model.getPaper("1"));
        assertEquals(paper, model.getPaper("pAPEr1"));

        // addPaper() does not add duplicate paper
        model.addPaper(paper);
        assertTrue(model.getPapers().size() == 1);
    }
}