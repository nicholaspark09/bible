package com.apps.nicholaspark.bible.data.chapter.remote;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.chapter.ChapterDataSource;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.Chapter;
import com.apps.nicholaspark.bible.data.vo.ChapterResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by nicholaspark on 12/23/16.
 */

public class ChapterRemoteDataSource implements ChapterDataSource {

  private final BibleApi api;

  @Inject
  public ChapterRemoteDataSource(BibleApi bibleApi) {
    this.api = bibleApi;
  }

  @Override
  public Observable<List<Chapter>> getChapters(String bookId) {
    return api.loadChapters(bookId)
            .doOnNext(new Consumer<ChapterResponse>() {
              @Override
              public void accept(ChapterResponse chapterResponse) throws Exception {
              }
            })
            .flatMap(new Function<ChapterResponse, ObservableSource<List<Chapter>>>() {
              @Override
              public ObservableSource<List<Chapter>> apply(ChapterResponse chapterResponse) throws Exception {
                return Observable.just(chapterResponse.response().chapters());
              }
            });
  }

  @Override
  public Observable<Chapter> getChapter(String chapterId) {
    return null;
  }

  @Override
  public void saveChapter(@NonNull Chapter chapter) {

  }

  @Override
  public void deleteChapter(@NonNull String chapterId) {

  }

  @Override
  public void deleteChapters() {

  }

  @Override
  public void refresh() {

  }
}
