package com.example.rostrzep.googlebooksapp.api.model;

import java.io.Serializable;

public class BookThumbnail implements Serializable {

    private final String smallThumbnail;
    private final String thumbnail;

    BookThumbnail(String smallThumbnail, String thumbnail) {
        this.smallThumbnail = smallThumbnail;
        this.thumbnail = thumbnail;
    }


    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
