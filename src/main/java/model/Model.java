package model;

import java.util.ArrayList;
import java.util.Collection;

public interface Model {
    void addPaper(Paper paper);
    boolean hasPaper(String paper);
    Paper getPaper(String paper);
    Collection<Paper> getPapers();

    void addAuthor(Author author);
    boolean hasAuthor(String author);
    Author getAuthor(String author);
    Collection<Author> getAuthors();

    void clear();
}
