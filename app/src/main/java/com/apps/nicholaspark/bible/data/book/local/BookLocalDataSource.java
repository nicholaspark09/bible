package com.apps.nicholaspark.bible.data.book.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.apps.nicholaspark.bible.data.book.BookDataSource;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

import rx.functions.Func1;

/**
 * Created by nicholaspark on 12/22/16.
 */

public class BookLocalDataSource implements BookDataSource {

  @NonNull
  private final BriteDatabase databaseHelper;

  @NonNull
  private Func1<Cursor, Book> bookMapperFunction;

  @Inject
  public BookLocalDataSource(BookDbHelper bookDbHelper,
                             SqlBrite sqlBrite,
                             @Named("io") rx.Scheduler ioThread) {
    databaseHelper = sqlBrite.wrapDatabaseHelper(bookDbHelper, ioThread);
    bookMapperFunction = this::getBook;
  }

  @NonNull
  private Book getBook(@NonNull Cursor c) {
    String id = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_ID));
    String name = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_NAME));
    int order = c.getInt(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_ORDER));
    String test = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_TESTAMENT));
    String abbr = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_ABBR));
    String osis = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_OSIS));
    String group = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_GROUP));
    String version = c.getString(c.getColumnIndexOrThrow(BooksPersistenceContract.BookEntry.COLUMN_NAME_VERSION));
    return Book.create(id, name, order, test, abbr, osis, group, version, "");
  }


  @Override
  public Observable<List<Book>> getBooks(@NonNull String version) {
    String[] projection = {
            BooksPersistenceContract.BookEntry.COLUMN_NAME_ID,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_NAME,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_ABBR,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_VERSION
    };

    String SQL = String.format("SELECT %s FROM %s ORDER BY " + BooksPersistenceContract.BookEntry.COLUMN_NAME_ID,
            TextUtils.join(",", projection), BooksPersistenceContract.BookEntry.TABLE_NAME);

    // Must use RXJavaInterop until SQLBrite implements RXJAVA2
    return RxJavaInterop.toV2Observable(databaseHelper.createQuery(BooksPersistenceContract.BookEntry.TABLE_NAME, SQL)
            .mapToList(bookMapperFunction));
  }

  @Override
  public Observable<Book> getBook(@NonNull String id) {
    String[] projection = {
            BooksPersistenceContract.BookEntry.COLUMN_NAME_ID,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_NAME,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_ABBR,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_VERSION
    };
    String SQL = String.format("SELECT %s FROM %s WHERE %s LIKE ? LIMIT 1" + BooksPersistenceContract.BookEntry.COLUMN_NAME_ID,
            TextUtils.join(",", projection), BooksPersistenceContract.BookEntry.TABLE_NAME,
            BooksPersistenceContract.BookEntry.COLUMN_NAME_ID);
    return RxJavaInterop.toV2Observable(databaseHelper.createQuery(BooksPersistenceContract.BookEntry.TABLE_NAME,
            SQL, id)
            .mapToOneOrDefault(bookMapperFunction, null));
  }

  @Override
  public void saveBook(@NonNull Book book) {
    ContentValues values = new ContentValues();
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_ID, book.id());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_NAME, book.name());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_ORDER, book.ord());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_TESTAMENT, book.testament());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_ABBR, book.abbr());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_OSIS, book.osisEnd());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_GROUP, book.bookGroupId());
    values.put(BooksPersistenceContract.BookEntry.COLUMN_NAME_VERSION, book.versionId());
    databaseHelper.insert(BooksPersistenceContract.BookEntry.TABLE_NAME,
            values, SQLiteDatabase.CONFLICT_REPLACE);
  }


  @Override
  public void deleteBook(@NonNull String id) {
    String selection = BooksPersistenceContract.BookEntry.COLUMN_NAME_ID + " LIKE ?";
    String[] args = {id};
    databaseHelper.delete(BooksPersistenceContract.BookEntry.TABLE_NAME,
            selection, args);
  }

  @Override
  public void deleteAll() {
    databaseHelper.delete(BooksPersistenceContract.BookEntry.TABLE_NAME, null);
  }

  @Override
  public void refresh() {
    // This takes place in the Concrete implementation
  }
}
