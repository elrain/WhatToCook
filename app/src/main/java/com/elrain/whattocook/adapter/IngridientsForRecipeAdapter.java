package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dao.Ingridient;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class IngridientsForRecipeAdapter extends BaseAdapter {

    private List<Ingridient> mIngridients;
    private LayoutInflater mInflater;

    public IngridientsForRecipeAdapter(Context context, List<Ingridient> mIngridients) {
        this.mIngridients = mIngridients;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mIngridients.size();
    }

    @Override
    public Ingridient getItem(int position) {
        return mIngridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView)
            convertView = mInflater.inflate(R.layout.ingridient_for_recipe_view, null);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(getItem(position).getIngridientName());
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        int amount = getItem(position).getAmount();
        if (amount == 0) tvAmount.setText("");
        else tvAmount.setText(String.valueOf(amount));
        TextView tvAmountType = (TextView) convertView.findViewById(R.id.tvAmountType);
        tvAmountType.setText(getItem(position).getAmountTypeName());
        return convertView;
    }
}
