package com.draconra.bakingapp.util.helper;

import android.content.Context;
import android.content.Intent;

import com.draconra.bakingapp.model.Recipe;
import com.draconra.bakingapp.view.RecipeActivity;
import com.draconra.bakingapp.view.SettingsPrefActivity;

/**
 * Created by draconra on 8/26/17.
 */

public class RedirectHelper {

    public static void goToRecipeDetail(Context context, Recipe recipes){
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, recipes);
        context.startActivity(intent);
    }

    public static void goToSettingPref(Context context){
        Intent intent = new Intent(context, SettingsPrefActivity.class);
        context.startActivity(intent);
    }

}
