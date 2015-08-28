package com.elrain.whattocook.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.MainActivity;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.dao.RecipeIngridientsEntity;
import com.elrain.whattocook.fragment.CommentsFragment;
import com.elrain.whattocook.message.ChangeFragmentMessage;
import com.elrain.whattocook.util.ImageUtil;
import com.elrain.whattocook.util.PdfLoader;
import com.elrain.whattocook.webutil.rest.api.Constants;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 12.06.15.
 */
public class ExpandRecipeAdapter extends BaseExpandableListAdapter {

    public static final String ID_RECIPE = "ID";
    private final List<Recipe> mRecipes;
    private final LayoutInflater mInflater;
    private final Context mContext;

    public ExpandRecipeAdapter(Context context, List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return mRecipes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mRecipes.get(groupPosition).getIngridients().size();
    }

    @Override
    public Recipe getGroup(int groupPosition) {
        return mRecipes.get(groupPosition);
    }

    @Override
    public RecipeIngridientsEntity getChild(int groupPosition, int childPosition) {
        return mRecipes.get(groupPosition).getIngridients().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mRecipes.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final TitleViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.recipe_title_view, parent, false);
            viewHolder = new TitleViewHolder();
            viewHolder.ivHolder = (ImageView) convertView.findViewById(R.id.ivHolder);
            viewHolder.tvCookTime = (TextView) convertView.findViewById(R.id.tvCookTime);
            viewHolder.tvDishType = (TextView) convertView.findViewById(R.id.tvDishType);
            viewHolder.tvKitchenType = (TextView) convertView.findViewById(R.id.tvKitchenType);
            viewHolder.tvRecipeName = (TextView) convertView.findViewById(R.id.tvRecipeName);
            viewHolder.ivSave = (ImageView) convertView.findViewById(R.id.ivAction);
            if (getGroup(groupPosition).isSaved()) {
                ImageUtil.setColor(viewHolder.ivSave.getDrawable());
                viewHolder.ivSave.setOnClickListener(null);
            } else {
                viewHolder.ivSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeHelper.saveUnsaveRecipe(DbHelper.getInstance(mContext).getWritableDatabase(), getGroupId(groupPosition), true);
                        getGroup(groupPosition).setIsSaved(true);
                        ImageUtil.setColor(viewHolder.ivSave.getDrawable());
                        viewHolder.ivSave.setOnClickListener(null);
                    }
                });
            }
            viewHolder.ivDowload = (ImageView) convertView.findViewById(R.id.ivDownload);
            final String fileName = getGroup(groupPosition).getName();
            if(new File(String.format(Constants.PDF_LOCATION, fileName)).exists())
                ImageUtil.setColor(viewHolder.ivDowload.getDrawable());
            else
                viewHolder.ivDowload.setBackgroundResource(android.R.drawable.ic_menu_upload);
            viewHolder.ivDowload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PdfLoader.downloadPdf(mContext, getGroup(groupPosition).getId(), fileName);
                }
            });

            convertView.setTag(viewHolder);
        } else viewHolder = (TitleViewHolder) convertView.getTag();

        viewHolder.tvRecipeName.setText(getGroup(groupPosition).getName());
        viewHolder.tvDishType.setText(getGroup(groupPosition).getDishTypeName());
        int cookTime = getGroup(groupPosition).getCookTime();
        if (cookTime > 0) {
            viewHolder.tvCookTime.setText(cookTime + "минут");
            viewHolder.tvCookTime.setVisibility(View.VISIBLE);
        } else viewHolder.tvCookTime.setVisibility(View.GONE);
        viewHolder.tvKitchenType.setText(getGroup(groupPosition).getKitchenTypeName());
        viewHolder.ivHolder.setImageDrawable(ImageUtil.getDrawableFromPath(getGroup(groupPosition).getImage()));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        InfoViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new InfoViewHolder();
            convertView = mInflater.inflate(R.layout.recipe_info_view, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvQuantityAndType = (TextView) convertView.findViewById(R.id.tvQuantityAndType);
            viewHolder.btnShowDetails = (Button) convertView.findViewById(R.id.btnShowDetails);
            viewHolder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ID_RECIPE, getGroupId(groupPosition));
                    EventBus.getDefault().post(new ChangeFragmentMessage(MainActivity.DETAILS_INFO, bundle));
                }
            });
            viewHolder.btnShowComments = (Button) convertView.findViewById(R.id.btnShowComments);
            viewHolder.btnShowComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommentsFragment.RECIPE_ID, getGroupId(groupPosition));
                    EventBus.getDefault().post(new ChangeFragmentMessage(MainActivity.COMMENTS, bundle));
                }
            });
            convertView.setTag(viewHolder);
        } else viewHolder = (InfoViewHolder) convertView.getTag();
        viewHolder.tvName.setText(getChild(groupPosition, childPosition).getName());
        int quantity = getChild(groupPosition, childPosition).getQuantity();
        viewHolder.tvQuantityAndType.setText(quantity == 0 ? getChild(groupPosition, childPosition)
                .getAmountTypeName() : quantity + " " + getChild(groupPosition, childPosition).getAmountTypeName());
        if (childPosition < getChildrenCount(groupPosition) - 1) {
            viewHolder.btnShowDetails.setVisibility(View.GONE);
            viewHolder.btnShowComments.setVisibility(View.GONE);
        } else {
            viewHolder.btnShowDetails.setVisibility(View.VISIBLE);
            viewHolder.btnShowComments.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class TitleViewHolder {
        public ImageView ivHolder;
        public ImageView ivSave;
        public ImageView ivDowload;
        public TextView tvKitchenType;
        public TextView tvDishType;
        public TextView tvRecipeName;
        public TextView tvCookTime;
    }

    private static class InfoViewHolder {
        public TextView tvName;
        public TextView tvQuantityAndType;
        public Button btnShowDetails;
        public Button btnShowComments;
    }
}
