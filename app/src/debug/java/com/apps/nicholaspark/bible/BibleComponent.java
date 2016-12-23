package com.apps.nicholaspark.bible;

import com.apps.nicholaspark.bible.data.DebugDataModule;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.apps.nicholaspark.bible.ui.debug.DebugUiModule;
import com.apps.nicholaspark.bible.ui.debug.DebugView;

import dagger.Component;

/**
 * Created by nicholaspark on 11/29/16.
 */
@ApplicationScope
@Component(modules = {BibleModule.class, DebugUiModule.class, DebugDataModule.class})
public interface BibleComponent extends AppComponent {
  void inject(DebugView debugView);

  final class Initializer {
    private Initializer() {}

   static BibleComponent init(BibleApp app) {
     return DaggerBibleComponent.builder().bibleModule(new BibleModule(app)).build();
   }
  }
}
