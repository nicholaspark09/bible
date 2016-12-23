package com.apps.nicholaspark.bible.data.book.remote;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.book.BookDataSource;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.BookResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by nicholaspark on 12/21/16.
 */

public class BookRemoteDataSource implements BookDataSource {

  private final BibleApi api;

  @Inject
  public BookRemoteDataSource(BibleApi bibleApi) {
    api = bibleApi;
  }

  @Override
  public Observable<List<Book>> getBooks(@NonNull String version) {
    return api.loadBooks(version)
            .flatMap(new Function<BookResponse, ObservableSource<List<Book>>>() {
              @Override
              public ObservableSource<List<Book>> apply(BookResponse bookResponse) throws Exception {
                return Observable.just(bookResponse.response().books());
              }
            });
  }

  @Override
  public Observable<Book> getBook(@NonNull String id) {
    return null;
  }

  @Override
  public void saveBook(@NonNull Book book) {
    // Handled in concrete implementation
  }


  @Override
  public void deleteBook(@NonNull String id) {
    // Handled in concrete implementation
  }

  @Override
  public void deleteAll() {
    // Handled in concrete implementation
  }

  @Override
  public void refresh() {
    // Handled in concrete implementation
  }
}
