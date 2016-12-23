package com.apps.nicholaspark.bible;

import android.app.Application;
import android.os.StrictMode;

import com.apps.nicholaspark.bible.di.DaggerService;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import timber.log.Timber;

import static android.os.StrictMode.setThreadPolicy;
import static android.os.StrictMode.setVmPolicy;

/**
 * Created by nicholaspark on 11/29/16.
 */

public class BibleApp extends Application {

  @Inject Application app;
  private AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
      setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    Timber.i("Initializing App");
    AndroidThreeTen.init(this);
    LeakCanary.install(this);
    buildComponentAndInject();
  }

  @Override
  public Object getSystemService(String name) {
    if (name.equals(DaggerService.SERVICE_NAME) && appComponent != null) {
      return appComponent;
    }
    return super.getSystemService(name);
  }

  private void buildComponentAndInject() {
    appComponent = BibleComponent.Initializer.init(this);
    appComponent.inject(this);
  }
}
