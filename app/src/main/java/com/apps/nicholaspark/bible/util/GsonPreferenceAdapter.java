package com.apps.nicholaspark.bible.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import preferences2.Preference;

public final class GsonPreferenceAdapter<T> implements Preference.Adapter<T> {
  private final Class<T> clazz;
  private final Gson gson;

  public GsonPreferenceAdapter(Gson gson, Class<T> clazz) {
    this.clazz = clazz;
    this.gson = gson;
  }

  @Override public T get(@NonNull String key, @NonNull SharedPreferences preferences) {
    return gson.fromJson(preferences.getString(key, ""), clazz);
  }

  @Override public void set(@NonNull String key, @NonNull T value, @NonNull SharedPreferences.Editor editor) {
    editor.putString(key, gson.toJson(value)).apply();
  }
}
