package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dao.NamedObject;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class IngridientsAdapter extends BaseAdapter {

    private List<NamedObject> mIngridients;
    private LayoutInflater mInflater;

    public IngridientsAdapter(Context context, List<NamedObject> ingridients) {
        mInflater = LayoutInflater.from(context);
        mIngridients = ingridients;
    }

    @Override
    public int getCount() {
        return mIngridients.size();
    }

    @Override
    public NamedObject getItem(int position) {
        return mIngridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mIngridients.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null == convertView)
            convertView = mInflater.inflate(R.layout.ingridient_view, null);

        ((TextView)convertView.findViewById(R.id.tvName)).setText(getItem(position).getName());
        return convertView;
    }
}
