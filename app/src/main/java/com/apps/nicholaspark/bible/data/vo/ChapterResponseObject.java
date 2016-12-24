package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Created by nicholaspark on 12/23/16.
 */

@AutoValue public abstract class ChapterResponseObject {

  public static ChapterResponseObject create(List<Chapter> chapters) {
    return new AutoValue_ChapterResponseObject(chapters);
  }

  public static TypeAdapter<ChapterResponseObject> typeAdapter(Gson gson) {
    return new AutoValue_ChapterResponseObject.GsonTypeAdapter(gson);
  }

  public abstract List<Chapter> chapters();
}
