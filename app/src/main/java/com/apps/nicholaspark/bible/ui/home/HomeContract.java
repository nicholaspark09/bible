package com.apps.nicholaspark.bible.ui.home;

import com.apps.nicholaspark.bible.data.vo.Verse;
import com.apps.nicholaspark.bible.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nicholaspark on 11/30/16.
 */

public interface HomeContract {

  interface View {
    void showIntroUi();
    void showTitle(String title);
    void setLoadingIndicator(boolean isLoading);
    void showError(String error);
    void showVerses(List<Verse> verses);
  }

  interface Presenter extends BasePresenter {
    void init(String chapterId);
    void openIntro();
    void setView(View view);
    void openVerses(List<Verse> verses);
    void openBookLoadingState(Boolean loadingState);
  }
}
