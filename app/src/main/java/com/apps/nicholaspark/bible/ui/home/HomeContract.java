package com.apps.nicholaspark.bible.ui.home;

import com.apps.nicholaspark.bible.ui.base.BasePresenter;

/**
 * Created by nicholaspark on 11/30/16.
 */

public interface HomeContract {

  interface View {
    void showIntroUi();
    void setLoadingIndicator(boolean isLoading);
    void showError(String error);
  }

  interface Presenter extends BasePresenter {
    void openIntro();
    void setView(View view);
    void openBookLoadingState(Boolean loadingState);
  }
}
