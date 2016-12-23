package com.apps.nicholaspark.bible;

import android.app.Application;

import com.apps.nicholaspark.bible.data.DataModule;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nicholaspark on 11/29/16.
 */
@Module(includes = DataModule.class)
final class BibleModule {
  private final BibleApp app;

  BibleModule(BibleApp app) {
    this.app = app;
  }

  @Provides
  @ApplicationScope
  Application provideApp() {
    return app;
  }
}
