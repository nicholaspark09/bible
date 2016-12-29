package com.apps.nicholaspark.bible.ui.home;

import com.apps.nicholaspark.bible.data.book.BookRepository;
import com.apps.nicholaspark.bible.data.chapter.ChapterRepository;
import com.apps.nicholaspark.bible.data.verse.VerseRepository;
import com.apps.nicholaspark.bible.data.vo.Verse;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by nicholaspark on 11/30/16.
 */
@ApplicationScope
public final class HomePresenter implements HomeContract.Presenter {

  // Local visibility for easier testing
  CompositeDisposable disposables;
  private final BookRepository bookRepository;
  private final VerseRepository verseRepository;
  private final ChapterRepository chapterRepository;
  private HomeContract.View view;
  Scheduler ioThread;
  Scheduler mainThread;


  @Inject
  HomePresenter(VerseRepository verseRepository,
                BookRepository bookRepository,
                ChapterRepository chapterRepository,
                @Named("ioThread") Scheduler ioThread,
                @Named("mainThread") Scheduler mainThread) {
    this.verseRepository = verseRepository;
    this.bookRepository = bookRepository;
    this.chapterRepository = chapterRepository;
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
  public void init(String chapterId) {
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
    if (chapterId.length()>0) {

      String title = chapterRepository.chapter.parent().book().toString() + " " + chapterRepository.chapter.name();
      view.showTitle(title);
      Timber.d("The title was " + chapterRepository.chapter.toString());
      Disposable verseDisposable =
              verseRepository.getVerses(chapterId)
                      .subscribeOn(ioThread)
                      .observeOn(mainThread)
                      .subscribe(
                              this::openVerses,
                              this::onError
                      );
      disposables.add(verseDisposable);
    }
  }

  @Override
  public void openVerses(List<Verse> verses) {
    view.showVerses(verses);
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
