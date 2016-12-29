package com.apps.nicholaspark.bible.data.verse;

import android.support.annotation.NonNull;

import com.apps.nicholaspark.bible.data.vo.Verse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/29/16.
 */

public interface VerseDataSource {
  Observable<List<Verse>> getVerses(@NonNull String chapterId);
  Observable<Verse> getVerse(@NonNull String id);
  void saveVerse(@NonNull Verse verse);
  void deleteAll();
  void refresh();
}
