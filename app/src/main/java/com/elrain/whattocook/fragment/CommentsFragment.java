package com.elrain.whattocook.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.helper.ProgressBarDialogBuilder;
import com.elrain.whattocook.adapter.CommentsAdapter;
import com.elrain.whattocook.message.CommentsMessage;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsFragment extends Fragment implements View.OnClickListener{

    public static final String RECIPE_ID = "recipeId";
    private ListView mLvComments;
    private TextView mTvNoComments;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvComments = (ListView) view.findViewById(R.id.lvComments);
        mTvNoComments = (TextView) view.findViewById(R.id.tvNoComments);
        mLvComments.setAdapter(new CommentsAdapter(getActivity(), new ArrayList<CommentsResponse>()));
        mTvNoComments.setVisibility(View.VISIBLE);
        Button btnLeaveComment = (Button) view.findViewById(R.id.btnLeaveComment);
        btnLeaveComment.setOnClickListener(this);

        mProgressDialog = new ProgressBarDialogBuilder(getActivity(),
                getString(R.string.dialog_message_getting_comments)).build();
        mProgressDialog.show();
        ApiWorker apiWorker = ApiWorker.getInstance(getActivity());
        apiWorker.getComments(getArguments().getLong(RECIPE_ID));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(CommentsMessage message) {
        mProgressDialog.dismiss();
        if (null != message.comments && message.comments.size() != 0)
            mTvNoComments.setVisibility(View.GONE);
        else mTvNoComments.setVisibility(View.VISIBLE);
        mLvComments.setAdapter(new CommentsAdapter(getActivity(), message.comments));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLeaveComment){

        }
    }
}
