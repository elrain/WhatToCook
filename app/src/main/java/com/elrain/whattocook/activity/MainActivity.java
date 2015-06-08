package com.elrain.whattocook.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.adapter.IngridientsAdapter;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.webutil.rest.ApiWorker;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        RecipeHelper recipeHelper = new RecipeHelper(this);
        if (recipeHelper.getRecipeCount() <= 0) {
            DialogGetter.initDataNeededDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                }
            }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadData() {
        if (NetworkUtil.isNetworkOnline(MainActivity.this)) {
            ApiWorker.getInstance(MainActivity.this).initData();
        } else {
            DialogGetter.NoInternetDialog(MainActivity.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                }
            }).show();
        }
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
        switch (id) {
            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(CommonMessage message) {
        switch (message.mMessageEvent) {
            case DATA_LOAD_FINISHED:
            default:
                ListView lv = (ListView) findViewById(R.id.lvItems);
                KitchenTypeHelper ingridientsHelper = new KitchenTypeHelper(this);
                IngridientsAdapter adapter = new IngridientsAdapter(this, ingridientsHelper.getAll());
                lv.setAdapter(adapter);
                break;
        }
    }
}
