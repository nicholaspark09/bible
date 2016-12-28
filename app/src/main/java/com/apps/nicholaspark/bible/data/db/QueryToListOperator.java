package com.apps.nicholaspark.bible.data.db;

import android.database.Cursor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

/**
 * Created by nicholaspark on 12/28/16.
 */

public class QueryToListOperator<T> implements ObservableOperator<List<T>, PopcornPopper.Query> {

  final Function<Cursor, T> mapper;

  QueryToListOperator(Function<Cursor, T> mapper) {
    this.mapper = mapper;
  }

  @Override
  public Observer<? super PopcornPopper.Query> apply(Observer<? super List<T>> observer) throws Exception {

    return new Observer<PopcornPopper.Query>() {
      @Override
      public void onSubscribe(Disposable d) {
        observer.onSubscribe(d);

      }

      @Override
      public void onNext(PopcornPopper.Query query) {
        try {
          Cursor cursor = query.run();
          if (cursor == null)
            return;
          List<T> items = new ArrayList<T>(cursor.getCount());
          try {
            while(cursor.moveToNext()) {
              items.add(mapper.apply(cursor));
            }
          } finally {
            cursor.close();
          }
        }catch (Throwable e) {
          Exceptions.throwIfFatal(e);
          onError(e);
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
