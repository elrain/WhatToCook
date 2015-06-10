package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dao.IngridientsEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class IngridientAdapter extends BaseAdapter {
    private List<IngridientsEntity> mIngridients;
    private LayoutInflater mInflater;

    public IngridientAdapter(Context context, List<IngridientsEntity> ingridients) {
        mInflater = LayoutInflater.from(context);
        mIngridients = ingridients;
    }

    public void addObjects(List<IngridientsEntity> objects) {
        mIngridients.clear();
        for (IngridientsEntity input : objects) {
            boolean isExist = false;
            for (IngridientsEntity exists : mIngridients)
                if (input.getId() == exists.getId()) {
                    isExist = true;
                    break;
                }
            if (!isExist)
                mIngridients.add(input);
        }
        notifyDataSetChanged();
    }

    public void addObject(IngridientsEntity object) {
        int size = mIngridients.size();
        boolean isExist = false;
        for (IngridientsEntity exists : mIngridients)
            if (object.getId() == exists.getId()) {
                isExist = true;
                break;
            }
        if (!isExist)
            mIngridients.add(object);
        if (size != mIngridients.size())
            notifyDataSetChanged();
    }

    public void clearList() {
        mIngridients.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mIngridients.size();
    }

    @Override
    public IngridientsEntity getItem(int position) {
        return mIngridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mIngridients.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView)
            convertView = mInflater.inflate(R.layout.named_view, null);

        ((TextView) convertView.findViewById(R.id.tvName)).setText(getItem(position).getName());
        return convertView;
    }
}
