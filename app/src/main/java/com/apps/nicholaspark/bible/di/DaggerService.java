package com.apps.nicholaspark.bible.di;

import android.content.Context;
import com.apps.nicholaspark.bible.BibleComponent;

/**
 * Created by nicholaspark on 11/29/16.
 */

public final class DaggerService {
  public static final String SERVICE_NAME = DaggerService.class.getName();

  private DaggerService() {
    throw new AssertionError("No instances");
  }

  @SuppressWarnings("WrongConstant")
  public static BibleComponent getAppComponent(Context context) {
    return (BibleComponent) context.getApplicationContext().getSystemService(SERVICE_NAME);
  }
}
