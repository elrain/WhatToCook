package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.util.ImageUtil;

import java.util.List;

/**
 * Created by elrain on 11.08.15.
 */
public class SavedRecipesAdapter extends BaseAdapter {

    private List<Recipe> mRecipes;
    private LayoutInflater mInflater;
    private Context mContext;

    public SavedRecipesAdapter(Context context, List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return mRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mRecipes.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.recipe_title_view, parent, false);
            holder = new ViewHolder();
            holder.ivHolder = (ImageView) convertView.findViewById(R.id.ivHolder);
            holder.ivAction = (ImageView) convertView.findViewById(R.id.ivAction);
            holder.ivAction.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            holder.ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeHelper.saveUnsaveRecipe(DbHelper.getInstance(mContext).getWritableDatabase(), getItemId(position), false);
                    mRecipes.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.tvCookTime = (TextView) convertView.findViewById(R.id.tvCookTime);
            holder.tvDishType = (TextView) convertView.findViewById(R.id.tvDishType);
            holder.tvKitchenType = (TextView) convertView.findViewById(R.id.tvKitchenType);
            holder.tvRecipeName = (TextView) convertView.findViewById(R.id.tvRecipeName);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.tvRecipeName.setText(getItem(position).getName());
        holder.tvDishType.setText(getItem(position).getDishTypeName());
        int cookTime = getItem(position).getCookTime();
        if (cookTime > 0) {
            holder.tvCookTime.setText(cookTime + "минут");
            holder.tvCookTime.setVisibility(View.VISIBLE);
        } else holder.tvCookTime.setVisibility(View.GONE);
        holder.tvKitchenType.setText(getItem(position).getKitchenTypeName());
        holder.ivHolder.setImageDrawable(ImageUtil.getDrawableFromPath(getItem(position).getImage()));
        return convertView;
    }

    private static class ViewHolder {
        public ImageView ivHolder;
        public ImageView ivAction;
        public TextView tvKitchenType;
        public TextView tvDishType;
        public TextView tvRecipeName;
        public TextView tvCookTime;
    }
}
