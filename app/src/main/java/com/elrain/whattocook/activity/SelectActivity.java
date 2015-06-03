package com.elrain.whattocook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.adapter.IngridientsAdapter;
import com.elrain.whattocook.dal.helper.IngridientsHelper;


public class SelectActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IngridientsHelper ingridients = new IngridientsHelper(this);
        IngridientsAdapter adapter = new IngridientsAdapter(this, ingridients.getAllIngridients());
        ListView lv = (ListView) findViewById(R.id.lvItems);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogGetter.insertQuantityDialog(SelectActivity.this, id).show();
    }
}
