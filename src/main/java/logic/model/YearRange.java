package logic.model;

import java.time.Year;
import java.util.stream.IntStream;

import logic.Precondition;

/**
 * Represents the range of years.
 */
public class YearRange {
    private final int startYear;
    private final int endYear;

    public YearRange(String yearString) {
        String[] years = yearString.split("-");
        this.startYear = Integer.parseInt(years[0]);
        this.endYear = Integer.parseInt(years[1]);
    }

    public YearRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public IntStream stream() {
        return IntStream.rangeClosed(startYear, endYear);
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }
}
