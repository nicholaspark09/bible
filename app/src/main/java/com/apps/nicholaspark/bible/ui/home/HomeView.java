package com.apps.nicholaspark.bible.ui.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.nicholaspark.bible.AppComponent;
import com.apps.nicholaspark.bible.MainActivity;
import com.apps.nicholaspark.bible.R;
import com.apps.nicholaspark.bible.data.vo.Verse;
import com.apps.nicholaspark.bible.di.ApplicationScope;
import com.apps.nicholaspark.bible.ui.ControllerResult;
import com.apps.nicholaspark.bible.ui.base.BaseController;
import com.apps.nicholaspark.bible.util.BundleBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nicholaspark on 11/30/16.
 */
public final class HomeView extends BaseController implements HomeContract.View {
  private static final String CHAPTER_ID = "key.chapter_id";
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progress_bar) ProgressBar progressBar;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @Inject HomePresenter presenter;
  String chapterId;
  private VersesRecyclerAdapter adapter;

  public HomeView(String chapterId) {
    this(new BundleBuilder(new Bundle())
    .putString(CHAPTER_ID, chapterId)
    .build());
  }

  public HomeView(Bundle args) {
    super(args);
    chapterId = args.getString(CHAPTER_ID);
  }

  @Override
  protected int layoutId() {
    return R.layout.home_view;
  }

  @Override
  protected void onViewBound(View view) {
    super.onViewBound(view);
    adapter = new VersesRecyclerAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected void onAttach(@NonNull View view) {
    super.onAttach(view);
    setActionBar(toolbar);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle("The Bible");
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.show();
    setHasOptionsMenu(true);
    presenter.setView(this);
    presenter.subscribe();
    presenter.init(chapterId);
  }

  @Override
  protected void onDetach(@NonNull View view) {
    presenter.unsubscribe();
    super.onDetach(view);
  }

  @Override
  public void onResult(ControllerResult result) {

  }

  @Override
  public void showIntroUi() {

  }

  @Override
  public void showTitle(String title) {
    getActionBar().setTitle(title);
  }

  @Override
  public void setLoadingIndicator(boolean isLoading) {
    if(isLoading) {
      progressBar.setVisibility(View.VISIBLE);
    }else {
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void showError(String error) {
    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
  }

  @Override
  public void showVerses(List<Verse> verses) {
    adapter.setVerses(verses);
  }

  @Override
  protected void inject(AppComponent component) {
    component.inject(this);
  }

  public class VersesRecyclerAdapter extends RecyclerView.Adapter<VersesRecyclerAdapter.ViewHolder> {

    private List<Verse> verses;

    public VersesRecyclerAdapter() {
      verses = new ArrayList<>();
    }

    public void setVerses(List<Verse> verses) {
      this.verses = verses;
      notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verse_row, parent, false);
      ViewHolder vh = new ViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Verse verse = verses.get(position);
      holder.titleTextView.setText(Html.fromHtml(verse.text()));
    }

    @Override
    public int getItemCount() {
      return verses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.verse_text_view)
      TextView titleTextView;

      public ViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
      }
    }
  }

  public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
      this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      outRect.bottom = verticalSpaceHeight;
    }
  }
}
