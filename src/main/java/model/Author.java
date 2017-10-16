package model;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String id;
    private List<String> nameVariants;
    private List<Paper> papers;

    public Author(String id, String name, Paper paper) {
        this.id = id;
        this.nameVariants = new ArrayList<>();
        this.papers = new ArrayList<>();
    }
}
