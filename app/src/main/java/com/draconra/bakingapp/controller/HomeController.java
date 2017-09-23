package com.draconra.bakingapp.controller;

import android.util.Log;

import com.draconra.bakingapp.AppController;
import com.draconra.bakingapp.event.NetworkErrorEvent;
import com.draconra.bakingapp.event.home.HomeEvent;
import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.util.network.APIError;
import com.draconra.bakingapp.util.network.ErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeController extends BaseController {

    public void getRecipe() {
        Call<List<Recipe>> recipeResponseCall = AppController.getInstance().getApiService().getRecipes();
        recipeResponseCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    eventBus.post(new HomeEvent(true, response.body()));
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    eventBus.post(new HomeEvent(false, error.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("response", ""+t.getMessage());
                eventBus.post(new NetworkErrorEvent(""));
            }
        });
    }

}
