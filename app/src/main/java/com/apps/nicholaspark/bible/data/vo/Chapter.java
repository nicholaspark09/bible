package com.apps.nicholaspark.bible.data.vo;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nicholaspark on 12/18/16.
 */

@AutoValue public abstract class Chapter {

  public static Chapter create(String id, String name,
                               ChapterParent parent, String label,
                               NextChapter nextChapter, PreviousChapter previousChapter) {
    return new AutoValue_Chapter(id, name,
            parent, label,
            nextChapter, previousChapter);
  }

  public static TypeAdapter<Chapter> typeAdapter(Gson gson) {
    return new AutoValue_Chapter.GsonTypeAdapter(gson);
  }

  public abstract String id();

  @SerializedName("chapter")
  @Nullable
  public abstract String name();

  @SerializedName("parent")
  @Nullable
  public abstract ChapterParent parent();

  @Nullable
  public abstract String label();

  @SerializedName("next")
  @Nullable
  public abstract NextChapter nextChapter();

  @SerializedName("previous")
  @Nullable
  public abstract PreviousChapter previousChapter();

}
