package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Created by nicholaspark on 12/18/16.
 */

@AutoValue public abstract class Response {

  public static Response create(List<Version> versions) {
    return new AutoValue_Response(versions);
  }

  public static TypeAdapter<Response> typeAdapter(Gson gson) {
    return new AutoValue_Response.GsonTypeAdapter(gson);
  }

  public abstract List<Version> versions();
}
