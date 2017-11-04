package model;

import logic.exception.ParseException;

public enum DataAttributes {
    PAPER_TITLE, AUTHOR_NAME, YEAR, VENUE, IN_CITATION, OUT_CITATION;

    public static DataAttributes getEnum(String attribute) throws ParseException {
        switch (attribute.toLowerCase()) {
            case "title" :
            case "papertitle" :
            case "paper_title" :
                return PAPER_TITLE;

            case "name" :
            case "authorname" :
            case "author_name" :
                return AUTHOR_NAME;

            case "year" :
                return YEAR;

            case "venue" :
                return VENUE;

            case "incitation" :
            case "in_citation" :
                return IN_CITATION;

            case "outcitation" :
            case "out_citation" :
                return OUT_CITATION;

            default :
                throw new ParseException("Attribute not found");
        }
    }
}
