package com.apps.nicholaspark.bible.data.chapter;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.Chapter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/23/16.
 */

public interface ChapterDataSource {
  Observable<List<Chapter>> getChapters(String bookId);
  Observable<Chapter> getChapter(String chapterId);
  void saveChapter(@NonNull Chapter chapter);
  void deleteChapter(@NonNull String chapterId);
  void deleteChapters();
  void refresh();
}
