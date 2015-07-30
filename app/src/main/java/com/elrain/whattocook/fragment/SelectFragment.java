package com.elrain.whattocook.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.MainActivity;
import com.elrain.whattocook.adapter.IngridientAdapter;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.message.ChangeFragmentMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectFragment extends Fragment implements View.OnClickListener {
    public static final String INGRIDIENTS = "ingridients";
    private IngridientAdapter mAddIngridientsAdapter;
    private IngridientAdapter mSelectedIngridientsAdapter;
    private EditText mEtSearch;
    private DbHelper mDbHelper;
    private Set<Long> mSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = DbHelper.getInstance(getActivity());
        mSelected = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvMyIngridients = (ListView) view.findViewById(R.id.lvMyItems);
        ListView lvAddIngridients = (ListView) view.findViewById(R.id.lvAddItems);
        mAddIngridientsAdapter = new IngridientAdapter(getActivity(), new ArrayList<IngridientsEntity>());
        mSelectedIngridientsAdapter = new IngridientAdapter(getActivity(), new ArrayList<IngridientsEntity>());
        lvAddIngridients.setAdapter(mAddIngridientsAdapter);
        lvMyIngridients.setAdapter(mSelectedIngridientsAdapter);
        lvAddIngridients.setOnItemClickListener(new AddElementListener());
        mEtSearch = (EditText) view.findViewById(R.id.etSearch);
        mEtSearch.addTextChangedListener(new SearchWatcher());
        Button btnSearch = (Button) view.findViewById(R.id.btnSearchRecipe);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchRecipe:
                Bundle bundle = new Bundle();
                long[] array = new long[mSelected.size()];
                int i=0;
                for(long id : mSelected)
                    array[i++] = id;
                bundle.putLongArray(INGRIDIENTS, array);
                EventBus.getDefault().post(new ChangeFragmentMessage(MainActivity.RECIPES, bundle));
                break;
        }
    }

    private class AddElementListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelected.add(id);
            mSelectedIngridientsAdapter.addObject(IngridientsHelper.getIngridientById(mDbHelper.getReadableDatabase(), id));
            mEtSearch.setText("");
        }
    }

    private class SearchWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() >= 2) {
                List<IngridientsEntity> ingridients = IngridientsHelper.getIngridientsByName(mDbHelper.getReadableDatabase(), s.toString());
                mAddIngridientsAdapter.addObjects(ingridients);
            } else mAddIngridientsAdapter.clearList();
        }
    }
}
