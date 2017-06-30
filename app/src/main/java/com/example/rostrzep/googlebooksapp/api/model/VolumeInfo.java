package com.example.rostrzep.googlebooksapp.api.model;

import java.io.Serializable;
import java.util.List;

public class VolumeInfo implements Serializable {

    private final String title;
    private final String subtitle;
    private final List<String> authors;
    private final String description;
    private final BookThumbnail imageLinks;


    VolumeInfo(String title, String subtitle, List<String> authors, String description, BookThumbnail imageLinks) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.description = description;
        this.imageLinks = imageLinks;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public BookThumbnail getImageLinks() {
        return imageLinks;
    }
}
