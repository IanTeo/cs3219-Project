package util;

import model.Author;
import model.Paper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PaperBuilder {
    private String id;
    private String title;
    private int year;
    private Author[] authors;
    private String venue;

    public PaperBuilder() {
        id = "";
        title = "";
        year = 0;
        authors = new Author[0];
        venue = "";
    }

    public PaperBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public PaperBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PaperBuilder withYear(int year) {
        this.year = year;
        return this;
    }

    public PaperBuilder withAuthors(Author... authors) {
        this.authors = authors;
        return this;
    }

    public PaperBuilder withVenue(String venue) {
        this.venue = venue;
        return this;
    }

    public Paper build() {
        Paper paper = new Paper(id, title, year, authors, venue);
        return paper;
    }
}
