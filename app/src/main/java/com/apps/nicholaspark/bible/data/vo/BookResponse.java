package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/21/16.
 */

@AutoValue public abstract class BookResponse {

  public static BookResponse create(BookResponseObject object) {
    return new AutoValue_BookResponse(object);
  }

  public static TypeAdapter<BookResponseObject> typeAdapter(Gson gson) {
    return new AutoValue_BookResponseObject.GsonTypeAdapter(gson);
  }

  public abstract BookResponseObject response();
}
