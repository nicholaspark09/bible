package com.apps.nicholaspark.bible.data.book.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nicholaspark on 12/22/16.
 */

public class BookDbHelper extends SQLiteOpenHelper {

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "bibleapp.db";
  private static final String TEXT_TYPE = " TEXT";
  private static final String BOOLEAN_TYPE = " INTEGER";
  private static final String COMMA_SEP = ",";
  private static final String SQL_CREATE =
          "CREATE TABLE " + BooksPersistenceContract.BookEntry.TABLE_NAME + "(" +
                  BooksPersistenceContract.BookEntry._ID + TEXT_TYPE + " PRIMARY KEY, " +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_ORDER + BOOLEAN_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_TESTAMENT + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_ABBR + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_OSIS + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_GROUP + TEXT_TYPE + COMMA_SEP +
                  BooksPersistenceContract.BookEntry.COLUMN_NAME_VERSION + TEXT_TYPE +
                  ")";

  public BookDbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + BooksPersistenceContract.BookEntry.TABLE_NAME);
    this.onCreate(db);
  }

}
