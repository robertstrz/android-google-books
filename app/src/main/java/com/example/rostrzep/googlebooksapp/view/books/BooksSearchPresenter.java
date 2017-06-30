package com.example.rostrzep.googlebooksapp.view.books;

import android.support.annotation.NonNull;

import com.example.rostrzep.googlebooksapp.AppConst;
import com.example.rostrzep.googlebooksapp.api.GoogleBooksService;
import com.example.rostrzep.googlebooksapp.api.model.BookItem;
import com.example.rostrzep.googlebooksapp.api.model.BooksList;
import com.example.rostrzep.googlebooksapp.dagger.NetworkScheduler;
import com.example.rostrzep.googlebooksapp.dagger.UiScheduler;
import com.google.common.base.Strings;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class BooksSearchPresenter {

    private final Observable<Boolean> emptyResultsObservable;
    private final Observable<BooksList> bookList;

    private final BehaviorSubject<Boolean> progressSubject = BehaviorSubject.create(false);
    private final BehaviorSubject<String> searchQuery = BehaviorSubject.create();
    private final PublishSubject<Object> triggerSearch = PublishSubject.create();
    private final PublishSubject<BookItem> bookItemClickSubject = PublishSubject.create();
    private final PublishSubject<BooksList> bookListApiError = PublishSubject.create();
    private final Scheduler uiScheduler;
    @Inject
    public BooksSearchPresenter(
            @NonNull final GoogleBooksService googleBooksService,
            @NonNull @UiScheduler final Scheduler uiScheduler,
            @NonNull @NetworkScheduler final Scheduler networkScheduler) {

        this.uiScheduler = uiScheduler;

        bookList =
                triggerSearch.withLatestFrom(
                        searchQuery, new Func2<Object, String, String>() {
                            @Override
                            public String call(Object aObject, String searchQuery) {
                                return searchQuery;
                            }
                        })
                        .throttleLast(700, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .filter(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String searchQuery) {
                                return !Strings.isNullOrEmpty(searchQuery);
                            }
                        })
                        .doOnNext(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                progressSubject.onNext(true);
                            }
                        })
                        .switchMap(new Func1<CharSequence, Observable<BooksList>>() {
                            @Override
                            public Observable<BooksList> call(CharSequence searchQuery) {
                                return googleBooksService.searchForBooks(searchQuery.toString(), AppConst.BOOKS_MAX_RESULTS)
                                        .subscribeOn(networkScheduler);
                            }
                        })
                        .onErrorResumeNext(new Func1<Throwable, Observable<? extends BooksList>>() {
                            @Override
                            public Observable<? extends BooksList> call(Throwable throwable) {
                                return bookListApiError;
                            }
                        })
                        .observeOn(uiScheduler)
                        .doOnNext(new Action1<BooksList>() {
                            @Override
                            public void call(BooksList bookList) {
                                progressSubject.onNext(false);
                            }
                        });

        emptyResultsObservable =
                Observable.combineLatest(bookList, searchQuery, new Func2<BooksList, String, Boolean>() {
                    @Override
                    public Boolean call(BooksList booksList, String searchQuery) {
                        if (Strings.isNullOrEmpty(searchQuery)) {
                            return false;
                        }
                        return booksList == null || booksList.getItems() == null;
                    }
                });
    }

    public Observable<Boolean> getEmptyResultsObservable() {
        return emptyResultsObservable;
    }

    public Observable<BooksList> getBookList() {
        return bookList.flatMap(new Func1<BooksList, Observable<BooksList>>() {
            @Override
            public Observable<BooksList> call(BooksList booksList) {
                return booksList == null ?
                        Observable.just(new BooksList(Collections.<BookItem>emptyList())) : Observable.just(booksList);
            }
        });
    }

    public Observer<String> querySubject() {
        return searchQuery;
    }

    public Observer<BookItem> bookItemClickedSubject() {
        return bookItemClickSubject;
    }

    public Observable<BookItem> bookItemClickObservable() {
        return bookItemClickSubject;
    }

    public Observable<Boolean> getProgressObservable() {
        return progressSubject.observeOn(uiScheduler);
    }

    public Observer<Boolean> progressObserver() {
        return progressSubject;
    }

    public PublishSubject<Object> triggerSearch() {
        return triggerSearch;
    }

    public PublishSubject<BooksList> getBookListApiError() {
        return bookListApiError;
    }
}
