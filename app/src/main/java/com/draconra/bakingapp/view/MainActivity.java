package com.draconra.bakingapp.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.draconra.bakingapp.R;
import com.draconra.bakingapp.controller.ControllerFactory;
import com.draconra.bakingapp.controller.HomeController;
import com.draconra.bakingapp.event.NetworkErrorEvent;
import com.draconra.bakingapp.event.home.HomeEvent;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.util.Constant;
import com.draconra.bakingapp.util.helper.ConnectivityHelper;
import com.draconra.bakingapp.util.helper.RecycleViewTypeHelper;
import com.draconra.bakingapp.view.IdlingResources.SimpleIdlingResource;
import com.draconra.bakingapp.view.adapter.HomeAdapter;
import com.draconra.bakingapp.view.core.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.recipeRecyclerView)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Integer> mImages;
    protected ArrayList<Recipe> recipes = new ArrayList<>();
    protected HomeAdapter homeAdapter;
    private HomeController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.register(this);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        controller = ControllerFactory.homeController();

        mImages = new ArrayList<>();
        mImages.add(R.drawable.nutella);
        mImages.add(R.drawable.brownies);
        mImages.add(R.drawable.cheesecake);
        mImages.add(R.drawable.yellowcake);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecycleViewTypeHelper.setRecyclerViewLayoutManager(this, RecycleViewTypeHelper.LayoutManagerType.LINEAR_LAYOUT_MANAGER, recipeRecyclerView, 0);
        } else {
            RecycleViewTypeHelper.setRecyclerViewLayoutManager(this, RecycleViewTypeHelper.LayoutManagerType.GRID_LAYOUT_MANAGER, recipeRecyclerView, Constant.SPAN_COUNT);
        }

        if (ConnectivityHelper.isNetworkAvailable(this)) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(Constant.CACHE_RECIPE)) {
                    ArrayList<Recipe> recipes = savedInstanceState
                            .getParcelableArrayList(Constant.CACHE_RECIPE);
                    int scrollPos = savedInstanceState.getInt(Constant.CACHE_POSITION);
                    recipeRecyclerView.scrollToPosition(scrollPos);

                    homeAdapter = new HomeAdapter(recipes, mImages);
                    recipeRecyclerView.setAdapter(homeAdapter);
                }
            } else {
                homeAdapter = new HomeAdapter(recipes, mImages);
                recipeRecyclerView.setAdapter(homeAdapter);
                progressBar.setVisibility(View.VISIBLE);

                getIdlingResource();
                loadRecipeData();
            }
        } else {
            Snackbar snackbar = Snackbar.make(recipeRecyclerView, "Check your network connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipeData();
            }
        });
    }

    private void loadRecipeData() {
        controller.getRecipe();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipeList(HomeEvent event) {
        progressBar.setVisibility(View.GONE);
        if (event.isSuccess()) {
            showUIDataView();

            recipes = (ArrayList<Recipe>) event.getRecipeObjects();
            homeAdapter.refresh((ArrayList<Recipe>) event.getRecipeObjects());
        } else {
            Snackbar snackbar = Snackbar.make(recipeRecyclerView, "Check your network connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipeError(NetworkErrorEvent event) {
        progressBar.setVisibility(View.GONE);
        showUIErrorMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadRecipeData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    private void showUIErrorMessage() {
        swipeRefreshLayout.setRefreshing(false);
        recipeRecyclerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showUIDataView() {
        swipeRefreshLayout.setRefreshing(false);
        recipeRecyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int scrollPosition = 0;

        recipes = (ArrayList<Recipe>) homeAdapter.getData();

        outState.putParcelableArrayList(Constant.CACHE_RECIPE, recipes);
        // If a layout manager has already been set, get current scroll position.
        if (recipeRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recipeRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        outState.putInt(Constant.CACHE_POSITION, scrollPosition);
    }

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
