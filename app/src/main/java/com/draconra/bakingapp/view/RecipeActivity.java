package com.draconra.bakingapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.draconra.bakingapp.R;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.model.Step;
import com.draconra.bakingapp.util.Constant;
import com.draconra.bakingapp.util.helper.RedirectHelper;
import com.draconra.bakingapp.view.core.BaseActivity;
import com.draconra.bakingapp.view.fragment.IngredientsFragment;
import com.draconra.bakingapp.view.fragment.RecipeDetailsFragment;
import com.draconra.bakingapp.view.fragment.VideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends BaseActivity implements RecipeDetailsFragment.RecipeClickListener, VideoFragment.VideoClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipeContainer)
    FrameLayout recipeContainer;

    private Recipe recipe;
    private boolean twoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        getSupportActionBar().setTitle(recipe.getName());

        if (findViewById(R.id.tablet_linear_layout) != null) {
            twoPanes = true; // Double-pane mode
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null) {

                fragmentManager.beginTransaction()
                        .replace(R.id.recipeFragment, new RecipeDetailsFragment().newInstance(recipe))
                        .commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.recipeContainer, new IngredientsFragment().newInstance(recipe))
                        .commit();
            }
        } else {
            twoPanes = false; // Single-pane mode
            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeContainer, new RecipeDetailsFragment().newInstance(recipe))
                        .commit();
        }
    }


    @Override
    public void onIngredientsSelected(View v, Recipe recipe) {

        if (v.getId() == R.id.ingredientCardView) {

            if (twoPanes) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeContainer, new IngredientsFragment().newInstance(recipe))
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeContainer, new IngredientsFragment().newInstance(recipe))
                        .addToBackStack(IngredientsFragment.class.getSimpleName())
                        .commit();
            }
        }

    }

    @Override
    public void onStepSelected(int position) {

        Step step = recipe.getSteps().get(position);
        int length = recipe.getSteps().size();
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        String sDescription = step.getShortDescription();

        bundle.putInt("size", length);
        bundle.putInt("position", position);
        bundle.putString("description", step.getDescription());
        bundle.putString("videoURL", step.getVideoURL());
        bundle.putString("thumbnailUrl", step.getThumbnailURL());


        if (bundle != null) {

            videoFragment.setArguments(bundle);

            if (twoPanes) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeContainer, videoFragment)
                        .commit();
            } else {
                toolbar.setTitle(sDescription);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeContainer, videoFragment)
                        .addToBackStack(VideoFragment.class.getSimpleName())
                        .commit();
            }
        }

    }

    @Override
    public void onNextSelected(int position) {
        onStepSelected(position);
    }

    @Override
    public void onPreviousSelected(int position) {
        onStepSelected(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.settings) {
            RedirectHelper.goToSettingPref(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Recipe recipeContents = recipe;
        outState.putParcelable(Constant.CACHE_RECIPE, recipeContents);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        recipe = savedInstanceState.getParcelable(Constant.CACHE_RECIPE);
    }
}
