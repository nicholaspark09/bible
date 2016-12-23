package com.apps.nicholaspark.bible.ui.home;

import com.apps.nicholaspark.bible.AppComponent;
import com.apps.nicholaspark.bible.R;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.apps.nicholaspark.bible.ui.ControllerResult;
import com.apps.nicholaspark.bible.ui.base.BaseController;

import javax.inject.Inject;

/**
 * Created by nicholaspark on 11/30/16.
 */
public final class HomeView extends BaseController implements HomeContract.View {


  @Override
  public void showIntroUi() {

  }

  @Override
  protected int layoutId() {
    return R.layout.home_view;
  }

  @Override
  protected void inject(AppComponent component) {
    component.inject(this);
  }

  @Override
  public void onResult(ControllerResult result) {

  }
}
