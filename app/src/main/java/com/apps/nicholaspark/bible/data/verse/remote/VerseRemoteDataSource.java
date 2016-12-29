package com.apps.nicholaspark.bible.data.verse.remote;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.verse.VerseDataSource;
import com.apps.nicholaspark.bible.data.vo.Verse;
import com.apps.nicholaspark.bible.data.vo.VerseResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by nicholaspark on 12/29/16.
 *
 *  Implementation of data source that adds a network layer
 *
 */

public class VerseRemoteDataSource implements VerseDataSource {

  private final BibleApi bibleApi;

  @Inject
  public VerseRemoteDataSource(BibleApi bibleApi) {
    this.bibleApi = bibleApi;
  }

  @Override
  public Observable<List<Verse>> getVerses(@NonNull String chapterId) {
    return bibleApi.loadVerses(chapterId)
            .flatMap(new Function<VerseResponse, ObservableSource<List<Verse>>>() {
              @Override
              public ObservableSource<List<Verse>> apply(VerseResponse verseResponse) throws Exception {
                List<Verse> verses = verseResponse.response().verses();
                return Observable.just(verses);
              }
            });
  }

  @Override
  public Observable<Verse> getVerse(@NonNull String id) {
    // Handled in concrete implementation
    return null;
  }

  @Override
  public void saveVerse(@NonNull Verse verse) {
    // Handled in concrete implementation
  }

  @Override
  public void deleteAll() {
    // Handled in concrete implementation
  }

  @Override
  public void refresh() {
    // Handled in concrete implementation
  }
}
