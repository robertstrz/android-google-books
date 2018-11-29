package com.example.rostrzep.googlebooksapp.view.books.book_details

import com.example.rostrzep.googlebooksapp.api.model.BookItem
import io.reactivex.Observable

class BookDetailsPresenter(bookItem: BookItem) {
    val bookItemObservable: Observable<BookItem> = Observable.defer { Observable.just(bookItem) }
}
