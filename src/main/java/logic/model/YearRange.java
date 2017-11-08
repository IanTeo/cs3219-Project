package logic.model;

import java.time.Year;
import java.util.stream.IntStream;

import logic.Precondition;

/**
 * Represents the range of years.
 */
public class YearRange {
    private static final int EARLIEST_START_YEAR = 1991;

    private final int startYear;
    private final int endYear;

    public YearRange(int startYear) {
        Precondition.checkArgument(startYear >= EARLIEST_START_YEAR && startYear <= Year.now().getValue());
        this.startYear = startYear;
        this.endYear = startYear;
    }

    public YearRange(int startYear, int endYear) {
        Precondition.checkArgument(startYear >= EARLIEST_START_YEAR && startYear <= endYear && endYear <= Year.now().getValue());
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
