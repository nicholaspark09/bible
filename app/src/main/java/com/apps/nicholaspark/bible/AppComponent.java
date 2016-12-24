package com.apps.nicholaspark.bible;

import com.apps.nicholaspark.bible.ui.home.HomeView;

/**
 * Created by nicholaspark on 11/29/16.
 */

public interface AppComponent {
  void inject(BibleApp app);
  void inject(MainActivity mainActivity);
  void inject(SplashActivity splashActivity);
  void inject(HomeView homeView);
}
