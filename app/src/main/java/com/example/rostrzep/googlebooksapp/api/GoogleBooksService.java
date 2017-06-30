package com.example.rostrzep.googlebooksapp.api;

import com.example.rostrzep.googlebooksapp.api.model.BooksList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GoogleBooksService {

    @GET("volumes")
    Observable<BooksList> searchForBooks(@Query("q") final String searchQuery, @Query("maxResults") final Integer maxResults);
}