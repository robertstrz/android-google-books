package com.example.rostrzep.googlebooksapp.view.books.book_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.rostrzep.googlebooksapp.BaseActivity
import com.example.rostrzep.googlebooksapp.R
import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_book_details.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BookDetailsActivity : BaseActivity() {

    private val presenter: BookDetailsPresenter by inject { parametersOf(intent.extras.getSerializable(EXTRA_BOOK_ITEM)) }
    private val picasso: Picasso by inject()

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        compositeDisposable.add(
                presenter.bookItemObservable
                        .subscribe {
                            picasso.load(it.volumeInfo.imageLinks.thumbnail)
                                    .fit()
                                    .centerCrop()
                                    .into(book_cover)

                            book_title.text = it.volumeInfo.title
                            book_subtitle.text = it.volumeInfo.subtitle
                            book_description.text = it.volumeInfo.description
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        val EXTRA_BOOK_ITEM = "extra_book_item"

        fun newIntent(context: Context, bookItem: BookItem): Intent {
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra(EXTRA_BOOK_ITEM, bookItem)
            return intent
        }
    }

}
