package com.example.rostrzep.googlebooksapp.view.books

import com.example.rostrzep.googlebooksapp.AppConst
import com.example.rostrzep.googlebooksapp.api.GoogleBooksService
import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.example.rostrzep.googlebooksapp.api.model.BooksList
import com.example.rostrzep.googlebooksapp.helpers.Result
import com.example.rostrzep.googlebooksapp.helpers.onlyError
import com.example.rostrzep.googlebooksapp.helpers.onlySuccess
import com.example.rostrzep.googlebooksapp.helpers.toResult
import com.jakewharton.rx.ReplayingShare
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BooksSearchPresenter(googleBooksService: GoogleBooksService) {

    val emptyResultsObservable: Observable<Boolean>
    private val bookList: Observable<Result<BooksList>>

    private val progressSubject = BehaviorSubject.create<Boolean>()
    private val searchQuery = BehaviorSubject.create<String>()
    private val triggerSearch = PublishSubject.create<Any>()
    private val bookItemClickSubject = PublishSubject.create<BookItem>()

    val progressObservable: Observable<Boolean>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    init {

        bookList = triggerSearch
                .withLatestFrom(searchQuery, BiFunction<Any, String, String> { _, searchQuery -> searchQuery })
                .filter { searchQuery -> searchQuery.isNotEmpty() }
                .distinctUntilChanged()
                .doOnNext { progressSubject.onNext(true) }
                .switchMap {
                    return@switchMap googleBooksService
                            .searchForBooks(it, AppConst.BOOKS_MAX_RESULTS)
                            .toResult()
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { progressSubject.onNext(false) }
                .compose(ReplayingShare.instance())

        emptyResultsObservable = Observable.combineLatest(bookList, searchQuery,
                BiFunction<Result<BooksList>, String, Boolean> { booksList, searchQuery ->
                    if (searchQuery.isEmpty()) {
                        false
                    } else booksList.data == null

                })
                .compose(ReplayingShare.instance())

    }

    fun getBookList(): Observable<BooksList> {
        return bookList.onlySuccess()
    }

    fun getBookListError(): Observable<Throwable> {
        return bookList.onlyError()
    }

    fun bookItemClickedSubject(): Observer<BookItem> {
        return bookItemClickSubject
    }

    fun bookItemClickObservable(): Observable<BookItem> {
        return bookItemClickSubject
    }

    fun triggerSearch(): PublishSubject<Any> {
        return triggerSearch
    }

    fun searchQueryObserver(): Observer<String> {
        return searchQuery
    }
}
