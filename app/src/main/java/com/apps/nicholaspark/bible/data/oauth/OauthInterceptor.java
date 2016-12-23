package com.apps.nicholaspark.bible.data.oauth;

import com.apps.nicholaspark.bible.BuildConfig;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nicholaspark on 12/18/16.
 */

@ApplicationScope
public final class OauthInterceptor implements Interceptor {

  @Inject
  public OauthInterceptor() {

  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder builder;
    String api_key = BuildConfig.API_KEY;
      builder = request.newBuilder()
              .header("Authorization", "Basic " + api_key);
      builder.addHeader("Accept", "application/json");
      builder.addHeader("Content-Type", "application/json");
    return chain.proceed(builder.build());
  }
}