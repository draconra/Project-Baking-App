package com.draconra.bakingapp.api;

import com.draconra.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by draconra on 8/11/17.
 */

public interface ApiInterface {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
