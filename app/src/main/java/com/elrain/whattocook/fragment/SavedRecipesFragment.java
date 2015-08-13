package com.elrain.whattocook.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.MainActivity;
import com.elrain.whattocook.adapter.ExpandRecipeAdapter;
import com.elrain.whattocook.adapter.SavedRecipesAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.ChangeFragmentMessage;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 11.08.15.
 */
public class SavedRecipesFragment extends Fragment implements AdapterView.OnItemClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_recipes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SavedRecipesAdapter adapter = new SavedRecipesAdapter(getActivity(), RecipeHelper.getSavedRecipes(DbHelper.getInstance(getActivity()).getReadableDatabase()));
        ListView lvSavedRecipes = (ListView) view.findViewById(R.id.lvItems);
        lvSavedRecipes.setAdapter(adapter);
        lvSavedRecipes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(ExpandRecipeAdapter.ID_RECIPE, id);
        EventBus.getDefault().post(new ChangeFragmentMessage(MainActivity.DETAILS_INFO, bundle));
    }
}
