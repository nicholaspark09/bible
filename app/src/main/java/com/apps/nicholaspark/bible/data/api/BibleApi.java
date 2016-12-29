package com.apps.nicholaspark.bible.data.api;

import com.apps.nicholaspark.bible.data.vo.BookResponse;
import com.apps.nicholaspark.bible.data.vo.ChapterResponse;
import com.apps.nicholaspark.bible.data.vo.VerseResponse;
import com.apps.nicholaspark.bible.data.vo.VersionResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nicholaspark on 12/18/16.
 */

public interface BibleApi {
  @GET("versions.js")
  Observable<VersionResponse> getVersions();

  @GET("versions/{version_id}/books.js")
  Observable<BookResponse> loadBooks(@Path("version_id") String versionId);

  @GET("books/{book_id}/chapters.js")
  Observable<ChapterResponse> loadChapters(@Path("book_id") String bookId);

  @GET("chapters/{chapter_id}/verses.js")
  Observable<VerseResponse> loadVerses(@Path("chapter_id") String chapterId);

}
