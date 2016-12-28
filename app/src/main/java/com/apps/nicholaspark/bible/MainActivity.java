package com.apps.nicholaspark.bible;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apps.nicholaspark.bible.data.book.BookRepository;
import com.apps.nicholaspark.bible.data.chapter.ChapterRepository;
import com.apps.nicholaspark.bible.data.version.VersionRepository;
import com.apps.nicholaspark.bible.data.vo.Book;
import com.apps.nicholaspark.bible.data.vo.Chapter;
import com.apps.nicholaspark.bible.di.DaggerService;
import com.apps.nicholaspark.bible.ui.home.HomeView;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActionBarProvider, DrawerToggleProvider {

  @Inject Application app;
  @Inject ViewContainer debugViewContainer;
  @Inject VersionRepository versionRepo;
  @Inject BookRepository bookRepository;
  @Inject ChapterRepository chapterRepository;
  @BindView(R.id.view_container) FrameLayout viewContainer;
  @BindView(R.id.chapter_swipe_layout) SwipeRefreshLayout swipeLayout;
  private Unbinder unbinder;
  private Router router;
  private CompositeDisposable disposables;
  private RecyclerView recyclerView;
  private RecyclerView chapterRecyclerView;
  private NavigationView navigationView;
  private BooksRecyclerAdapter adapter;
  private ChaptersRecyclerAdapter chaptersAdapter;
  ActionBarDrawerToggle toggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LayoutInflater inflater = getLayoutInflater();
    DaggerService.getAppComponent(this).inject(this);

    ViewGroup container = debugViewContainer.forActivity(this);
    inflater.inflate(R.layout.activity_main, container);

    unbinder = ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
      @Override
      public void onDrawerClosed(View drawerView) {
        if (chapterRecyclerView != null) {
          chapterRecyclerView.setVisibility(View.INVISIBLE);
        }
        super.onDrawerClosed(drawerView);
      }
    };
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      recyclerView = (RecyclerView) navigationView.findViewById(R.id.recycler_view);
      chapterRecyclerView = (RecyclerView) navigationView.findViewById(R.id.chapter_recycler_view);
      adapter = new BooksRecyclerAdapter();
      chaptersAdapter = new ChaptersRecyclerAdapter();
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(adapter);
      chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
      chapterRecyclerView.setAdapter(chaptersAdapter);
    }
    navigationView.setNavigationItemSelectedListener(this);

    router = Conductor.attachRouter(this, viewContainer, savedInstanceState);
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(new HomeView()));
    }

    disposables = new CompositeDisposable();




    getBookList();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  protected void onDestroy() {
    Timber.d("Destroying...");
    disposables.clear();
    unbinder.unbind();
    super.onDestroy();
  }

  @Override
  public void setActionBar(Toolbar toolbar) {
    setSupportActionBar(toolbar);
  }

  @Override
  public ActionBarDrawerToggle getToggle() {
    return toggle;
  }

  // Handle responses from BookRepository
  private void handleBookResponses(List<Book> books) {
    adapter.setBooks(books);
  }

  // Handle errors from BookRepository
  private void onError(Throwable throwable) {
    swipeLayout.setRefreshing(false);
  }

  private void handleChapters(List<Chapter> chapters) {
    swipeLayout.setRefreshing(false);
    chaptersAdapter.setChapters(chapters);
    chapterRecyclerView.smoothScrollToPosition(0);
  }

  private void getBookList() {
    Disposable disposable = bookRepository.getBooks("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    // onNext
                    this::handleBookResponses,
                    // onError
                    this::onError
            );
    disposables.add(disposable);
  }

  private void getChapters() {
    swipeLayout.setRefreshing(true);
    Disposable disposable = chapterRepository.
            getChapters("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    // onNext
                    this::handleChapters,
                    // onError
                    this::onError
            );
    disposables.add(disposable);
  }

  // Custom RecyclerView Adapter to handle and display Accounts in the navigation menu
  public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolder> {

    private List<Book> books;

    public BooksRecyclerAdapter() {
      books = new ArrayList<>();
    }

    private void setBooks(List<Book> books) {
      this.books = books;
      notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row, parent, false);
      ViewHolder vh = new ViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Book book = books.get(position);
      holder.titleTextView.setText(book.toString());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          bookRepository.setSelectedBook(book.id());
          chapterRecyclerView.setVisibility(View.VISIBLE);
          List<Chapter> emptyChapters = new ArrayList<Chapter>();
          chaptersAdapter.setChapters(emptyChapters);
          getChapters();
        }
      });
    }

    @Override
    public int getItemCount() {
      return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.book_name_text_view)
      TextView titleTextView;

      public ViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
      }
    }
  }


  // Custom RecyclerView Adapter to handle and display Accounts in the navigation menu
  public class ChaptersRecyclerAdapter extends RecyclerView.Adapter<ChaptersRecyclerAdapter.ViewHolder> {

    private List<Chapter> chapters;

    public ChaptersRecyclerAdapter() {
      chapters = new ArrayList<>();
    }

    private void setChapters(List<Chapter> chapters) {
      this.chapters = chapters;
      notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row, parent, false);
      ViewHolder vh = new ViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Chapter chapter = chapters.get(position);
      holder.titleTextView.setText(chapter.name());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
      });
    }

    @Override
    public int getItemCount() {
      return chapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.book_name_text_view)
      TextView titleTextView;

      public ViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
      }
    }
  }
}
