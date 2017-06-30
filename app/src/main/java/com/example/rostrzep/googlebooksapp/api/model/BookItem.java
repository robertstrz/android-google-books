package com.example.rostrzep.googlebooksapp.api.model;

import java.io.Serializable;

public class BookItem implements Serializable {

    private final VolumeInfo volumeInfo;

    public BookItem(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
}
