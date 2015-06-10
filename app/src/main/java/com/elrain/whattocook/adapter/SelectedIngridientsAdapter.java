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
import com.elrain.whattocook.dao.SelectedIngridientsEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectedIngridientsAdapter extends BaseAdapter {
    private final List<SelectedIngridientsEntity> mSelectedIngridients;
    private final LayoutInflater mInflater;
    private final Context mContext;

    public SelectedIngridientsAdapter(Context context, List<SelectedIngridientsEntity> mSelectedIngridients) {
        this.mSelectedIngridients = mSelectedIngridients;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addIngridient(SelectedIngridientsEntity ingridient) {
        boolean isExists = false;
        for (SelectedIngridientsEntity sie : mSelectedIngridients) {
            if (sie.getIngridientsEntity().getId() == ingridient.getIngridientsEntity().getId() &&
                    sie.getIdAmountType() == ingridient.getIdAmountType()) {
                sie.setQuantity(sie.getQuantity() + ingridient.getQuantity());
                isExists = true;
                break;
            }
        }
        if (!isExists)
            mSelectedIngridients.add(ingridient);
        notifyDataSetChanged();
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
        viewHolder.etQuantity.setText(String.valueOf(getItem(position).getQuantity()));
        viewHolder.tvName.setText(getItem(position).getIngridientsEntity().getName());

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
        public EditText etQuantity;
        public TextView tvAmountType;
    }
}
