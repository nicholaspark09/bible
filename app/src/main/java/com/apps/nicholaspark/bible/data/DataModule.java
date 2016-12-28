package com.apps.nicholaspark.bible.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.book.BookDataSource;
import com.apps.nicholaspark.bible.data.book.local.BookLocalDataSource;
import com.apps.nicholaspark.bible.data.book.remote.BookRemoteDataSource;
import com.apps.nicholaspark.bible.data.chapter.ChapterDataSource;
import com.apps.nicholaspark.bible.data.chapter.remote.ChapterRemoteDataSource;
import com.apps.nicholaspark.bible.data.oauth.OauthInterceptor;
import com.apps.nicholaspark.bible.data.qualifier.Local;
import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.version.VersionDataSource;
import com.apps.nicholaspark.bible.data.version.remote.VersionRemoteDataSource;
import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.apps.nicholaspark.bible.util.GsonPreferenceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import preferences2.Preference;
import preferences2.RxSharedPreferences;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;

/**
 * Created by nicholaspark on 11/30/16.
 */
@Module
public final class DataModule {
  private static final String SHARED_PREFERENCES_LOCATION = "my.account.prefs";
  private static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  static OkHttpClient.Builder createOkHttpClient(Application app) {
    //Install a HTTP cache in the application cache directory.
    File cacheDir = new File(app.getCacheDir(), "https");
    Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
    return new OkHttpClient.Builder().cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS);
  }

  @Provides
  @ApplicationScope
  Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(BibleTypeAdapterFactory.create()).create();
  }

  @Provides
  @ApplicationScope
  SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences(SHARED_PREFERENCES_LOCATION, Context.MODE_PRIVATE);
  }

  @Provides
  @ApplicationScope
  RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs);
  }

  @Provides
  @ApplicationScope
  Retrofit provideAuthedRetrofit(@Remote OkHttpClient client, Gson gson) {
    return new Retrofit.Builder().client(client)
            .baseUrl("https://bibles.org/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
  }

  @Provides
  @ApplicationScope
  @Remote
  VersionDataSource providerVersionRemoteSource(BibleApi bibleApi) {
    return new VersionRemoteDataSource(bibleApi);
  }

  @Provides
  @ApplicationScope
  AssetManager providerAssetManager(Application app) {
    return app.getAssets();
  }

  @Provides
  @ApplicationScope
  @Remote
  OkHttpClient provideRemoteOkHttpClient(OkHttpClient client, OauthInterceptor oauthInterceptor) {
    return createApiClient(client, oauthInterceptor).build();
  }

  @Provides
  @ApplicationScope
  Preference<Version> provideVersion(RxSharedPreferences sharedPreferences, Gson gson) {
    Version version = Version.create("eng-US", "", "English (US)", "NASB", "eng-NASB", "New American Standard Bible");
    return sharedPreferences.getObject("key.defaultversion", version, new GsonPreferenceAdapter<>(gson, Version.class));
  }

  @Provides
  @ApplicationScope
  @Named("BibleBooks")
  Preference<String> provideBooks(RxSharedPreferences sharedPreferences) {
    return sharedPreferences.getString("key.biblebooks", "");
  }

  @Provides
  @ApplicationScope
  @Named("ioThread")
  Scheduler provideIoThread() {
    return Schedulers.io();
  }

  @Provides
  @ApplicationScope
  @Named("mainThread")
  Scheduler provideUiThread() {
    return AndroidSchedulers.mainThread();
  }


  @Provides
  @ApplicationScope
  @Remote
  BookDataSource provideRemoteBookDataSource(BibleApi bibleApi) {
    return new BookRemoteDataSource(bibleApi);
  }

  @Provides
  @ApplicationScope
  @Local
  BookDataSource provideLocalBookDataSource(@Named("BibleBooks") Preference<String> books, Gson gson) {
    return new BookLocalDataSource(books, gson);
  }

  @Provides
  @ApplicationScope
  @Remote
  ChapterDataSource provideChapterRemoteDataSource(BibleApi bibleApi) {
    return new ChapterRemoteDataSource(bibleApi);
  }

  static OkHttpClient.Builder createApiClient(OkHttpClient client, Interceptor interceptor) {
    return client.newBuilder()
            .addInterceptor(interceptor);
  }
}
