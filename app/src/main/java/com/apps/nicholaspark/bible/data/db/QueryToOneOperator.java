package com.apps.nicholaspark.bible.data.db;

import android.database.Cursor;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

/**
 * Created by nicholaspark on 12/24/16.
 */

final class QueryToOneOperator<T> implements ObservableOperator<T, PopcornPopper.Query> {

  final Function<Cursor, T> mapper;
  boolean emitDefault;
  T defaultValue;

  QueryToOneOperator(Function<Cursor, T> mapper, boolean emitDefault, T defaultValue ) {
    this.mapper = mapper;
    this.emitDefault = emitDefault;
    this.defaultValue = defaultValue;
  }

  @Override
  public Observer<? super PopcornPopper.Query> apply(Observer<? super T> observer) throws Exception {

    return new Observer<PopcornPopper.Query>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(PopcornPopper.Query value) {
        try {
          boolean emit = false;
          T item = null;
          Cursor cursor = value.run();
          if (cursor != null) {
            try {
              if (cursor.moveToNext()) {
                item = mapper.apply(cursor);
                emit = true;
                if (cursor.moveToNext()) {
                  throw new IllegalStateException("Cursor returned more than 1 row");
                }
              }
            } finally {
              cursor.close();
            }
          }
          if (emit) {
            observer.onNext(item);
          }else if (emitDefault) {
            observer.onNext(defaultValue);
          }
        }catch(Throwable e) {
          Exceptions.throwIfFatal(e);
        }
      }

      @Override
      public void onError(Throwable e) {
        observer.onError(e);
      }

      @Override
      public void onComplete() {
        observer.onComplete();
      }
    };
  }
}
