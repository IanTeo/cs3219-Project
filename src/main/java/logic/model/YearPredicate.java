package logic.model;

import java.util.function.Predicate;

/**
 * Tests that a {@code Paper}'s {@code year} falls within the range of {@code startYear} and {@code endYear}.
 */
public class YearPredicate implements Predicate<Integer> {
    private final YearRange yearRange;

    public YearPredicate(YearRange yearRange) {
        this.yearRange = yearRange;
    }

    @Override
    public boolean test(Integer num) {
        if (yearRange.hasStartYear() && yearRange.hasEndYear()) {
            return num >= yearRange.getStartYear() && num <= yearRange.getEndYear();
        } else if (yearRange.hasStartYear()) {
            return num >= yearRange.getStartYear();
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof YearPredicate)) {
            return false;
        }

        YearPredicate otherPredicate = (YearPredicate) other;
        return this.yearRange == otherPredicate.yearRange;
    }
}
