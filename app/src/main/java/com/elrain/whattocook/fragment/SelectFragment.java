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
import android.widget.EditText;
import android.widget.ListView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.adapter.IngridientAdapter;
import com.elrain.whattocook.adapter.SelectedIngridientsAdapter;
import com.elrain.whattocook.dal.helper.CurrentSelectedHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;
import com.elrain.whattocook.message.CommonMessage;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectFragment extends Fragment implements View.OnClickListener{
    private ListView mLvMyIngridients;
    private ListView mLvAddIngridients;
    private IngridientAdapter mAddIngridientsAdapter;
    private SelectedIngridientsAdapter mSelectedIngridientsAdapter;
    private EditText mEtSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvMyIngridients = (ListView) view.findViewById(R.id.lvMyItems);
        mLvAddIngridients = (ListView) view.findViewById(R.id.lvAddItems);
        mAddIngridientsAdapter = new IngridientAdapter(getActivity(), new ArrayList<IngridientsEntity>());
        mSelectedIngridientsAdapter = new SelectedIngridientsAdapter(getActivity(), new ArrayList<SelectedIngridientsEntity>());
        mLvAddIngridients.setAdapter(mAddIngridientsAdapter);
        mLvMyIngridients.setAdapter(mSelectedIngridientsAdapter);
        mLvAddIngridients.setOnItemClickListener(new AddElementListener());
        mEtSearch = (EditText) view.findViewById(R.id.etSearch);
        mEtSearch.addTextChangedListener(new SearchWatcher());
    }

    public void onEvent(CommonMessage message) {
        if (message.mMessageEvent == CommonMessage.MessageEvent.INGRIDIENT_ADDED) {
            SelectedIngridientsEntity entity = (SelectedIngridientsEntity) message.messageObject;
            CurrentSelectedHelper helper = new CurrentSelectedHelper(getActivity());
            helper.addIngridient(entity);
            mSelectedIngridientsAdapter.addIngridient(entity);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearchRecipe:
                break;
        }
    }

    private class AddElementListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DialogGetter.insertQuantityDialog(getActivity(), new SelectedIngridientsEntity(0, 0, 0, mAddIngridientsAdapter.getItem(position))).show();
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
                IngridientsHelper helper = new IngridientsHelper(getActivity());
                List<IngridientsEntity> ingridients = helper.getIngridientsByName(s.toString());
                mAddIngridientsAdapter.addObjects(ingridients);
            } else mAddIngridientsAdapter.clearList();
        }
    }
}
