package logic.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Model;
import model.Paper;

import java.util.HashMap;
import java.util.Map;

public class CountYearCommandTest {
    private final CountYearCommand command = new CountYearCommand();
    private final Model model = new Model();

    private static final Paper PAPER_ONE = new Paper.PaperBuilder().withVenue("Singapore").withYear(2000).withId("1").build(); // valid
    private static final Paper PAPER_TWO = new Paper.PaperBuilder().withYear(2000).withId("2").build(); // missing venue
    private static final Paper PAPER_THREE = new Paper.PaperBuilder().withVenue("Malaysia").withYear(2001).withId("3").build(); // different country
    private static final Paper PAPER_FOUR = new Paper.PaperBuilder().withVenue("Singapore").withId("4").build(); // missing year
    private static final Paper PAPER_FIVE = new Paper.PaperBuilder().withVenue("Singapore").withYear(2004).withId("5").build(); // valid
    private static final Paper PAPER_SIX = new Paper.PaperBuilder().withVenue("Singapore").withYear(2005).withId("6").build(); // year out of bounds

    @Test
    public void test() throws Exception {
        model.addPaper(PAPER_ONE);
        model.addPaper(PAPER_TWO);
        model.addPaper(PAPER_THREE);
        model.addPaper(PAPER_FOUR);
        model.addPaper(PAPER_FIVE);
        model.addPaper(PAPER_SIX);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("year", "1999-2004");
        paramMap.put("venue", "Singapore");
        command.setParameters(model, paramMap);
        assertEquals("[{\"year\":1999,\"count\":0},{\"year\":2000,\"count\":1},{\"year\":2001,\"count\":0}" +
                ",{\"year\":2002,\"count\":0},{\"year\":2003,\"count\":0},{\"year\":2004,\"count\":1}]", command.execute());
    }

}
