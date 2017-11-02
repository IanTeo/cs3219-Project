package logic.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Model;
import model.Paper;

import java.util.HashMap;

public class WebCommandTest {
    private final WebCommand command = new WebCommand();
    private final Model model = new Model();

    private static final Paper PAPER_ONE = new Paper.PaperBuilder().withTitle("One").withId("1").build();
    private static final Paper PAPER_TWO = new Paper.PaperBuilder().withTitle("Two").withId("2").build();
    private static final Paper PAPER_THREE = new Paper.PaperBuilder().withTitle("Three").withId("3").build();
    private static final Paper PAPER_FOUR = new Paper.PaperBuilder().withTitle("Four").withId("4").build();
    private static final Paper PAPER_FIVE = new Paper.PaperBuilder().withTitle("Five").withId("5").build();

    static {
        PAPER_TWO.addCitation(PAPER_ONE);
        PAPER_THREE.addCitation(PAPER_ONE);
        PAPER_THREE.addCitation(PAPER_TWO);
        PAPER_FOUR.addCitation(PAPER_THREE);
        PAPER_FIVE.addCitation(PAPER_FOUR);
    }

    @Test
    public void execute() throws Exception {
        /*
        Verifies the following cases:
          1. Papers beyond Level 3 are not included.
          2. Repeated Papers are not included.
          3. If a Paper falls under 2 Levels, the Level with a lower value is chosen.
        For a visual of the nodes and links, see under test/res/webcommand.png
         */
        setUp();
        HashMap<String, String> argumentMap = new HashMap<>();
        argumentMap.put("count", "3");
        argumentMap.put("paper", "1");
        command.setParameters(model, argumentMap);
        assertEquals("{\"nodes\":[{\"citationCount\":2,\"venue\":\"\",\"year\":0,\"id\":\"1\",\"title\":\"One\",\"authors\":\"\",\"group\":1}," +
                "{\"citationCount\":1,\"venue\":\"\",\"year\":0,\"id\":\"2\",\"title\":\"Two\",\"authors\":\"\",\"group\":2}," +
                "{\"citationCount\":1,\"venue\":\"\",\"year\":0,\"id\":\"3\",\"title\":\"Three\",\"authors\":\"\",\"group\":2}," +
                "{\"citationCount\":1,\"venue\":\"\",\"year\":0,\"id\":\"4\",\"title\":\"Four\",\"authors\":\"\",\"group\":3}]," +
                "\"links\":[{\"source\":\"1\",\"target\":\"2\"},{\"source\":\"1\",\"target\":\"3\"},{\"source\":\"2\"," +
                "\"target\":\"3\"},{\"source\":\"3\",\"target\":\"4\"}]}", command.execute());
    }

    private void setUp() {
        model.addPaper(PAPER_ONE);
        model.addPaper(PAPER_TWO);
        model.addPaper(PAPER_THREE);
        model.addPaper(PAPER_FOUR);
        model.addPaper(PAPER_FIVE);
    }
}
