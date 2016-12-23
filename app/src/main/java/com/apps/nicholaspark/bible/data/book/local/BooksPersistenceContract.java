package com.apps.nicholaspark.bible.data.book.local;

import android.provider.BaseColumns;

/**
 * Created by nicholaspark on 12/22/16.
 */

public final class BooksPersistenceContract {

  // Don't want an instantiable class
  private BooksPersistenceContract(){}

  // Defines the table contents of a Books sql table
  public static abstract class BookEntry implements BaseColumns {
    public static final String TABLE_NAME = "books";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ORDER = "order";
    public static final String COLUMN_NAME_TESTAMENT = "testament";
    public static final String COLUMN_NAME_ABBR = "abbr";
    public static final String COLUMN_NAME_OSIS = "osis_end";
    public static final String COLUMN_NAME_GROUP = "book_group_id";
    public static final String COLUMN_NAME_VERSION = "version_id";
  }
}
