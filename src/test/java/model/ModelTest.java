package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModelTest {
    private final Model model = new Model();
    private final Author foo = new Author("1", "foo");
    private final Author bar = new Author("bar");
    private final Author barWithId = new Author("2", "bar");

    @Test
    public void author() {
        // hasAuthor() and addAuthor(Author) adds by using id if it is present.
        model.addAuthor(foo);
        assertTrue(model.hasAuthor("1"));
        assertFalse(model.hasAuthor("foo"));

        // addAuthor(Author) adds author with same names but different id
        model.addAuthor(bar);
        model.addAuthor(barWithId);
        assertTrue(model.hasAuthor("2"));
        assertTrue(model.hasAuthor("bar"));

        // addAuthor(Author) doesn't add authors with same identifiers
        Author repeatId = new Author("2", "foo");
        model.addAuthor(repeatId);
        assertEquals(barWithId, model.getAuthor("2"));
        assertFalse(model.hasAuthor("foo"));
    }
}