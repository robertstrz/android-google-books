package com.example.rostrzep.googlebooksapp.view.books.book_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rostrzep.googlebooksapp.BaseActivity;
import com.example.rostrzep.googlebooksapp.R;
import com.example.rostrzep.googlebooksapp.api.model.BookItem;
import com.example.rostrzep.googlebooksapp.dagger.ActivityModule;
import com.example.rostrzep.googlebooksapp.dagger.ActivityScope;
import com.example.rostrzep.googlebooksapp.dagger.AppComponent;
import com.example.rostrzep.googlebooksapp.dagger.BaseActivityComponent;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class BookDetailsActivity extends BaseActivity {

    public static final String EXTRA_BOOK_ITEM = "extra_book_item";

    public static Intent newIntent(final Context context, final BookItem bookItem) {
        final Intent intent = new Intent(context, BookDetailsActivity.class);
        intent.putExtra(EXTRA_BOOK_ITEM, bookItem);
        return intent;
    }

    @BindView(R.id.book_cover)
    ImageView bookCover;
    @BindView(R.id.book_title)
    TextView bookTitle;
    @BindView(R.id.book_subtitle)
    TextView bookSubtitle;
    @BindView(R.id.book_description)
    TextView bookDescription;

    @Inject
    BookDetailsPresenter presenter;
    @Inject
    Picasso picasso;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        compositeSubscription = new CompositeSubscription(
                presenter.getBookItemObservable()
                        .subscribe(
                                new Action1<BookItem>() {
                                    @Override
                                    public void call(BookItem bookItem) {
                                        picasso.load(bookItem.getVolumeInfo().getImageLinks().getThumbnail())
                                                .fit()
                                                .centerCrop()
                                                .into(bookCover);

                                        bookTitle.setText(bookItem.getVolumeInfo().getTitle());
                                        bookSubtitle.setText(bookItem.getVolumeInfo().getSubtitle());

                                        bookDescription.setText(bookItem.getVolumeInfo().getDescription());
                                    }
                                }
                        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.clear();
            compositeSubscription = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void inject(Bundle savedInstanceState, AppComponent appComponent) {
        final Component component = DaggerBookDetailsActivity_Component.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .module(new Module(this))
                .build();
        component.inject(this);
    }

    @ActivityScope
    @dagger.Component(
            dependencies = AppComponent.class,
            modules = {
                    ActivityModule.class,
                    Module.class
            }
    )
    interface Component extends BaseActivityComponent {
        void inject(BookDetailsActivity activity);
    }

    @dagger.Module
    class Module {

        private final BookDetailsActivity bookDetailsActivity;

        public Module(BookDetailsActivity bookDetailsActivity) {
            this.bookDetailsActivity = bookDetailsActivity;
        }

        @Provides
        @Named("book_item")
        public BookItem provideBookItem() {
            return (BookItem) getIntent().getExtras().getSerializable(EXTRA_BOOK_ITEM);
        }

        @Provides
        public BookDetailsPresenter provideBookDetailsPresenter(final @Named("book_item") BookItem bookItem) {
            return new BookDetailsPresenter(bookItem);
        }

    }
}
