package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Created by nicholaspark on 12/29/16.
 */

@AutoValue public abstract class VerseResponseObject {

  public static VerseResponseObject create(List<Verse> verses) {
    return new AutoValue_VerseResponseObject(verses);
  }

  public static TypeAdapter<VerseResponseObject> typeAdapter(Gson gson) {
    return new AutoValue_VerseResponseObject.GsonTypeAdapter(gson);
  }

  public abstract List<Verse> verses();
}
