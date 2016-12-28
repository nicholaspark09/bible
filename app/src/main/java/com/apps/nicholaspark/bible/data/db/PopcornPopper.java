package com.apps.nicholaspark.bible.data.db;

import android.database.Cursor;
import android.support.annotation.NonNull;

import io.reactivex.ObservableOperator;
import io.reactivex.functions.Function;

/**
 * Created by nicholaspark on 12/24/16.
 *
 *  A lightweight wrapper used for continuously
 *  observing the result of a query
 *
 */

public final class PopcornPopper {


  public static abstract class Query {

    public static <T> ObservableOperator<T, Query> mapToOne(@NonNull Function<Cursor, T> mapper) {
      return null;
    }

    public abstract Cursor run();
  }


}
