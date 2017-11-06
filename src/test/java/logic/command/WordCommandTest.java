package logic.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Model;
import model.Paper;

public class WordCommandTest {
    private final Model model = new Model();
    private final WordCommand wordCommand = new WordCommand();

    private static final Paper PAPER_ONE = new Paper.PaperBuilder().withTitle("ian@ian/ian ian CAPS caps caps").withId("1").build();
    private static final Paper PAPER_TWO = new Paper.PaperBuilder().withTitle("i i i")
            .withVenue("Foo Foo Foo").withId("2").build(); // title doesn't fit but venue fits
    private static final Paper PAPER_THREE = new Paper.PaperBuilder().withId("3").build(); // missing title

    @Test
    public void test() throws Exception {
        setUp();
        wordCommand.setParameters(model, "2 title");
        assertEquals("[{\"size\":100,\"count\":4,\"word\":\"ian\"},{\"size\":75,\"count\":3,\"word\":\"caps\"}]", wordCommand.execute());

        wordCommand.setParameters(model, "1 title");
        assertEquals("[{\"size\":100,\"count\":4,\"word\":\"ian\"}]", wordCommand.execute());

        wordCommand.setParameters(model, "0 title");
        assertEquals("[]", wordCommand.execute());
    }

    private void setUp() {
        model.addPaper(PAPER_ONE);
        model.addPaper(PAPER_TWO);
        model.addPaper(PAPER_THREE);
    }
}
