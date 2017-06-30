package com.example.rostrzep.googlebooksapp.api.model;

import java.util.List;

public class BooksList {

    private final List<BookItem> items;

    public BooksList(List<BookItem> items) {
        this.items = items;
    }


    public List<BookItem> getItems() {
        return items;
    }
}
