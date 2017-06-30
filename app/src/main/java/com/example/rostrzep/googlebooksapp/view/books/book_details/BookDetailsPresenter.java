package com.example.rostrzep.googlebooksapp.view.books.book_details;

import com.example.rostrzep.googlebooksapp.api.model.BookItem;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func0;

public class BookDetailsPresenter {

    private final Observable<BookItem> bookItemObservable;

    @Inject
    public BookDetailsPresenter(final BookItem bookItem) {

        bookItemObservable = Observable.defer(
                new Func0<Observable<BookItem>>() {
                    @Override
                    public Observable<BookItem> call() {
                        return Observable.just(bookItem);
                    }
                });

    }

    public Observable<BookItem> getBookItemObservable() {
        return bookItemObservable;
    }
}
