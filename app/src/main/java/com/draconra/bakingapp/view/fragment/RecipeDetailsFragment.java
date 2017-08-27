package com.draconra.bakingapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.draconra.bakingapp.R;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.model.Step;
import com.draconra.bakingapp.view.adapter.StepAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailsFragment extends Fragment implements StepAdapter.ListItemClickListener {

    @BindView(R.id.recipeIngredients)
    TextView recipeIngredients;
    @BindView(R.id.ingredientCardView)
    CardView ingredientCardView;
    @BindView(R.id.stepsRV)
    RecyclerView stepsRecyclerView;
    Unbinder unbinder;

    private StepAdapter stepsAdapter;
    private List<Step> stepsList;
    private Recipe recipe;

    RecipeClickListener callback;

    public RecipeDetailsFragment() {

    }

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Intent.EXTRA_TEXT, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Intent.EXTRA_TEXT);
            stepsList = recipe.getSteps();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        unbinder = ButterKnife.bind(this, view);

        ingredientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onIngredientsSelected(v, recipe);
                }
            }
        });

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepAdapter(stepsList, this);
        stepsRecyclerView.setAdapter(stepsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        stepsRecyclerView.addItemDecoration(itemDecoration);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeClickListener) {
            callback = (RecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement RecipeClickListener");
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        callback.onStepSelected(clickedItemIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface RecipeClickListener {
        void onIngredientsSelected(View v, Recipe recipe);

        void onStepSelected(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}