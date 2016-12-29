package com.apps.nicholaspark.bible.data.vo;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nicholaspark on 12/29/16.
 */

@AutoValue public abstract class Verse {

  public static Verse create(String reference, String text,
                             String label, String verse,
                             String osisEnd, String id,
                             String lastVerse, VerseParent parent) {
    return new AutoValue_Verse(reference, text,
            label, verse, osisEnd, id,
            lastVerse, parent);
  }

  public static TypeAdapter<Verse> typeAdapter(Gson gson) {
    return new AutoValue_Verse.GsonTypeAdapter(gson);
  }

  @Nullable
  public abstract String reference();

  @Nullable
  public abstract String text();

  @Nullable
  public abstract String label();

  @Nullable
  public abstract String verse();

  @SerializedName("osis_end")
  @Nullable
  public abstract String osisEnd();

  @Nullable
  public abstract String id();

  @SerializedName("lastVerse")
  @Nullable
  public abstract String lastVerse();

  @SerializedName("parent")
  @Nullable
  public abstract VerseParent verseParent();
}
