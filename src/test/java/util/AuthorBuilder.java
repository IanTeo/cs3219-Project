package util;

import model.Author;
import model.Paper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthorBuilder {
    private String id;
    private String name;

    public AuthorBuilder() {
        this.id = "";
        this.name = "";
    }

    public AuthorBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public AuthorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Author build() {
        Author author = new Author(id, name);
        return author;
    }
}
