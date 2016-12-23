package com.apps.nicholaspark.bible.data.vo;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by nicholaspark on 12/18/16.
 */

@AutoValue public abstract class Book {

  public static Book create(String id, String name, int order,
                            String testament, String abbr,
                            String osis, String group, String version, String copy) {
    return new AutoValue_Book(id, name, order, testament, abbr,
            osis, group, version, copy);
  }

  public static TypeAdapter<Book> typeAdapter(Gson gson) {
    return new AutoValue_Book.GsonTypeAdapter(gson);
  }


  public abstract String id();
  public abstract String name();
  public abstract int ord();
  public abstract String testament();
  public abstract String abbr();
  @SerializedName("osis_end")
  public abstract String osisEnd();
  @SerializedName("book_group_id")
  public abstract String bookGroupId();
  @SerializedName("version_id")
  public abstract String versionId();
  @Nullable
  public abstract String copyright();
}
