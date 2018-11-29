package com.example.rostrzep.googlebooksapp

import com.example.rostrzep.googlebooksapp.api.GoogleBooksService
import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.example.rostrzep.googlebooksapp.api.model.BookThumbnail
import com.example.rostrzep.googlebooksapp.api.model.BooksList
import com.example.rostrzep.googlebooksapp.api.model.VolumeInfo
import com.example.rostrzep.googlebooksapp.view.books.BooksSearchPresenter
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class MoviesInteractorTest {

    @Rule @JvmField
    val rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var googleBooksService: GoogleBooksService

    internal lateinit var bookSearchPresenter: BooksSearchPresenter

    @Before
    fun setUp() {
        bookSearchPresenter = BooksSearchPresenter(googleBooksService)
    }

    @Test
    fun `when books are found they will be propagated to the subscriber`() {

        val bookItemList = ArrayList<BookItem>()
        val authors = ArrayList<String>()
        authors.add("First author")
        authors.add("Second author")
        val volumeInfo = VolumeInfo("Title", "Subtitle", authors, "Description", BookThumbnail("Test", "Test"))
        bookItemList.add(BookItem(volumeInfo))
        bookItemList.add(BookItem(volumeInfo))
        val booksList = BooksList(bookItemList)

        val bookItemObservable = Observable.just<BooksList>(booksList)
        val testObserver = TestObserver<BooksList>()
        val testObserverError = TestObserver<Throwable>()
        bookSearchPresenter.getBookList().subscribe(testObserver)
        bookSearchPresenter.getBookListError().subscribe(testObserverError)

        Mockito.`when`(googleBooksService.searchForBooks("searchQuery", 10)).thenReturn(bookItemObservable)
        bookSearchPresenter.searchQueryObserver().onNext("Search Query")
        bookSearchPresenter.triggerSearch().onNext(Any())

        testObserver.assertValueCount(1)
    }
}

class RxImmediateSchedulerRule : TestRule {

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}