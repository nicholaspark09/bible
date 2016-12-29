package com.apps.nicholaspark.bible.ui.base;

/**
 * Created by nicholaspark on 10/25/16.
 */

public interface BasePresenter {
    void subscribe();
    void unsubscribe();
    void onError(Throwable throwable);
}
