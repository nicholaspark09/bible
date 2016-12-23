package com.apps.nicholaspark.bible.ui.debug;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.nicholaspark.bible.BuildConfig;
import com.apps.nicholaspark.bible.R;
import com.apps.nicholaspark.bible.data.Env;
import com.apps.nicholaspark.bible.data.qualifier.AnimationSpeed;
import com.apps.nicholaspark.bible.data.qualifier.IsMockMode;
import com.apps.nicholaspark.bible.data.qualifier.NetworkDelay;
import com.apps.nicholaspark.bible.data.qualifier.NetworkFailurePercent;
import com.apps.nicholaspark.bible.data.qualifier.NetworkVariancePercent;
import com.apps.nicholaspark.bible.data.qualifier.PicassoDebugging;
import com.apps.nicholaspark.bible.di.DaggerService;
import com.apps.nicholaspark.bible.ui.misc.EnumAdapter;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.leakcanary.internal.DisplayLeakActivity;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAccessor;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import preferences2.Preference;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

import static com.apps.nicholaspark.bible.util.Strings.truncateAt;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class DebugView extends FrameLayout {
  private static final DateTimeFormatter DATE_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US).withZone(ZoneId.systemDefault());

  @BindView(R.id.debug_contextual_title) View contextualTitleView;
  @BindView(R.id.debug_contextual_list) LinearLayout contextualListView;

  @BindView(R.id.debug_network_endpoint) Spinner endpointView;
  @BindView(R.id.debug_network_delay) Spinner networkDelayView;
  @BindView(R.id.debug_network_variance) Spinner networkVarianceView;
  @BindView(R.id.debug_network_error) Spinner networkErrorView;
  @BindView(R.id.debug_network_logging) Spinner networkLoggingView;

  @BindView(R.id.debug_user_response) Spinner userResponseView;
  @BindView(R.id.debug_payment_response) Spinner paymentResponseView;

  @BindView(R.id.debug_ui_animation_speed) Spinner uiAnimationSpeedView;

  @BindView(R.id.debug_build_name) TextView buildNameView;
  @BindView(R.id.debug_build_code) TextView buildCodeView;
  @BindView(R.id.debug_build_sha) TextView buildShaView;
  @BindView(R.id.debug_build_date) TextView buildDateView;

  @BindView(R.id.debug_device_make) TextView deviceMakeView;
  @BindView(R.id.debug_device_model) TextView deviceModelView;
  @BindView(R.id.debug_device_resolution) TextView deviceResolutionView;
  @BindView(R.id.debug_device_density) TextView deviceDensityView;
  @BindView(R.id.debug_device_release) TextView deviceReleaseView;
  @BindView(R.id.debug_device_api) TextView deviceApiView;

  @BindView(R.id.debug_picasso_indicators) Switch picassoIndicatorView;
  @BindView(R.id.debug_picasso_cache_size) TextView picassoCacheSizeView;
  @BindView(R.id.debug_picasso_cache_hit) TextView picassoCacheHitView;
  @BindView(R.id.debug_picasso_cache_miss) TextView picassoCacheMissView;
  @BindView(R.id.debug_picasso_decoded) TextView picassoDecodedView;
  @BindView(R.id.debug_picasso_decoded_total) TextView picassoDecodedTotalView;
  @BindView(R.id.debug_picasso_decoded_avg) TextView picassoDecodedAvgView;
  @BindView(R.id.debug_picasso_transformed) TextView picassoTransformedView;
  @BindView(R.id.debug_picasso_transformed_total) TextView picassoTransformedTotalView;
  @BindView(R.id.debug_picasso_transformed_avg) TextView picassoTransformedAvgView;

  @BindView(R.id.debug_okhttp_cache_max_size) TextView okHttpCacheMaxSizeView;
  @BindView(R.id.debug_okhttp_cache_write_error) TextView okHttpCacheWriteErrorView;
  @BindView(R.id.debug_okhttp_cache_request_count) TextView okHttpCacheRequestCountView;
  @BindView(R.id.debug_okhttp_cache_network_count) TextView okHttpCacheNetworkCountView;
  @BindView(R.id.debug_okhttp_cache_hit_count) TextView okHttpCacheHitCountView;

  @Inject OkHttpClient client;
  @Inject @IsMockMode
  boolean isMockMode;
  @Inject
  Preference<HttpLoggingInterceptor.Level> logLevel;
  @Inject Preference<Env> env;
  @Inject @AnimationSpeed
  Preference<Integer> animationSpeed;
  @Inject @PicassoDebugging
  Preference<Boolean> picassoDebugging;
  @Inject NetworkBehavior behavior;
  @Inject @NetworkDelay
  Preference<Long> networkDelay;
  @Inject @NetworkFailurePercent
  Preference<Integer> networkFailurePercent;
  @Inject @NetworkVariancePercent
  Preference<Integer> networkVariancePercent;

  public DebugView(Context context) {
    this(context, null);
  }

  public DebugView(Context context, AttributeSet attrs) {
    super(context, attrs);
    DaggerService.getAppComponent(context).inject(this);
    LayoutInflater.from(context).inflate(R.layout.debug_view, this);
  }

  private static String getDensityString(DisplayMetrics displayMetrics) {
    switch (displayMetrics.densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "ldpi";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhdpi";
      case DisplayMetrics.DENSITY_XXHIGH:
        return "xxhdpi";
      case DisplayMetrics.DENSITY_XXXHIGH:
        return "xxxhdpi";
      case DisplayMetrics.DENSITY_TV:
        return "tvdpi";
      default:
        return String.valueOf(displayMetrics.densityDpi);
    }
  }

  private static String getSizeString(long b) {
    long bytes = b;
    String[] units = new String[] { "B", "KB", "MB", "GB" };
    int unit = 0;
    while (bytes >= 1024) {
      bytes /= 1024;
      unit += 1;
    }
    return bytes + units[unit];
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ButterKnife.bind(this);

    setupNetworkSection();
    setupMockBehaviorSection();
    setupBuildSection();
    setupDeviceSection();
    setupPicassoSection();
    setupOkHttpCacheSection();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  private void setupNetworkSection() {
    final Env currentEnv = env.get();
    final EnumAdapter<Env> endpointAdapter = new EnumAdapter<>(getContext(), Env.class);
    endpointView.setAdapter(endpointAdapter);
    endpointView.setSelection(currentEnv.ordinal());
    //TODO Replace with RxBindings when it comes out
    endpointView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Env selected = endpointAdapter.getItem(position);
        if (selected != currentEnv) {
          Timber.d("Setting env to %s", selected.toString());
          env.set(selected);
          ProcessPhoenix.triggerRebirth(getContext());
        }
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    final NetworkDelayAdapter delayAdapter = new NetworkDelayAdapter(getContext());
    networkDelayView.setAdapter(delayAdapter);
    networkDelayView.setSelection(NetworkDelayAdapter.getPositionForValue(behavior.delay(MILLISECONDS)));
    networkDelayView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Long selected = delayAdapter.getItem(position);
        if (selected != behavior.delay(MILLISECONDS)) {
          Timber.d("Setting network delay to %sms", selected);
          behavior.setDelay(selected, MILLISECONDS);
          networkDelay.set(selected);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    final NetworkVarianceAdapter varianceAdapter = new NetworkVarianceAdapter(getContext());
    networkVarianceView.setAdapter(varianceAdapter);
    networkVarianceView.setSelection(NetworkVarianceAdapter.getPositionForValue(behavior.variancePercent()));
    networkVarianceView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Integer selected = varianceAdapter.getItem(position);
        if (selected != behavior.variancePercent()) {
          Timber.d("Setting network variance to %s%%", selected);
          behavior.setVariancePercent(selected);
          networkVariancePercent.set(selected);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    final NetworkErrorAdapter errorAdapter = new NetworkErrorAdapter(getContext());
    networkErrorView.setAdapter(errorAdapter);
    networkErrorView.setSelection(NetworkErrorAdapter.getPositionForValue(behavior.failurePercent()));
    networkErrorView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Integer selected = errorAdapter.getItem(position);
        if (selected != behavior.failurePercent()) {
          Timber.d("Setting network error to %s%%", selected);
          behavior.setFailurePercent(selected);
          networkFailurePercent.set(selected);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });
    final HttpLoggingInterceptor.Level level = logLevel.get();
    final EnumAdapter<HttpLoggingInterceptor.Level> loggingAdapter = new EnumAdapter<>(getContext(), HttpLoggingInterceptor.Level.class);
    networkLoggingView.setAdapter(loggingAdapter);
    networkLoggingView.setSelection(level.ordinal());
    networkLoggingView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        HttpLoggingInterceptor.Level selected = loggingAdapter.getItem(position);
        if (selected != level) {
          Timber.d("Setting logging level to %s", selected);
          logLevel.set(selected);
          ProcessPhoenix.triggerRebirth(getContext());
        }
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    if (currentEnv == Env.MOCK_MODE) {
      // Disable network proxy if we are in mock mode.
      networkLoggingView.setEnabled(false);
    } else {
      // Disable network controls if we are not in mock mode.
      networkDelayView.setEnabled(false);
      networkVarianceView.setEnabled(false);
      networkErrorView.setEnabled(false);
    }
  }

  private void setupMockBehaviorSection() {

  }

  private <T extends Enum<T>> void configureResponseSpinner(Spinner spinner, final Preference<T> pref, final Class<T> responseClass) {
    final EnumAdapter<T> adapter = new EnumAdapter<>(getContext(), responseClass);
    spinner.setEnabled(isMockMode);
    spinner.setAdapter(adapter);
    spinner.setSelection(pref.get().ordinal());
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        T selected = adapter.getItem(position);
        if (selected != pref.get()) {
          Timber.d("Setting %s to %s", responseClass.getSimpleName(), selected);
          pref.set(selected);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
      }
    });
  }


  @OnClick(R.id.debug_leaks_show) void showLeaks() {
    Intent intent = new Intent(getContext(), DisplayLeakActivity.class);
    getContext().startActivity(intent);
  }

  private void setupBuildSection() {
    buildNameView.setText(BuildConfig.VERSION_NAME);
    buildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
    buildShaView.setText(BuildConfig.GIT_SHA);

    TemporalAccessor buildTime = Instant.ofEpochSecond(BuildConfig.GIT_TIMESTAMP);
    buildDateView.setText(DATE_DISPLAY_FORMAT.format(buildTime));
  }

  private void setupDeviceSection() {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    String densityBucket = getDensityString(displayMetrics);
    deviceMakeView.setText(truncateAt(Build.MANUFACTURER, 20));
    deviceModelView.setText(truncateAt(Build.MODEL, 20));
    deviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
    deviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
    deviceReleaseView.setText(Build.VERSION.RELEASE);
    deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
  }

  private void setupPicassoSection() {
//    boolean picassoDebuggingValue = picassoDebugging.get();
//    picasso.setIndicatorsEnabled(picassoDebuggingValue);
//    picassoIndicatorView.setChecked(picassoDebuggingValue);
//    picassoIndicatorView.setOnCheckedChangeListener((button, isChecked) -> {
//      Timber.d("Setting Picasso debugging to " + isChecked);
//      picasso.setIndicatorsEnabled(isChecked);
//      picassoDebugging.set(isChecked);
//    });
//
//    refreshPicassoStats();
  }

  private void refreshPicassoStats() {
//    StatsSnapshot snapshot = picasso.getSnapshot();
//    String size = getSizeString(snapshot.size);
//    String total = getSizeString(snapshot.maxSize);
//    int percentage = (int) ((1f * snapshot.size / snapshot.maxSize) * 100);
//    picassoCacheSizeView.setText(size + " / " + total + " (" + percentage + "%)");
//    picassoCacheHitView.setText(String.valueOf(snapshot.cacheHits));
//    picassoCacheMissView.setText(String.valueOf(snapshot.cacheMisses));
//    picassoDecodedView.setText(String.valueOf(snapshot.originalBitmapCount));
//    picassoDecodedTotalView.setText(getSizeString(snapshot.totalOriginalBitmapSize));
//    picassoDecodedAvgView.setText(getSizeString(snapshot.averageOriginalBitmapSize));
//    picassoTransformedView.setText(String.valueOf(snapshot.transformedBitmapCount));
//    picassoTransformedTotalView.setText(getSizeString(snapshot.totalTransformedBitmapSize));
//    picassoTransformedAvgView.setText(getSizeString(snapshot.averageTransformedBitmapSize));
  }

  private void setupOkHttpCacheSection() {
    Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
    okHttpCacheMaxSizeView.setText(getSizeString(cache.maxSize()));

    refreshOkHttpCacheStats();
  }

  private void refreshOkHttpCacheStats() {
    Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
    int writeTotal = cache.writeSuccessCount() + cache.writeAbortCount();
    int percentage = (int) ((1f * cache.writeAbortCount() / writeTotal) * 100);
    okHttpCacheWriteErrorView.setText(cache.writeAbortCount() + " / " + writeTotal + " (" + percentage + "%)");
    okHttpCacheRequestCountView.setText(String.valueOf(cache.requestCount()));
    okHttpCacheNetworkCountView.setText(String.valueOf(cache.networkCount()));
    okHttpCacheHitCountView.setText(String.valueOf(cache.hitCount()));
  }

  private void applyAnimationSpeed(int multiplier) {
    try {
      Method method = ValueAnimator.class.getDeclaredMethod("setDurationScale", float.class);
      method.invoke(null, (float) multiplier);
    } catch (Exception e) {
      throw new RuntimeException("Unable to apply animation speed.", e);
    }
  }

  public void onDrawerOpened() {
    refreshPicassoStats();
    refreshOkHttpCacheStats();
  }
}
