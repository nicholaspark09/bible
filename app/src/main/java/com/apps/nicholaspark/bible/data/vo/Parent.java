package com.apps.nicholaspark.bible.data.vo;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by nicholaspark on 12/20/16.
 */

@AutoValue public abstract class Parent {

  public static Parent create(Version version) {
    return null;
  }

  public static TypeAdapter<Parent> typeAdapter(Gson gson) {
    return null;
  }

  public abstract Version version();
}
