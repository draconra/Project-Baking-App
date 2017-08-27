package com.draconra.bakingapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.draconra.bakingapp.R;
import com.draconra.bakingapp.model.Ingredient;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.view.adapter.IngredientAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IngredientsFragment extends Fragment {

    @BindView(R.id.ingredientsRV)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private Recipe recipe;
    private List<Ingredient> ingredients;
    private IngredientAdapter ingredientsAdapter;
    private static final String SAVED_LAYOUT_MANAGER = "classname.recycler.layout";


    public IngredientsFragment() {

    }

    public static IngredientsFragment newInstance(Recipe recipe) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Intent.EXTRA_TEXT, recipe);
        ingredientsFragment.setArguments(args);
        return ingredientsFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        outstate.putInt(SAVED_LAYOUT_MANAGER, scrollPosition);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Intent.EXTRA_TEXT);
            ingredients = recipe.getIngredients();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            int scrollPos = savedInstanceState.getInt(SAVED_LAYOUT_MANAGER);
            recyclerView.scrollToPosition(scrollPos);
        }

        ingredientsAdapter = new IngredientAdapter(ingredients);
        recyclerView.setAdapter(ingredientsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
