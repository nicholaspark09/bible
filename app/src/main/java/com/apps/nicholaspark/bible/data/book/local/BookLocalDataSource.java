package com.apps.nicholaspark.bible.data.book.local;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.book.BookDataSource;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Named;

import io.reactivex.Observable;
import preferences2.Preference;

/**
 * Created by nicholaspark on 12/28/16.
 */

public class BookLocalDataSource implements BookDataSource {

  Preference<String> books;
  private final Gson gson;

  public BookLocalDataSource(@Named("BibleBooks") Preference<String> books, Gson gson) {
    this.books = books;
    this.gson = gson;
  }

  @Override
  public Observable<List<Book>> getBooks(String version) {
    return Observable.fromCallable(new Callable<List<Book>>() {

      @Override
      public List<Book> call() throws Exception {
        String serialized = books.get();
        List<Book> books = new ArrayList<>();
        if (serialized.length() > 0) {
          Type type = new TypeToken<List<Book>>(){}.getType();
          books = gson.fromJson(serialized, type);
        }
        return books;
      }
    });
  }

  @Override
  public Observable<Book> getBook(@NonNull String id) {
    return null;
  }

  @Override
  public void saveBooks(List<Book> books) {
    String serialized = gson.toJson(books);
    this.books.set(serialized);
  }

  @Override
  public void deleteBook(@NonNull String id) {
    // Not handled here, handled in concrete implementation
  }

  @Override
  public void deleteAll() {
    this.books.delete();
  }

  @Override
  public void refresh() {
    // Not handled here, handled in concrete implementation
  }

  @Override
  public void setSelectedBook(String bookId) {
    // Not handled here, handled in concrete implementation
  }

  @Override
  public Book getSelectedBook() {
    return null;
  }
}
