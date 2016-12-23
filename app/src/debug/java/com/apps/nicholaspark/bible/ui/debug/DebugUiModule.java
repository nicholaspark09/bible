package com.apps.nicholaspark.bible.ui.debug;

import com.apps.nicholaspark.bible.ViewContainer;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nicholaspark on 11/30/16.
 */
@Module
public final class DebugUiModule {

  @Provides
  @ApplicationScope
  ViewContainer provideViewContainer() {
    return new DebugViewContainer();
  }
}
