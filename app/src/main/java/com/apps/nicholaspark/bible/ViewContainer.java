package com.apps.nicholaspark.bible;

import android.app.Activity;
import android.view.ViewGroup;

import static butterknife.ButterKnife.findById;

/**
 * Created by nicholaspark on 11/9/16.
 */

public interface ViewContainer {

  ViewContainer DEFAULT = activity -> findById(activity, android.R.id.content);

  ViewGroup forActivity(Activity activity);
}
