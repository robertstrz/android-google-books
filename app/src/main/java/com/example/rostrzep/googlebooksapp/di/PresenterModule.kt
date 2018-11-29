package com.example.rostrzep.googlebooksapp.di

import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.example.rostrzep.googlebooksapp.view.books.BooksSearchPresenter
import com.example.rostrzep.googlebooksapp.view.books.book_details.BookDetailsPresenter
import org.koin.dsl.module.module

val presenterModule = module {
    single { BooksSearchPresenter(get()) }
    single { (bookItem: BookItem) -> BookDetailsPresenter(bookItem) }
}
