package com.elrain.whattocook.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.adapter.NamedAdapter;
import com.elrain.whattocook.dal.helper.DishTypeHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.fragment.RecipeFragment;
import com.elrain.whattocook.fragment.SelectFragment;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.webutil.rest.ApiWorker;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {
    private static final String RECIPES = "recipes";
    private static final String ADDING_INGRIDIENTS = "addingIngridients";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager mFragmentManager;
    private HashMap<String, Fragment> mFragmentMap;
    private String mLastTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFragmentManager = getFragmentManager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        EventBus.getDefault().register(this);

        initSpinners();

        RecipeHelper recipeHelper = new RecipeHelper(this);
        if (recipeHelper.getRecipeCount() <= 0) {
            DialogGetter.initDataNeededDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                }
            }).show();
        }
        initFragmentMap();
        changeFragment(RECIPES);
    }

    private void initFragmentMap() {
        mFragmentMap = new HashMap<>();
        mFragmentMap.put(ADDING_INGRIDIENTS, new SelectFragment());
        mFragmentMap.put(RECIPES, new RecipeFragment());
    }

    private void initSpinners() {
        Spinner spKitchen = (Spinner) findViewById(R.id.spKitchens);
        KitchenTypeHelper kitchenTypeHelper = new KitchenTypeHelper(this);
        List<NamedEntity> kitchens = kitchenTypeHelper.getAll();
        NamedAdapter kitchensAdapter = new NamedAdapter(this, kitchens);
        spKitchen.setAdapter(kitchensAdapter);

        Spinner spDishTypes = (Spinner) findViewById(R.id.spDishTypes);
        DishTypeHelper dishTypeHelper = new DishTypeHelper(this);
        List<NamedEntity> dishTypes = dishTypeHelper.getAll();
        NamedAdapter dishTypesAdapter = new NamedAdapter(this, dishTypes);
        spDishTypes.setAdapter(dishTypesAdapter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                changeFragment(ADDING_INGRIDIENTS);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(String tag) {
        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.content_frame);
        Fragment newFragment = mFragmentMap.get(tag);
        if (newFragment != currentFragment) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (null != newFragment) {
                if (null != currentFragment)
                    ft.detach(currentFragment);
                if (null == mFragmentManager.findFragmentByTag(tag))
                    ft.add(R.id.content_frame, newFragment, tag);
                else
                    ft.attach(mFragmentManager.findFragmentByTag(tag));
            }
            mLastTag = tag;
            ft.commit();
        }
    }

    public void onEvent(CommonMessage message) {
        switch (message.mMessageEvent) {
            case DATA_LOAD_FINISHED:
                initSpinners();
                break;
        }
    }
}
