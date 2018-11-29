package com.example.rostrzep.googlebooksapp.view.books

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.rostrzep.googlebooksapp.R
import com.example.rostrzep.googlebooksapp.api.model.BookItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_book.view.*

import java.util.ArrayList

class BookItemAdapter(private val layoutInflater: LayoutInflater, private val picasso: Picasso, private val onBookItemClickListener: OnBookItemClickListener) : RecyclerView.Adapter<BookItemAdapter.CountriesViewHolder>() {

    private var bookItems: List<BookItem>? = ArrayList()

    interface OnBookItemClickListener {
        fun bookItemClick(bookItem: BookItem)
    }

    inner class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bookItem: BookItem) {

            if (bookItem.volumeInfo.imageLinks != null) {
                picasso.load(bookItem.volumeInfo.imageLinks.thumbnail)
                        .placeholder(R.drawable.ic_camera_alt_blue_grey_500_48dp)
                        .fit()
                        .centerCrop()
                        .into(itemView.book_cover)
            } else {
                picasso.load(R.drawable.ic_camera_alt_blue_grey_500_48dp)
                        .fit()
                        .centerCrop()
                        .into(itemView.book_cover)
            }


            itemView.book_title!!.text = bookItem.volumeInfo.title
            itemView.book_subtitle!!.text = bookItem.volumeInfo.subtitle

            itemView.container_book_item!!.setOnClickListener { onBookItemClickListener.bookItemClick(bookItem) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false))
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(bookItems!![position])
    }

    override fun getItemCount(): Int {
        return if (bookItems == null) 0 else bookItems!!.size
    }

    fun bindData(bookItems: List<BookItem>) {
        this.bookItems = bookItems
        notifyDataSetChanged()
    }
}
