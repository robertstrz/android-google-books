package com.example.rostrzep.googlebooksapp;

/**
 * Created by ROSTRZEP on 2017-06-28.
 */

public class AppConst {
    
    public static final Integer BOOKS_MAX_RESULTS = 40;
    private static String GOOGLE_BOOKS_API_BASE_URL = "https://www.googleapis.com/books/v1/";

    public static String getBaseUrl() {
        return GOOGLE_BOOKS_API_BASE_URL;
    }
}
