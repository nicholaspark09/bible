package com.apps.nicholaspark.bible.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.nicholaspark.bible.AppComponent;
import com.apps.nicholaspark.bible.R;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.apps.nicholaspark.bible.ui.ControllerResult;
import com.apps.nicholaspark.bible.ui.base.BaseController;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by nicholaspark on 11/30/16.
 */
public final class HomeView extends BaseController implements HomeContract.View {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progress_bar) ProgressBar progressBar;
  @Inject HomePresenter presenter;

  @Override
  protected int layoutId() {
    return R.layout.home_view;
  }

  @Override
  protected void onAttach(@NonNull View view) {
    super.onAttach(view);
    setActionBar(toolbar);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle("The Bible");
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.show();
    setHasOptionsMenu(true);
    presenter.setView(this);
    presenter.subscribe();
    presenter.init();
  }

  @Override
  protected void onDetach(@NonNull View view) {
    presenter.unsubscribe();
    super.onDetach(view);
  }

  @Override
  public void onResult(ControllerResult result) {

  }

  @Override
  public void showIntroUi() {

  }

  @Override
  public void setLoadingIndicator(boolean isLoading) {
    if(isLoading) {
      progressBar.setVisibility(View.VISIBLE);
    }else {
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void showError(String error) {
    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
  }


  @Override
  protected void inject(AppComponent component) {
    component.inject(this);
  }


}
