package com.apps.nicholaspark.bible.data.vo;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/29/16.
 */

@AutoValue public abstract class VerseParent {

  public static VerseParent create(Chapter chapter) {
    return new AutoValue_VerseParent(chapter);
  }

  public static TypeAdapter<VerseParent> typeAdapter(Gson gson) {
    return new AutoValue_VerseParent.GsonTypeAdapter(gson);
  }

  @Nullable
  public abstract Chapter chapter();
}
