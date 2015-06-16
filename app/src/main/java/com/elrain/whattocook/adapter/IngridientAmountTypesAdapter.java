package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dao.RecipeIngridientsEntity;

import java.util.List;

/**
 * Created by elrain on 15.06.15.
 */
public class IngridientAmountTypesAdapter extends BaseAdapter {

    private final List<RecipeIngridientsEntity> mIngridients;
    private final LayoutInflater mInflater;

    public IngridientAmountTypesAdapter(Context context, List<RecipeIngridientsEntity> mIngridients) {
        this.mIngridients = mIngridients;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mIngridients.size();
    }

    @Override
    public RecipeIngridientsEntity getItem(int position) {
        return mIngridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.ingridient_for_recipe_info_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvQuantityAndType = (TextView) convertView.findViewById(R.id.tvQuantityAndType);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvName.setText(getItem(position).getName());
        int quantity = getItem(position).getQuantity();
        viewHolder.tvQuantityAndType.setText(quantity == 0 ? getItem(position).getAmountTypeName() :
                quantity + " " + getItem(position).getAmountTypeName());

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvName;
        public TextView tvQuantityAndType;
    }


}
