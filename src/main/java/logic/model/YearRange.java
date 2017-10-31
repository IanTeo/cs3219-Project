package logic.model;

import logic.Precondition;

public class YearRange {
    private final int startYear;
    private final int endYear;

    public YearRange() {
        this.startYear = -1;
        this.endYear = -1;
    }

    public YearRange(int startYear) {
        Precondition.checkArgument(startYear >= 0);
        this.startYear = startYear;
        this.endYear = -1;
    }

    public YearRange(int startYear, int endYear) {
        Precondition.checkArgument(startYear >= 0 && endYear >= 0 && startYear < endYear);
        this.startYear = startYear;
        this.endYear = endYear;
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
