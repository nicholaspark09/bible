package com.apps.nicholaspark.bible.data.version;

import com.apps.nicholaspark.bible.data.qualifier.Remote;
import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.data.vo.VersionResponse;
import com.apps.nicholaspark.bible.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/18/16.
 */
@ApplicationScope
public final class VersionRepository implements VersionDataSource {

  private VersionDataSource networkLayer;
  VersionResponse versionResponse;
  boolean cacheIsDirty = false;

  @Inject
  public VersionRepository(@Remote VersionDataSource networkLayer) {
    this.networkLayer = networkLayer;
  }

  @Override
  public Observable<VersionResponse> getVersions() {
    if (versionResponse != null && !cacheIsDirty)
      return Observable.just(versionResponse);
    return networkLayer.getVersions()
            .doOnNext(versionResponse -> {
              cacheIsDirty = false;
              this.versionResponse = versionResponse;
            });
  }

  @Override
  public void deleteVersionPreference() {

  }

  @Override
  public void saveVersion(Version version) {

  }

  @Override
  public void refresh() {

  }
}
