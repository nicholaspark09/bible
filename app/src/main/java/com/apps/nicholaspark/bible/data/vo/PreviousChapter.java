package com.apps.nicholaspark.bible.data.vo;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/23/16.
 */

@AutoValue public abstract class PreviousChapter {

  public static PreviousChapter create(Chapter chapter) {
    return new AutoValue_PreviousChapter(chapter);
  }

  public static TypeAdapter<PreviousChapter> typeAdapter(Gson gson) {
    return new AutoValue_PreviousChapter.GsonTypeAdapter(gson);
  }

  @Nullable
  public abstract Chapter chapter();
}
