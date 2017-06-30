package com.example.rostrzep.googlebooksapp.view.books;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rostrzep.googlebooksapp.BaseActivity;
import com.example.rostrzep.googlebooksapp.R;
import com.example.rostrzep.googlebooksapp.api.model.BookItem;
import com.example.rostrzep.googlebooksapp.api.model.BooksList;
import com.example.rostrzep.googlebooksapp.dagger.ActivityModule;
import com.example.rostrzep.googlebooksapp.dagger.ActivityScope;
import com.example.rostrzep.googlebooksapp.dagger.AppComponent;
import com.example.rostrzep.googlebooksapp.dagger.BaseActivityComponent;
import com.example.rostrzep.googlebooksapp.helpers.KeyboardHelper;
import com.example.rostrzep.googlebooksapp.view.books.book_details.BookDetailsActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class BooksSearchActivity extends BaseActivity implements BookItemAdapter.OnBookItemClickListener {

    @BindView(R.id.text_search_query)
    EditText searchInput;
    @BindView(R.id.recycler_view_book_list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_reuslts_view)
    View emptyResultsView;
    @BindView(R.id.progress_view)
    View progressView;

    @Inject
    BooksSearchPresenter presenter;
    @Inject
    BookItemAdapter bookItemAdapter;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookItemAdapter);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.triggerSearch().onNext(new Object());
                    KeyboardHelper.hideSoftKeyboard(BooksSearchActivity.this);
                    return true;
                }
                return false;
            }
        });

        compositeSubscription = new CompositeSubscription(
                RxTextView.textChanges(searchInput)
                        .map(new Func1<CharSequence, String>() {
                            @Override
                            public String call(CharSequence charSequence) {
                                return charSequence.toString();
                            }
                        })
                        .subscribe(presenter.querySubject()),
                presenter.getBookList()
                        .subscribe(
                                new Action1<BooksList>() {
                                    @Override
                                    public void call(BooksList booksList) {
                                        bookItemAdapter.bindData(booksList.getItems());
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Toast.makeText(BooksSearchActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
                                    }
                                }),
                presenter.getBookListApiError()
                        .subscribe(new Action1<BooksList>() {
                            @Override
                            public void call(BooksList booksList) {
                                Toast.makeText(BooksSearchActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
                            }
                        }),
                presenter.getEmptyResultsObservable()
                        .subscribe(RxView.visibility(emptyResultsView)),
                presenter.getProgressObservable()
                        .subscribe(RxView.visibility(progressView)),
                presenter.bookItemClickObservable()
                        .subscribe(new Action1<BookItem>() {
                            @Override
                            public void call(BookItem bookItem) {
                                startActivity(BookDetailsActivity.newIntent(BooksSearchActivity.this, bookItem));
                            }
                        })
        );
    }

    @Override
    public void bookItemClick(BookItem bookItem) {
        presenter.bookItemClickedSubject().onNext(bookItem);
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
    public void inject(Bundle savedInstanceState, AppComponent appComponent) {
        final Component component = DaggerBooksSearchActivity_Component.builder()
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
        void inject(BooksSearchActivity activity);

        BookItemAdapter.OnBookItemClickListener provideOnBookItemClickListener();
    }

    @dagger.Module
    class Module {

        private final BooksSearchActivity selectRoomsActivity;

        public Module(final BooksSearchActivity selectRoomsActivity) {
            this.selectRoomsActivity = selectRoomsActivity;
        }

        @Provides
        public BookItemAdapter.OnBookItemClickListener provideOnBookItemClickListener() {
            return selectRoomsActivity;
        }

        @Provides
        public BookItemAdapter provideBookItemAdapter(final LayoutInflater LayoutInflater,
                                                      final Picasso picasso,
                                                      final BookItemAdapter.OnBookItemClickListener onBookItemClickListener) {
            return new BookItemAdapter(LayoutInflater, picasso, onBookItemClickListener);
        }

    }
}
