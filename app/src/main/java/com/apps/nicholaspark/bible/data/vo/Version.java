package com.apps.nicholaspark.bible.data.vo;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nicholaspark on 12/18/16.
 */

@AutoValue public abstract class Version {

  public static Version create(String lang, String copyright,
                               String langName, String abbreviation,
                               String id, String name) {
    return new AutoValue_Version(lang, copyright,
            langName, abbreviation,
            id, name);
  }

  public static TypeAdapter<Version> typeAdapter(Gson gson) {
    return new AutoValue_Version.GsonTypeAdapter(gson);
  }
  @Nullable
  public abstract String lang();
  @Nullable
  public abstract String copyright();
  @SerializedName("lang_name")
  @Nullable
  public abstract String langName();
  @Nullable
  public abstract String abbreviation();
  public abstract String id();
  public abstract String name();
}
