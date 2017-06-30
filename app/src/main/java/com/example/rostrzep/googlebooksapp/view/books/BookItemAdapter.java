package com.example.rostrzep.googlebooksapp.view.books;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rostrzep.googlebooksapp.R;
import com.example.rostrzep.googlebooksapp.api.model.BookItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.CountriesViewHolder> {

    public interface OnBookItemClickListener {
        public void bookItemClick(final BookItem bookItem);
    }

    class CountriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_cover)
        ImageView bookCover;
        @BindView(R.id.book_title)
        TextView bookTitle;
        @BindView(R.id.book_subtitle)
        TextView bookSubtitle;
        @BindView(R.id.container_book_item)
        View containerBookItem;

        public CountriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BookItem bookItem) {

            if (bookItem.getVolumeInfo().getImageLinks() != null) {
                picasso.load(bookItem.getVolumeInfo().getImageLinks().getThumbnail())
                        .placeholder(R.drawable.ic_camera_alt_blue_grey_500_48dp)
                        .fit()
                        .centerCrop()
                        .into(bookCover);
            } else {
                picasso.load(R.drawable.ic_camera_alt_blue_grey_500_48dp)
                        .fit()
                        .centerCrop()
                        .into(bookCover);
            }


            bookTitle.setText(bookItem.getVolumeInfo().getTitle());
            bookSubtitle.setText(bookItem.getVolumeInfo().getSubtitle());

            containerBookItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBookItemClickListener.bookItemClick(bookItem);
                }
            });
        }

    }

    @Override
    public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountriesViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(CountriesViewHolder holder, int position) {
        holder.bind(bookItems.get(position));
    }

    @Override
    public int getItemCount() {
        return bookItems == null ? 0 : bookItems.size();
    }

    public void bindData(final List<BookItem> bookItems) {
        this.bookItems = bookItems;
        notifyDataSetChanged();
    }

    private final LayoutInflater layoutInflater;
    private final Picasso picasso;
    private final OnBookItemClickListener onBookItemClickListener;

    private List<BookItem> bookItems = new ArrayList<>();

    @Inject
    public BookItemAdapter(final LayoutInflater layoutInflater, final Picasso picasso, OnBookItemClickListener onBookItemClickListener) {
        this.layoutInflater = layoutInflater;
        this.picasso = picasso;
        this.onBookItemClickListener = onBookItemClickListener;
    }
}
