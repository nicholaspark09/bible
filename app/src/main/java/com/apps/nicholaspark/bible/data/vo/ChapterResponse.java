package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/23/16.
 */

@AutoValue public abstract class ChapterResponse {

  public static ChapterResponse create(ChapterResponseObject responseObject) {
    return new AutoValue_ChapterResponse(responseObject);
  }

  public static TypeAdapter<ChapterResponse> typeAdapter(Gson gson) {
    return new AutoValue_ChapterResponse.GsonTypeAdapter(gson);
  }

  public abstract ChapterResponseObject response();
}
