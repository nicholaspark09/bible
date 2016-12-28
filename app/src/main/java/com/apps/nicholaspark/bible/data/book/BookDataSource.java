package com.apps.nicholaspark.bible.data.book;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.vo.Book;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/18/16.
 */

public interface BookDataSource {
  Observable<List<Book>> getBooks(String version);
  Observable<Book> getBook(@NonNull String id);
  void saveBooks(List<Book> books);
  void deleteBook(@NonNull String id);
  void deleteAll();
  void refresh();
  void setSelectedBook(String bookId);
  Book getSelectedBook();
}
