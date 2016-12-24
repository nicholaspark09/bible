package com.apps.nicholaspark.bible.data.chapter;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.book.BookRepository;
import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.Chapter;
import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import preferences2.Preference;
import timber.log.Timber;

/**
 * Created by nicholaspark on 12/23/16.
 */
@ApplicationScope
public final class ChapterRepository implements ChapterDataSource {

  Map<String, Chapter> chapters;
  String currentBook = "";
  boolean cacheIsDirty = false;
  private final ChapterDataSource remoteSource;
  private final Preference<Version> versionPreference;
  private final BookRepository bookRepository;

  @Inject
  public ChapterRepository(@Remote ChapterDataSource remoteSource,
                           BookRepository bookRepository,
                           Preference<Version> versionPreference) {
    this.remoteSource = remoteSource;
    this.versionPreference = versionPreference;
    this.bookRepository = bookRepository;
  }

  @Override
  public Observable<List<Chapter>> getChapters(String bookId) {
    Book book = bookRepository.getSelectedBook();
    if(book != null) {
      if (book.id().equals(currentBook) && chapters != null && !cacheIsDirty) {
        List<Chapter> currentChapters = new ArrayList<>(chapters.values());
        return Observable.just(currentChapters)
                .doOnComplete(()-> cacheIsDirty = false);
      }else if (chapters == null) {
        chapters = new LinkedHashMap<>();
      }
      return remoteSource.getChapters(book.id())
              .doOnNext(chapters -> {
                for(Chapter chapter : chapters) {
                  this.chapters.put(chapter.id(), chapter);
                }
              })
              .doOnComplete(()->{
                cacheIsDirty = false;
                currentBook = book.id();
              });
    }
    return Observable.never();
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
