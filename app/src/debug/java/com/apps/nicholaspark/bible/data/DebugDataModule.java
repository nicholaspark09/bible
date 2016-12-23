package com.apps.nicholaspark.bible.data;

import android.app.Application;

import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.qualifier.AnimationSpeed;
import com.apps.nicholaspark.bible.data.qualifier.IsMockMode;
import com.apps.nicholaspark.bible.data.qualifier.NetworkDelay;
import com.apps.nicholaspark.bible.data.qualifier.NetworkFailurePercent;
import com.apps.nicholaspark.bible.data.qualifier.NetworkVariancePercent;
import com.apps.nicholaspark.bible.data.qualifier.PicassoDebugging;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import preferences2.Preference;
import preferences2.RxSharedPreferences;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by nicholaspark on 11/30/16.
 */
@Module
public final class DebugDataModule {
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false;

  @Provides
  @ApplicationScope
  Preference<HttpLoggingInterceptor.Level> provideLogLevel(RxSharedPreferences preferences) {
    return preferences.getEnum("debug_log_level", HttpLoggingInterceptor.Level.BASIC, HttpLoggingInterceptor.Level.class);
  }

  @Provides
  @ApplicationScope
  HttpLoggingInterceptor provideLoggingInterceptor(Preference<HttpLoggingInterceptor.Level> logLevel) {
    return new HttpLoggingInterceptor(Timber::d).setLevel(logLevel.get());
  }

  @Provides
  @ApplicationScope
  OkHttpClient provideOkHttpClient(Application app, HttpLoggingInterceptor loggingInterceptor) {
    return DataModule.createOkHttpClient(app).addInterceptor(loggingInterceptor).build();
  }

  @Provides
  @ApplicationScope
  Preference<Env> provideEnv(RxSharedPreferences preferences) {
    return preferences.getEnum("debug_env", Env.MOCK_MODE, Env.class);
  }

  @Provides
  @ApplicationScope
  @IsMockMode
  boolean provideIsMockMode(Preference<Env> endpoint) {
    return endpoint.get().isMockMode();
  }

  @Provides
  @ApplicationScope
  @NetworkDelay
  Preference<Long> provideNetworkDelay(RxSharedPreferences preferences) {
    return preferences.getLong("debug_network_delay", 2000l);
  }

  @Provides
  @ApplicationScope
  @NetworkFailurePercent
  Preference<Integer> provideNetworkFailurePercent(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_network_failure_percent", 3);
  }

  @Provides
  @ApplicationScope
  @NetworkVariancePercent
  Preference<Integer> provideNetworkVariancePercent(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_network_variance_percent", 20);
  }

  @Provides
  @ApplicationScope
  @AnimationSpeed
  Preference<Integer> provideAnimationSpeed(RxSharedPreferences preferences) {
    return preferences.getInteger("debug_animation_speed", 1);
  }

  @Provides
  @ApplicationScope
  @PicassoDebugging
  Preference<Boolean> providePicassoDebugging(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }


  @Provides
  @ApplicationScope
  NetworkBehavior provideBehavior(@NetworkDelay Preference<Long> networkDelay,
                                  @NetworkFailurePercent Preference<Integer> networkFailurePercent,
                                  @NetworkVariancePercent Preference<Integer> networkVariancePercent) {
    NetworkBehavior behavior = NetworkBehavior.create();
    behavior.setDelay(networkDelay.get(), MILLISECONDS);
    behavior.setFailurePercent(networkFailurePercent.get());
    behavior.setVariancePercent(networkVariancePercent.get());
    return behavior;
  }

  @Provides
  @ApplicationScope
  MockRetrofit provideMockRetrofit(Retrofit retrofit, NetworkBehavior behavior) {
    return new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build();
  }

  @Provides
  @ApplicationScope
  BibleApi provideBibleApi(@IsMockMode boolean isMockMode,
                           Retrofit retrofit,
                           MockRetrofit mockRetrofit) {
    if (isMockMode) {
      // TODO actually create a mock response for the mock version
    }
    return retrofit.create(BibleApi.class);
  }
}
