package com.apps.nicholaspark.bible.data.version.remote;

import com.apps.nicholaspark.bible.BibleApp;
import com.apps.nicholaspark.bible.data.api.BibleApi;
import com.apps.nicholaspark.bible.data.version.VersionDataSource;
import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.data.vo.VersionResponse;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/18/16.
 */

public class VersionRemoteDataSource implements VersionDataSource {

  private final BibleApi bibleApi;

  @Inject
  public VersionRemoteDataSource(BibleApi bibleApi) {
    this.bibleApi = bibleApi;
  }

  @Override
  public Observable<VersionResponse> getVersions() {
    return bibleApi.getVersions();
  }

  @Override
  public void deleteVersionPreference() {
    // Handled in concrete implementation
  }

  @Override
  public void saveVersion(Version version) {
    // Handled in concrete implementation
  }

  @Override
  public void refresh() {
    // Handled in concrete implementation
  }
}
