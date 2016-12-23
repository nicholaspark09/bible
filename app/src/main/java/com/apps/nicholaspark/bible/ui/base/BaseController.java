package com.apps.nicholaspark.bible.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.nicholaspark.bible.AppComponent;
import com.apps.nicholaspark.bible.di.DaggerService;
import com.apps.nicholaspark.bible.ui.ControllerResultHandler;
import com.bluelinelabs.conductor.Controller;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseController extends Controller implements BaseView, ControllerResultHandler{

  private Unbinder unbinder;
  private boolean injected;

  protected BaseController() { }

  protected BaseController(Bundle args) {
    super(args);
  }

  protected void onViewBound(View view) {
  }

  @LayoutRes
  protected abstract int layoutId();

  protected abstract void inject(AppComponent component);

  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    View view = inflater.inflate(layoutId(), container, false);
    unbinder = ButterKnife.bind(this, view);
    if (!injected) {
      inject(DaggerService.getAppComponent(container.getContext()));
      injected = true;
    }
    onViewBound(view);
    return view;
  }


  @Override
  protected void onDestroyView(View view) {
    unbinder.unbind();
  }

  @Override
  protected void onDestroy() {
//    if (memoryLeakDetector != null) {
//      memoryLeakDetector.watch(this);
//    }
  }

  @Nullable
  protected ActionBar getActionBar() {
    return ((AppCompatActivity) getActivity()).getSupportActionBar();
  }

//  protected void setActionBar(Toolbar toolbar) {
//    ActionBarProvider actionBarProvider = ((ActionBarProvider) getActivity());
//    actionBarProvider.setActionBar(toolbar);
//  }
//
//  protected DrawerLayout getDrawerLayout() {
//    DrawerLayoutProvider layoutProvider = ((DrawerLayoutProvider) getActivity());
//    return layoutProvider.getDrawerLayout();
//  }
//
//  protected NavigationView getNavigationView() {
//    NavigationViewProvider viewProvider = ((NavigationViewProvider) getActivity());
//    return viewProvider.getNavigationView();
//  }

  @Override
  protected void onAttach(@NonNull View view) {
    super.onAttach(view);
  }
}
