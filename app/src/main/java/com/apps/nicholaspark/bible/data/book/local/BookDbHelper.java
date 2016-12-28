package com.apps.nicholaspark.bible.data.book.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nicholaspark on 12/24/16.
 */

public class BookDbHelper extends SQLiteOpenHelper {

  public static final int DB_VERSION = 1;
  public static final String DB_NAME = "thebooks.db";
  public static final String TEXT_TYPE = " TEXT";
  public static final String BOOLEAN_TYPE = " INTEGER";
  public static final String COMMA = ",";

  private static final String SQL_CREATE_ENTRY =
          "CREATE TABLE " + BookPersistenceContract.BookEntry.TABLE_NAME + "(" +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_BOOK_ID + BOOLEAN_TYPE + COMMA +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_ORDER + BOOLEAN_TYPE + COMMA +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_ABBR + TEXT_TYPE + COMMA +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_BOOK_GROUP_ID + TEXT_TYPE + COMMA +
                  BookPersistenceContract.BookEntry.COLUMN_NAME_VERSION_ID + TEXT_TYPE + ")";


  public BookDbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRY);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + BookPersistenceContract.BookEntry.TABLE_NAME);
    this.onCreate(db);
  }
}
