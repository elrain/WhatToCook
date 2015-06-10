package com.elrain.whattocook.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.adapter.NamedAdapter;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.dao.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elrain on 10.06.15.
 */
public class RecipeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvItems = (ListView) view.findViewById(R.id.lvItems);
        RecipeHelper helper = new RecipeHelper(getActivity());
        List<Recipe> recipes = helper.getAllRecipes();
        List<NamedEntity> namedEntities = new ArrayList<>();
        for (Recipe r : recipes)
            namedEntities.add(new NamedEntity(r.getId(), r.getName()));
        NamedAdapter adapter = new NamedAdapter(getActivity(), namedEntities);
        lvItems.setAdapter(adapter);
    }
}
