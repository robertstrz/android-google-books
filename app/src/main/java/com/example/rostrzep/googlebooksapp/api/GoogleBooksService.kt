package com.example.rostrzep.googlebooksapp.api

import com.example.rostrzep.googlebooksapp.api.model.BooksList

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {

    @GET("volumes")
    fun searchForBooks(@Query("q") searchQuery: String, @Query("maxResults") maxResults: Int?): Observable<BooksList>
}