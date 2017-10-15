package model;

import java.util.ArrayList;

public class Author {
    private String id;
    private ArrayList<String> nameVariants;
    private ArrayList<Paper> papers;

    public Author(String id, String name, Paper paper) {
        this.id = id;
        this.nameVariants = new ArrayList<>();
        this.papers = new ArrayList<>();
    }
}
