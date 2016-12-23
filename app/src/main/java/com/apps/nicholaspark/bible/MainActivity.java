package com.apps.nicholaspark.bible;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import com.apps.nicholaspark.bible.data.version.VersionRepository;
import com.apps.nicholaspark.bible.data.vo.VersionResponse;
import com.apps.nicholaspark.bible.di.DaggerService;
import com.apps.nicholaspark.bible.ui.home.HomeView;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  @Inject
  Application app;
  @Inject
  ViewContainer debugViewContainer;
  @Inject VersionRepository versionRepo;
  @BindView(R.id.view_container)
  FrameLayout viewContainer;
  private Unbinder unbinder;
  private Router router;
  private CompositeDisposable disposables;
  private RecyclerView recyclerView;
  private RecyclerView chapterRecyclerView;
  private NavigationView navigationView;
  private BooksRecyclerAdapter adapter;

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
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      recyclerView = (RecyclerView) navigationView.findViewById(R.id.recycler_view);
      chapterRecyclerView = (RecyclerView) navigationView.findViewById(R.id.chapter_recycler_view);
      adapter = new BooksRecyclerAdapter();
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(adapter);
    }
    navigationView.setNavigationItemSelectedListener(this);

    router = Conductor.attachRouter(this, viewContainer, savedInstanceState);
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(new HomeView()));
    }

    disposables = new CompositeDisposable();


    Disposable disposable = versionRepo.getVersions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    // onNext
                    this::handleVersionResponse,
                    this::handleError
            );
    disposables.add(disposable);
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
    unbinder.unbind();
    disposables.clear();
    super.onDestroy();
  }

  private void handleVersionResponse(VersionResponse response) {
    Timber.d("yout got a response: "+response.toString());
  }

  private void handleError(Throwable throwable) {
    Timber.d("You got an erro "+throwable.getMessage());
  }

  // Custom RecyclerView Adapter to handle and display Accounts in the navigation menu
  public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolder> {

    private String[] books;

    public BooksRecyclerAdapter() {
      books = getResources().getStringArray(R.array.books);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row, parent, false);
      ViewHolder vh = new ViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final String book = books[position];
      holder.titleTextView.setText(book);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
      });
    }

    @Override
    public int getItemCount() {
      return books.length;
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
