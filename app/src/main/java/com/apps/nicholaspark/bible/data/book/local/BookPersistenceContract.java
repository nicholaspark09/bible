package com.apps.nicholaspark.bible.data.book.local;

import android.provider.BaseColumns;

/**
 * Created by nicholaspark on 12/24/16.
 *
 *  Contract used to save books locally to db
 *
 */
public final class BookPersistenceContract {

  // Prevent instatiation
  private BookPersistenceContract() {}

  // Defines the table contents
  public static abstract class BookEntry implements BaseColumns {
    public static final String TABLE_NAME = "books";
    public static final String COLUMN_NAME_BOOK_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    // order is not a valid column name in sql
    public static final String COLUMN_NAME_ORDER = "theorder";
    public static final String COLUMN_NAME_ABBR = "abbr";
    public static final String COLUMN_NAME_BOOK_GROUP_ID = "book_group_id";
    public static final String COLUMN_NAME_VERSION_ID = "version_id";
  }

}
