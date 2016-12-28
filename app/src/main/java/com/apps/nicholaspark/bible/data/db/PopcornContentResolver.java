package com.apps.nicholaspark.bible.data.db;


import android.content.ContentResolver;
import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

/**
 * Created by nicholaspark on 12/28/16.
 */

public final class PopcornContentResolver {
  final Handler contentObserverHandler = new Handler(Looper.getMainLooper());
  final ContentResolver contentResolver;
  private final Scheduler scheduler;
  private final ObservableTransformer<PopcornPopper.Query, PopcornPopper.Query> queryTransformer;

  @Inject
  PopcornContentResolver(ContentResolver contentResolver,
                         Scheduler scheduler,
                         ObservableTransformer<PopcornPopper.Query, PopcornPopper.Query> transformer) {
    this.contentResolver = contentResolver;
    this.scheduler = scheduler;
    this.queryTransformer = transformer;
  }


}
