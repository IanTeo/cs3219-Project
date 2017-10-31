package logic.model;

import java.time.Year;
import java.util.stream.IntStream;

import logic.Precondition;

public class YearRange {
    private final int startYear;
    private final int endYear;

    public YearRange() {
        this.startYear = -1;
        this.endYear = -1;
    }

    public YearRange(int startYear) {
        Precondition.checkArgument(startYear >= 0 && startYear <= Year.now().getValue());
        this.startYear = startYear;
        this.endYear = -1;
    }

    public YearRange(int startYear, int endYear) {
        Precondition.checkArgument(startYear >= 0 && startYear <= endYear && endYear <= Year.now().getValue());
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public IntStream stream() {
        if (startYear != -1 && endYear != -1) {
            return IntStream.rangeClosed(startYear, endYear);
        } else if (startYear != -1) {
            return IntStream.rangeClosed(startYear, Year.now().getValue());
        } else {
            return IntStream.empty();
        }
    }

    public boolean hasStartYear() {
        return startYear != -1;
    }

    public boolean hasEndYear() {
        return endYear != -1;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }
}
