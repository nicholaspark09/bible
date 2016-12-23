package com.apps.nicholaspark.bible.data.version;

import com.apps.nicholaspark.bible.data.vo.Version;
import com.apps.nicholaspark.bible.data.vo.VersionResponse;

import io.reactivex.Observable;

/**
 * Created by nicholaspark on 12/18/16.
 */

public interface VersionDataSource {

  Observable<VersionResponse> getVersions();
  void deleteVersionPreference();
  void saveVersion(Version version);
  void refresh();
}
