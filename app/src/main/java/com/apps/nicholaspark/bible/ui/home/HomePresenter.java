package com.apps.nicholaspark.bible.ui.home;

import com.apps.nicholaspark.bible.data.book.BookRepository;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by nicholaspark on 11/30/16.
 */
@ApplicationScope
public final class HomePresenter implements HomeContract.Presenter {

  // Local visibility for easier testing
  CompositeDisposable disposables;
  private final BookRepository bookRepository;
  private HomeContract.View view;
  Scheduler ioThread;
  Scheduler mainThread;


  @Inject
  HomePresenter(BookRepository bookRepository, @Named("ioThread") Scheduler ioThread,
                @Named("mainThread") Scheduler mainThread) {
    this.bookRepository = bookRepository;
    this.ioThread = ioThread;
    this.mainThread = mainThread;
  }


  @Override
  public void openIntro() {

  }

  @Override
  public void setView(HomeContract.View view) {
    this.view = view;
  }



  @Override
  public void init() {
    Disposable disposable =
            bookRepository.getLoadingState()
            .subscribeOn(ioThread)
            .observeOn(mainThread)
            .subscribe(
                    // onNext
                    this::openBookLoadingState,
                    this::onError
            );
    disposables.add(disposable);
  }

  @Override
  public void subscribe() {
    disposables = new CompositeDisposable();
  }

  @Override
  public void unsubscribe() {
    disposables.clear();
  }

  @Override
  public void openBookLoadingState(Boolean loadingState) {
    view.setLoadingIndicator(loadingState);
  }

  @Override
  public void onError(Throwable throwable) {
    view.showError(throwable.getMessage());
  }
}
