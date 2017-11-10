package util;

import model.Author;
import model.Paper;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AssertUtil {
    public static void assertPaperEquals(Paper expected, Paper actual) {
        Set<String> expectedInCitationId = expected.getInCitation().stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> actualInCitationId = actual.getInCitation().stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> expectedOutCitationId = expected.getOutCitation().stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> actualOutCitationId = actual.getOutCitation().stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> expectedAuthorId = expected.getAuthors().stream().map(Author::getId).collect(Collectors.toSet());
        Set<String> actualAuthorId = actual.getAuthors().stream().map(Author::getId).collect(Collectors.toSet());

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getVenue(), actual.getVenue());
        assertEquals(expectedInCitationId, actualInCitationId);
        assertEquals(expectedOutCitationId, actualOutCitationId);
        assertEquals(expectedAuthorId, actualAuthorId);
    }

    public static void assertAuthorEquals(Author expected, Author actual) {
        Set<String> expectedPapersId = expected.getPapers().stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> actualPapersId = actual.getPapers().stream().map(Paper::getId).collect(Collectors.toSet());

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expectedPapersId, actualPapersId);
    }
}
