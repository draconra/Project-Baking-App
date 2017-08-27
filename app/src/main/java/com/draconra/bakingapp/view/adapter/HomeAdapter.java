package com.draconra.bakingapp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.draconra.bakingapp.R;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.util.helper.ImageHelper;
import com.draconra.bakingapp.util.helper.RedirectHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Recipe> recipeList;
    private ArrayList<Integer> mImages;

    public HomeAdapter(ArrayList<Recipe> arrayList, ArrayList<Integer> images) {
        recipeList = arrayList;
        mImages = images;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,
                parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {

        final Recipe recipes = recipeList.get(position);

        holder.recipeName.setText(recipes.getName());
        if (recipes.getImage() == "") {
            Glide.with(holder.itemView.getContext())
                    .load(mImages.get(position)).into(holder.recipeImage);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(recipes.getImage()).into(holder.recipeImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectHelper.goToRecipeDetail(holder.itemView.getContext(), recipes);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void refresh(ArrayList<Recipe> recipes) {
        if (recipeList != null) {
            recipeList.clear();
        }
        recipeList.addAll(recipes);
        notifyDataSetChanged();
    }


    class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeName;
        private ImageView recipeImage;

        public HomeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
        }

    }

    public List<Recipe> getData() {
        return this.recipeList;
    }

}
