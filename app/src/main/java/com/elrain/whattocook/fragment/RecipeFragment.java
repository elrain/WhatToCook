package com.elrain.whattocook.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.ExpandRecipeAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.Preferences;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 10.06.15.
 */
public class RecipeFragment extends Fragment {

    private ExpandableListView mElvItems;
    private ExpandRecipeAdapter expandRecipeAdapter;
    private Bundle mArguments;
    private Button btnShowAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mElvItems = (ExpandableListView) view.findViewById(R.id.elvItems);
        btnShowAll = (Button) view.findViewById(R.id.btnShowAll);
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArguments = null;
                addData();
                v.setVisibility(View.GONE);
            }
        });
        if (null == mArguments) btnShowAll.setVisibility(View.GONE);
        else btnShowAll.setVisibility(View.VISIBLE);
        addData();
    }

    private void addData() {
        if (null == mArguments) {
            expandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
        } else {
            expandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getPossibleRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            mArguments.getLongArray(SelectFragment.INGRIDIENTS)));
        }
        mElvItems.setAdapter(expandRecipeAdapter);
    }


    public void onEventMainThread(CommonMessage message) {
        if (message.mMessageEvent == CommonMessage.MessageEvent.FILTER_CHANGED) {
            expandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
            mElvItems.setAdapter(expandRecipeAdapter);
        }
    }

    @Override
    public void setArguments(Bundle args) {
        mArguments = args;
    }
}
