package com.apps.nicholaspark.bible.data.verse;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.vo.Chapter;
import com.apps.nicholaspark.bible.data.vo.Verse;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/29/16.
 */
@ApplicationScope
public final class VerseRepository implements VerseDataSource {

  boolean cacheIsDirty = false;
  String chapterId = "";
  Map<String, Verse> cache;
  private final VerseDataSource networkLayer;

  @Inject
  public VerseRepository(@Remote VerseDataSource remoteLayer) {
    this.networkLayer = remoteLayer;
  }

  @Override
  public Observable<List<Verse>> getVerses(@NonNull String chapterId) {
    if (!chapterId.equals(this.chapterId)) {
      cacheIsDirty = true;
    }
    if (cacheIsDirty || cache == null) {
      cache = new LinkedHashMap<>();
    }

    if (cacheIsDirty) {
      return this.networkLayer
              .getVerses(chapterId)
              .doOnComplete(()->{
                cacheIsDirty = false;
              });
    }else {
      List<Verse> verses = (ArrayList<Verse>) cache.values();
      return Observable.just(verses)
              .doOnComplete(()->cacheIsDirty = false);
    }
  }

  @Override
  public Observable<Verse> getVerse(@NonNull String id) {
    return null;
  }

  @Override
  public void saveVerse(@NonNull Verse verse) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public void refresh() {

  }
}
