package model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import util.PaperBuilder;

public class PaperTest {
    @Test
    public void hashcode() {
        Set<Paper> papers = new HashSet<>();

        Paper one = new PaperBuilder().withId("1").build();
        Paper oneWithOtherThings = new PaperBuilder().withId("1").withVenue("foo").build();
        Paper two = new PaperBuilder().withId("2").build();
        papers.addAll(Arrays.asList(one, oneWithOtherThings, two));
        assertEquals(new HashSet<>(Arrays.asList(one, oneWithOtherThings, two)), papers);
    }
}
