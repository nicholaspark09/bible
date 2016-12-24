package com.apps.nicholaspark.bible.data.book;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apps.nicholaspark.bible.data.qualifier.Local;
import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import preferences2.Preference;
import timber.log.Timber;

/**
 * Created by nicholaspark on 12/22/16.
 *
 *  Concrete implementation to loat books from data sources
 *
 *    Synchronises network layer and local SQLDatabase into one stream
 *    of books
 */
@ApplicationScope
public final class BookRepository implements BookDataSource {

  @VisibleForTesting
  @Nullable
  Map<String, Book> cachedBooks;

  boolean cacheIsDirty = false;

  public Book selectedBook;

  @NonNull
  private final BookDataSource networkLayer;

  private final Preference<Version> version;

  private Subject<Boolean> loadingState;



  @Inject
  public BookRepository(@Remote BookDataSource remoteSource,
                        Preference<Version> versionPreference) {
    this.networkLayer = remoteSource;
    this.version = versionPreference;
    loadingState = BehaviorSubject.createDefault(false).toSerialized();
  }

  @Override
  public Observable<List<Book>> getBooks(String version) {
    loadingState.onNext(true);
    if (cachedBooks != null && !cacheIsDirty)
    {
      // Responds immediately with cache if available
      List<Book> list = new ArrayList<>(cachedBooks.values());
      return Observable.just(list)
              .doOnComplete(()-> loadingState.onNext(false));
    }

    String versionId = this.version.get().id();
    Observable<List<Book>> remoteBooks = getAndSaveRemoteBooks(versionId);
      return remoteBooks
              .doOnComplete(()-> {
                loadingState.onNext(false);
                cacheIsDirty = false;
              });

  }

  @Override
  public Observable<Book> getBook(@NonNull String id) {
    return null;
  }

  @Override
  public void saveBook(@NonNull Book book) {
    if (cachedBooks == null)
      cachedBooks = new LinkedHashMap<>();
    cachedBooks.put(book.id(), book);
  }

  @Override
  public void deleteBook(@NonNull String id) {
    cachedBooks.remove(id);
  }

  @Override
  public void deleteAll() {
    if (cachedBooks == null)
      cachedBooks = new LinkedHashMap<>();
    cachedBooks.clear();
  }

  @Override
  public void refresh() {
    cacheIsDirty = true;
  }

  @Override
  public void setSelectedBook(String bookId) {
    selectedBook = cachedBooks.get(bookId);
  }

  @Override
  public Book getSelectedBook() {
    if (selectedBook != null)
      return selectedBook;
    return null;
  }

  public Observable<Boolean> getLoadingState() {
    return loadingState;
  }

  private Observable<List<Book>> getAndSaveRemoteBooks(String versionId) {
    return networkLayer
            .getBooks(versionId)
            .flatMap(new Function<List<Book>, ObservableSource<List<Book>>>() {
              @Override
              public ObservableSource<List<Book>> apply(List<Book> books) throws Exception {
                for(Book book : books) {
                  saveBook(book);
                }
                return Observable.fromArray(books);
              }
            });

  }

}
