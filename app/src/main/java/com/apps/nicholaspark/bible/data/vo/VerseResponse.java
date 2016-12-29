package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/29/16.
 */

@AutoValue public abstract class VerseResponse {

  public static VerseResponse create(VerseResponseObject object) {
    return new AutoValue_VerseResponse(object);
  }

  public static TypeAdapter<VerseResponse> typeAdapter(Gson gson) {
    return new AutoValue_VerseResponse.GsonTypeAdapter(gson);
  }

  public abstract VerseResponseObject response();
}
