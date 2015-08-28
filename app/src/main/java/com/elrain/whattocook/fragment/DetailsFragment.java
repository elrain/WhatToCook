package com.elrain.whattocook.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.ExpandRecipeAdapter;
import com.elrain.whattocook.adapter.IngridientAmountTypesAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.webutil.rest.api.Constants;

/**
 * Created by elrain on 15.06.15.
 */
public class DetailsFragment extends Fragment {

    public static final String MINUTES = " мин";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_details, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Recipe r = RecipeHelper.getRecipe(DbHelper.getInstance(getActivity()).getReadableDatabase(), getArguments().getLong(ExpandRecipeAdapter.ID_RECIPE));
        final LinearLayout llInfo = (LinearLayout) view.findViewById(R.id.llInfo);
        llInfo.animate().setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (llInfo.getVisibility() == View.GONE)
                    llInfo.setVisibility(View.VISIBLE);
                else llInfo.setVisibility(View.GONE);
            }
        });

        TextView tvKitchenType = (TextView) view.findViewById(R.id.tvKitchenType);
        tvKitchenType.setText(r.getKitchenTypeName());

        TextView tvDishType = (TextView) view.findViewById(R.id.tvDishType);
        tvDishType.setText(r.getDishTypeName());

        TextView tvCookTime = (TextView) view.findViewById(R.id.tvCookTime);
        int cookTime = r.getCookTime();
        if (cookTime == 0)
            tvCookTime.setVisibility(View.GONE);
        else
            tvCookTime.setText(cookTime + MINUTES);

        final TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(r.getName());
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llInfo.getVisibility() == View.GONE) {
                    llInfo.animate().alpha(1.0f);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                } else {
                    llInfo.animate().alpha(0.0f).setDuration(500);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }
            }
        });

        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvDescription.setText(r.getDescription());

        ImageView ivHolder = (ImageView) view.findViewById(R.id.ivHolder);

        Glide.with(getActivity()).load(Constants.IMAGE_URL + r.getId())
                .skipMemoryCache(false).dontTransform().placeholder(R.drawable.ic_launcher).diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_splash_screen).into(ivHolder);

        ListView lvItems = (ListView) view.findViewById(R.id.lvIngridients);
        IngridientAmountTypesAdapter adapter = new IngridientAmountTypesAdapter(getActivity(), r.getIngridients());
        lvItems.setAdapter(adapter);
    }
}