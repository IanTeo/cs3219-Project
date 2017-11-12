package logic.command;

import org.junit.Before;
import org.junit.Test;

import logic.exception.ParseException;
import logic.filter.Filter;
import logic.model.Category;
import logic.model.Measure;
import model.Model;
import model.Paper;
import util.FileReader;
import util.ModelStub;
import util.SampleData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebCommandTest {
    private static final String BASE_URL = "WebCommandTest/%s";

    @Test
    public void execute_validLevel_printJson() throws Exception {
        assertCommand(4, SampleData.PAPER_3, String.format(BASE_URL, "Web_ValidLevel4TestResult.json"));
        assertCommand(2, SampleData.PAPER_3, String.format(BASE_URL, "Web_ValidLevel2TestResult.json"));
    }
    
    private void assertCommand(int level, Paper paper, String expectedOutputFileName) throws Exception {
        WebCommand command = new WebCommand(level, paper);

        String actual = command.execute();
        String expected = FileReader.readFile(expectedOutputFileName);
        assertEquals(expected, actual);
    }
}
