package com.elrain.whattocook.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.elrain.whattocook.R;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectFragment extends Fragment {

    private ListView mLvIngridients;
    private EditText mEtSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvIngridients = (ListView) view.findViewById(R.id.lvItems);
        mEtSearch = (EditText) view.findViewById(R.id.etSearch);
    }
}
