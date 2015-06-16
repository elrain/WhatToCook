package com.elrain.whattocook.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.ExpandRecipeAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.CurrentSelectedHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.Preferences;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 10.06.15.
 */
public class RecipeFragment extends Fragment {

    private ExpandableListView mElvItems;
    private ExpandRecipeAdapter expandRecipeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mElvItems = (ExpandableListView) view.findViewById(R.id.elvItems);
        addData();
    }

    private void addData() {
        if (null == getArguments()) {
            expandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
            mElvItems.setAdapter(expandRecipeAdapter);
        } else {
        }
    }


    public void onEventMainThread(CommonMessage message) {
//        if (message.mMessageEvent == CommonMessage.MessageEvent.DATA_LOAD_FINISHED) {
//            addData();
         if(message.mMessageEvent == CommonMessage.MessageEvent.FILTER_CHANGED){
            expandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
            mElvItems.setAdapter(expandRecipeAdapter);
        }
    }
}
