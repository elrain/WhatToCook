package com.elrain.whattocook.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.adapter.NamedAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.DishTypeHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.fragment.CommentsFragment;
import com.elrain.whattocook.fragment.DetailsFragment;
import com.elrain.whattocook.fragment.RecipeFragment;
import com.elrain.whattocook.fragment.SelectFragment;
import com.elrain.whattocook.message.ChangeFragmentMessage;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.Preferences;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {
    public static final String RECIPES = "recipes";
    public static final String ADDING_INGRIDIENTS = "addingIngridients";
    public static final String DETAILS_INFO = "detailsInfo";
    public static final String COMMENTS = "comments";

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
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

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
        initButtons();

        initFragmentMap();

        changeFragment(RECIPES, null);
    }

    private void initButtons() {
        Button btnOpenRecipes = (Button) findViewById(R.id.btnOpenRecipes);
        btnOpenRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(RECIPES, null);
            }
        });

        Button btnOpenMyIngridients = (Button) findViewById(R.id.btnOpenMyIngridients);
        btnOpenMyIngridients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(ADDING_INGRIDIENTS, null);
            }
        });
    }

    private void initFragmentMap() {
        mFragmentMap = new HashMap<>();
        mFragmentMap.put(ADDING_INGRIDIENTS, new SelectFragment());
        mFragmentMap.put(RECIPES, new RecipeFragment());
        mFragmentMap.put(DETAILS_INFO, new DetailsFragment());
        mFragmentMap.put(COMMENTS, new CommentsFragment());
    }

    private void initSpinners() {
        Spinner spKitchen = (Spinner) findViewById(R.id.spKitchens);
        List<NamedEntity> kitchens = KitchenTypeHelper.getAll(DbHelper.getInstance(this).getReadableDatabase(), this);
        NamedAdapter kitchensAdapter = new NamedAdapter(this, kitchens);
        spKitchen.setAdapter(kitchensAdapter);
        spKitchen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    Preferences.getInstance(MainActivity.this).setKitchenTypeId(0);
                else
                    Preferences.getInstance(MainActivity.this).setKitchenTypeId(id);
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.FILTER_CHANGED));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spDishTypes = (Spinner) findViewById(R.id.spDishTypes);
        List<NamedEntity> dishTypes = DishTypeHelper.getAll(DbHelper.getInstance(this).getReadableDatabase(), this);
        NamedAdapter dishTypesAdapter = new NamedAdapter(this, dishTypes);
        spDishTypes.setAdapter(dishTypesAdapter);
        spDishTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    Preferences.getInstance(MainActivity.this).setDishTypeId(0);
                else
                    Preferences.getInstance(MainActivity.this).setDishTypeId(id);
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.FILTER_CHANGED));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                changeFragment(ADDING_INGRIDIENTS, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(@NonNull String tag, Bundle bundle) {
        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.content_frame);
        Fragment newFragment = mFragmentMap.get(tag);
        if (null != bundle) {
            if (null == newFragment.getArguments()) {
                newFragment.setArguments(bundle);
            } else
                newFragment.getArguments().putAll(bundle);
        }
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

    public void onEventMainThread(ChangeFragmentMessage message) {
        changeFragment(message.mFragmentTag, message.mBundle);
    }

    @Override
    public void onBackPressed() {
        if (mFragmentMap.get(mLastTag) instanceof DetailsFragment
                || mFragmentMap.get(mLastTag) instanceof CommentsFragment)
            changeFragment(RECIPES, null);
        else if (mFragmentMap.get(mLastTag) instanceof RecipeFragment) {
            if (Preferences.getInstance(MainActivity.this).getUserType() != 3) {
                DialogGetter.logoutDilog(MainActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        MainActivity.this.finish();
                    }
                }).show();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }
        } else if (mFragmentMap.get(mLastTag) instanceof SelectFragment)
            changeFragment(RECIPES, null);
    }
}
