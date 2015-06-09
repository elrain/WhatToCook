package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectIngridientAdapter extends BaseAdapter {

    private final List<SelectedIngridientsEntity> mSelectedIngridients;
    private final LayoutInflater mInflater;
    private final Context mContext;

    public SelectIngridientAdapter(Context context, List<SelectedIngridientsEntity> mSelectedIngridients) {
        this.mSelectedIngridients = mSelectedIngridients;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mSelectedIngridients.size();
    }

    @Override
    public SelectedIngridientsEntity getItem(int position) {
        return mSelectedIngridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSelectedIngridients.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.select_ingridients_view, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.etQuantity = (EditText) convertView.findViewById(R.id.etQuantity);
            viewHolder.tvAmountType = (TextView) convertView.findViewById(R.id.tvAmountType);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();
        AmountTypeHelper amountTypeHelper = new AmountTypeHelper(mContext);
        viewHolder.tvAmountType.setText(amountTypeHelper.getTypeName(getItem(position).getIdAmountType()));
        int quantity = getItem(position).getQuantity();
        if (quantity == 0) {
            viewHolder.etQuantity.setVisibility(View.INVISIBLE);
            viewHolder.tvAmountType.setVisibility(View.INVISIBLE);
        } else{
            viewHolder.etQuantity.setVisibility(View.VISIBLE);
            viewHolder.tvAmountType.setVisibility(View.VISIBLE);
            viewHolder.etQuantity.setText(String.valueOf(quantity));
            IngridientsHelper ingridientsHelper = new IngridientsHelper(mContext);
            viewHolder.tvName.setText(ingridientsHelper.getName(getItem(position).getIdIngridient()));
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
        public EditText etQuantity;
        public TextView tvAmountType;
    }
}
