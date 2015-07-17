package com.elrain.whattocook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 16.06.15.
 */
public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        EventBus.getDefault().register(this);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (RecipeHelper.getRecipeCount(DbHelper.getInstance(SplashScreenActivity.this).getReadableDatabase()) == 0)
                    loadData();
                else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    SplashScreenActivity.this.finish();
                }
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadData() {
        if (NetworkUtil.isNetworkOnline(SplashScreenActivity.this)) {
            ApiWorker.getInstance(SplashScreenActivity.this).initData();
        } else {
            DialogGetter.noInternetDialog(SplashScreenActivity.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                }
            }).show();
        }
    }

    public void onEventMainThread(CommonMessage message) {
        switch (message.mMessageEvent) {
            case DATA_LOAD_FINISHED:
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                SplashScreenActivity.this.finish();
                break;
            case DATA_LOAD_FAIL:
                break;
        }
    }
}
