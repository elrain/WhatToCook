package com.elrain.whattocook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.IngridientsForRecipeAdapter;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.Recipe;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class RecipeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RecipeHelper recipeHelper = new RecipeHelper(this);
        Recipe recipe = recipeHelper.getRecipe(1);

        TextView tvKitchen = (TextView) findViewById(R.id.tvKitchen);
        TextView tvType = (TextView) findViewById(R.id.tvType);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);

        tvKitchen.setText(recipe.getKitchenTypeName());
        tvType.setText(recipe.getDishTypeName());
        tvDescription.setText(recipe.getDescription());

        ListView lvIngridietnts = (ListView) findViewById(R.id.lvIngridients);
        IngridientsForRecipeAdapter adapter = new IngridientsForRecipeAdapter(this, recipe.getIngridients());
        lvIngridietnts.setAdapter(adapter);
    }
}
