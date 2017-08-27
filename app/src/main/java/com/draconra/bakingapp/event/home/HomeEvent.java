package com.draconra.bakingapp.event.home;

import com.draconra.bakingapp.event.BaseEvent;
import com.draconra.bakingapp.model.Recipe;

import java.util.List;

public class HomeEvent extends BaseEvent {

    private List<Recipe> recipeObjects;

    public HomeEvent(boolean isSuccess, List<Recipe> results) {
        this.isSuccess = isSuccess;
        this.recipeObjects = results;
    }

    public HomeEvent(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public List<Recipe> getRecipeObjects() {
        return recipeObjects;
    }
}
