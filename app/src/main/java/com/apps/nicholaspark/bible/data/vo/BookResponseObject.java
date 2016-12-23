package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Created by nicholaspark on 12/21/16.
 */

@AutoValue public abstract class BookResponseObject {

  public static BookResponseObject create(List<Book> books){
    return new AutoValue_BookResponseObject(books);
  }

  public static TypeAdapter<BookResponseObject> typeAdapter(Gson gson) {
    return new AutoValue_BookResponseObject.GsonTypeAdapter(gson);
  }

  public abstract List<Book> books();
}
