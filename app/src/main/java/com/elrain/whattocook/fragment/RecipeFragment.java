package com.elrain.whattocook.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.activity.helper.ProgressBarDialogBuilder;
import com.elrain.whattocook.adapter.ExpandRecipeAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.util.Preferences;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.api.Constants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 10.06.15.
 */
public class RecipeFragment extends Fragment {

    public static final String FILE_SAVED_TO = "File saved to ";
    private ExpandableListView mElvItems;
    private ExpandRecipeAdapter mExpandRecipeAdapter;
    private static Bundle mArguments;
    private ProgressDialog mProgressDialog;

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
        Button btnShowAll = (Button) view.findViewById(R.id.btnShowAll);
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
        mProgressDialog = new ProgressBarDialogBuilder(getActivity(), getActivity().getString(R.string.dialog_message_getting_recipes)).build();
        addData();
    }

    private void addData() {
        mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(), new ArrayList<Recipe>());
        if (null == mArguments) {
            mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
            mElvItems.setAdapter(mExpandRecipeAdapter);
        } else {
            final List<Recipe> recipes = RecipeHelper.getPossibleRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                    mArguments.getLongArray(SelectFragment.INGRIDIENTS));
            DialogGetter.recipesFoundDialog(getActivity(), recipes.size(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            lookForRecipes();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            mProgressDialog.show();
                            mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(), recipes);
                            mElvItems.setAdapter(mExpandRecipeAdapter);
                            dialog.dismiss();
                            break;
                    }
                }
            }).show();
        }
    }

    private void lookForRecipes() {
        if (NetworkUtil.isNetworkOnline(getActivity())) {
            mProgressDialog.show();
            ApiWorker.getInstance(getActivity()).getRecipesByIngridients(mArguments.getStringArrayList(SelectFragment.INGRIDIENTS_NAMES).toArray(new String[1]));
        } else {
            DialogGetter.noInternetDialogSecond(getActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            lookForRecipes();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            final List<Recipe> recipes = RecipeHelper.getPossibleRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                                    mArguments.getLongArray(SelectFragment.INGRIDIENTS));
                            if (recipes.size() == 0) {
                                mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                                        RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                                                Preferences.getInstance(getActivity())));
                                mElvItems.setAdapter(mExpandRecipeAdapter);
                            }
                            break;
                    }
                }
            }).show();
        }
    }

    public void onEventMainThread(CommonMessage message) {
        if (message.mMessageEvent == CommonMessage.MessageEvent.FILTER_CHANGED) {
            mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getAllRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            Preferences.getInstance(getActivity())));
            mElvItems.setAdapter(mExpandRecipeAdapter);
        } else if (message.mMessageEvent == CommonMessage.MessageEvent.NEW_RESIPES_ADDED) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mExpandRecipeAdapter = new ExpandRecipeAdapter(getActivity(),
                    RecipeHelper.getPossibleRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase(),
                            mArguments.getLongArray(SelectFragment.INGRIDIENTS)));
            mElvItems.setAdapter(mExpandRecipeAdapter);
        } else if(message.mMessageEvent == CommonMessage.MessageEvent.PDF_DOWNLOADED){
            String fileName = (String) message.messageObject;
            mExpandRecipeAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), FILE_SAVED_TO + String.format(Constants.PDF_LOCATION, fileName), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setArguments(Bundle args) {
        mArguments = args;
    }
}
