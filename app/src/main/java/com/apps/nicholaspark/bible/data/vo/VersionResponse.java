package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Created by nicholaspark on 12/18/16.
 */

@AutoValue public abstract class VersionResponse {
  public static VersionResponse create(Response response) {
    return new AutoValue_VersionResponse(response);
  }

  public static TypeAdapter<VersionResponse> typeAdapter(Gson gson) {
    return new AutoValue_VersionResponse.GsonTypeAdapter(gson);
  }

  public abstract Response response();
}
