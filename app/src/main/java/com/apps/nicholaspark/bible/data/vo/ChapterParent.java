package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/23/16.
 */

@AutoValue public abstract class ChapterParent {

  public static ChapterParent create(Book book) {
    return new AutoValue_ChapterParent(book);
  }

  public static TypeAdapter<ChapterParent> typeAdapter(Gson gson) {
    return new AutoValue_ChapterParent.GsonTypeAdapter(gson);
  }

  public abstract Book book();
}
