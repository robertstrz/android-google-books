package com.example.rostrzep.googlebooksapp.view.books

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.rostrzep.googlebooksapp.BaseActivity
import com.example.rostrzep.googlebooksapp.R
import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.example.rostrzep.googlebooksapp.helpers.KeyboardHelper
import com.example.rostrzep.googlebooksapp.view.books.book_details.BookDetailsActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_book_list.*
import org.koin.android.ext.android.inject

class BooksSearchActivity : BaseActivity(), BookItemAdapter.OnBookItemClickListener {

    private val presenter: BooksSearchPresenter by inject()
    private var bookItemAdapter: BookItemAdapter? = null

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        bookItemAdapter = BookItemAdapter(layoutInflater, Picasso.with(this), this)
        recycler_view_book_list.layoutManager = LinearLayoutManager(this)
        recycler_view_book_list.adapter = bookItemAdapter;

        text_search_query.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.triggerSearch().onNext(Any())
                KeyboardHelper.hideSoftKeyboard(this@BooksSearchActivity)
            }
            false
        }

        compositeDisposable.add(
                RxTextView.textChanges(text_search_query)
                        .subscribe { presenter.searchQueryObserver().onNext(it.toString()) })
        compositeDisposable.add(
                RxTextView.textChanges(text_search_query)
                        .subscribe { presenter.triggerSearch() })
        compositeDisposable.add(
                presenter.getBookList()
                        .subscribe {
                            bookItemAdapter!!.bindData(it.items)
                        })
        compositeDisposable.add(
                presenter.getBookListError()
                        .subscribe {
                            Snackbar.make(search_view_container, R.string.error_message, Snackbar.LENGTH_SHORT).show()
                        })
        compositeDisposable.add(
                presenter.emptyResultsObservable
                        .subscribe(RxView.visibility(empty_reuslts_view)))
        compositeDisposable.add(
                presenter.progressObservable
                        .subscribe(RxView.visibility(progress_view)))
        compositeDisposable.add(
                presenter.bookItemClickObservable()
                        .subscribe { it -> startActivity(BookDetailsActivity.newIntent(this, it)) })

    }

    override fun bookItemClick(bookItem: BookItem) {
        presenter.bookItemClickedSubject().onNext(bookItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
