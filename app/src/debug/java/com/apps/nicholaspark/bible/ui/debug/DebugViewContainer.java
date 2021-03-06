package com.apps.nicholaspark.bible.ui.debug;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.support.v4.view.GravityCompat;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.apps.nicholaspark.bible.R;
import com.apps.nicholaspark.bible.ViewContainer;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

/**
 * An {@link ViewContainer} for debug builds which wraps a sliding drawer on the right that holds
 * all of the debug information and settings.
 */
public final class DebugViewContainer implements ViewContainer {

  @Override
  public ViewGroup forActivity(final Activity activity) {
    activity.setContentView(R.layout.debug_activity_frame);

    final ViewHolder viewHolder = new ViewHolder();
    ButterKnife.bind(viewHolder, activity);

    final Context drawerContext = new ContextThemeWrapper(activity, R.style.Theme_MyAccount_Debug);
    final DebugView debugView = new DebugView(drawerContext);
    viewHolder.debugDrawer.addView(debugView);
    Timber.d("Inside the DebugViewContainer setting up the layouts");

    // Set up the contextual actions to watch views coming in and out of the content area.

    viewHolder.drawerLayout.setDrawerShadow(R.drawable.debug_drawer_shadow, GravityCompat.END);
    viewHolder.drawerLayout.setDrawerListener(new DebugDrawerLayout.SimpleDrawerListener() {
      @Override
      public void onDrawerOpened(View drawerView) {
        debugView.onDrawerOpened();
      }
    });

    //Don't let the user know that debug is available
//    viewHolder.drawerLayout.postDelayed(() -> {
//      viewHolder.drawerLayout.openDrawer(GravityCompat.END);
//      Toast.makeText(drawerContext, R.string.welcome_to_debug, Toast.LENGTH_LONG).show();
//    }, 1000);


    //riseAndShine(activity);
    return viewHolder.content;
  }

  /**
   * Show the activity over the lock-screen and wake up the device. If you launched the app manually
   * both of these conditions are already true. If you deployed from the IDE, however, this will
   * save you from hundreds of power button presses and pattern swiping per day!
   */
  public static void riseAndShine(Activity activity) {
    activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

    PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
    PowerManager.WakeLock lock =
            power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
    lock.acquire();
    lock.release();
  }

  static class ViewHolder {
    @BindView(R.id.debug_drawer_layout)
    DebugDrawerLayout drawerLayout;
    @BindView(R.id.debug_content)
    FrameLayout content;
    @BindView(R.id.debug_drawer)
    ViewGroup debugDrawer;
  }


}
