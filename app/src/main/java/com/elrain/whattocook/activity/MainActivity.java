package com.elrain.whattocook.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.IngridientsAdapter;
import com.elrain.whattocook.dal.helper.IngridientsHelper;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startActivity(new Intent(this, RecipeActivity.class));
//        setContentView(R.layout.activity_main);

//        IngridientsHelper ingridients = new IngridientsHelper(this);
//        IngridientsAdapter adapter = new IngridientsAdapter(this, ingridients.getAllIngridients());
//        ListView lv = (ListView) findViewById(R.id.lvItems);
//        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_add_new:
                startActivity(new Intent(MainActivity.this, SelectActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }
}
