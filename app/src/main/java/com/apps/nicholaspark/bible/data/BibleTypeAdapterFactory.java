package com.apps.nicholaspark.bible.data;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Created by nicholaspark on 11/30/16.
 */
@GsonTypeAdapterFactory
abstract class BibleTypeAdapterFactory implements TypeAdapterFactory {
  static TypeAdapterFactory create() {
    return new AutoValueGson_BibleTypeAdapterFactory();
  }
}
