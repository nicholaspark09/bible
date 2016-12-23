package com.apps.nicholaspark.bible.data.book;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apps.nicholaspark.bible.data.qualifier.Local;
import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

  @NonNull
  private final BookDataSource localLayer;

  @NonNull
  private final BookDataSource networkLayer;



  @Inject
  public BookRepository(@Local BookDataSource localSource, @Remote BookDataSource remoteSource) {
    this.localLayer = localSource;
    this.networkLayer = remoteSource;
  }

  @Override
  public Observable<List<Book>> getBooks(@NonNull String version) {
    if (cachedBooks != null && !cacheIsDirty)
    {
      // Responds immediately with cache if available
      List<Book> list = new ArrayList<>(cachedBooks.values());
      return Observable.just(list);
    }else if (cachedBooks == null) {
      cachedBooks = new LinkedHashMap<>();
    }

    Observable<List<Book>> remoteBooks = getAndSaveRemoteBooks(version);
    if (cacheIsDirty) {
      return remoteBooks;
    }else {
      Observable<List<Book>> localBooks = getAndCacheLocalBooks(version);
      return Observable.concat(localBooks, remoteBooks)
              .filter(books -> !books.isEmpty());
    }
  }

  @Override
  public Observable<Book> getBook(@NonNull String id) {
    return null;
  }

  @Override
  public void saveBook(@NonNull Book book) {
    localLayer.saveBook(book);
    if (cachedBooks == null)
      cachedBooks = new LinkedHashMap<>();
    cachedBooks.put(book.id(), book);
  }

  @Override
  public void deleteBook(@NonNull String id) {
    localLayer.deleteBook(id);
    cachedBooks.remove(id);
  }

  @Override
  public void deleteAll() {
    localLayer.deleteAll();
    if (cachedBooks == null)
      cachedBooks = new LinkedHashMap<>();
    cachedBooks.clear();
  }

  @Override
  public void refresh() {

  }

  private Observable<List<Book>> getAndSaveRemoteBooks(String versionId) {
    return networkLayer
            .getBooks(versionId)
            .flatMap(new Function<List<Book>, ObservableSource<List<Book>>>() {
              @Override
              public ObservableSource<List<Book>> apply(List<Book> books) throws Exception {
                for(Book book : books) {
                  localLayer.saveBook(book);
                  cachedBooks.put(book.id(), book);
                }
                return Observable.fromArray(books)
                        .doOnComplete(()-> cacheIsDirty = false);
              }
            });

  }

  private Observable<List<Book>> getAndCacheLocalBooks(String version) {
    return localLayer.getBooks(version)
            .flatMap(new Function<List<Book>, ObservableSource<List<Book>>>() {
              @Override
              public ObservableSource<List<Book>> apply(List<Book> books) throws Exception {
                for(Book book : books) {
                  cachedBooks.put(book.id(), book);
                }
                return Observable.fromArray(books);
              }
            });

  }
}
