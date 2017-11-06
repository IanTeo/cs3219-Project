package logic.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Model;
import model.Paper;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("type", "title");
        wordCommand.setParameters(model, paramMap);
        assertEquals("[{\"size\":100,\"count\":4,\"word\":\"ian\"},{\"size\":75,\"count\":3,\"word\":\"caps\"}]", wordCommand.execute());

        paramMap.put("count", "1");
        wordCommand.setParameters(model, paramMap);
        assertEquals("[{\"size\":100,\"count\":4,\"word\":\"ian\"}]", wordCommand.execute());

        paramMap.put("count", "0");
        wordCommand.setParameters(model, paramMap);
        assertEquals("[]", wordCommand.execute());
    }

    private void setUp() {
        model.addPaper(PAPER_ONE);
        model.addPaper(PAPER_TWO);
        model.addPaper(PAPER_THREE);
    }
}