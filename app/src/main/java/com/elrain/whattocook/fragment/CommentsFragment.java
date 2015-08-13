package com.elrain.whattocook.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.activity.MainActivity;
import com.elrain.whattocook.activity.helper.DialogGetter;
import com.elrain.whattocook.activity.helper.ProgressBarDialogBuilder;
import com.elrain.whattocook.adapter.CommentsAdapter;
import com.elrain.whattocook.message.ChangeFragmentMessage;
import com.elrain.whattocook.message.ListMessage;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.util.NetworkUtil;
import com.elrain.whattocook.util.Preferences;
import com.elrain.whattocook.webutil.rest.api.ApiWorker;
import com.elrain.whattocook.webutil.rest.body.CommentBody;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsFragment extends Fragment implements View.OnClickListener {

    public static final String RECIPE_ID = "recipeId";
    private ListView mLvComments;
    private TextView mTvNoComments;
    private ProgressDialog mProgressDialog;
    private Button mBtnLeaveComment;
    private EditText mEtComment;
    private Preferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPreferences = Preferences.getInstance(getActivity());
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
        mBtnLeaveComment = (Button) view.findViewById(R.id.btnLeaveComment);
        mBtnLeaveComment.setOnClickListener(this);
        mEtComment = (EditText) view.findViewById(R.id.etComment);

        if (Preferences.getInstance(getActivity()).getUserType() == 3) {
            mBtnLeaveComment.setVisibility(View.GONE);
        }

        getComments();
    }

    private void getComments() {
        if (NetworkUtil.isNetworkOnline(getActivity())) {
            if (null == mProgressDialog)
                mProgressDialog = new ProgressBarDialogBuilder(getActivity(),
                        getString(R.string.dialog_message_getting_comments)).build();
            else mProgressDialog.setMessage(getString(R.string.dialog_message_getting_comments));
            mProgressDialog.show();
            ApiWorker apiWorker = ApiWorker.getInstance(getActivity());
            apiWorker.getComments(getArguments().getLong(RECIPE_ID));
        } else DialogGetter.noInternetDialogSecond(getActivity(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        EventBus.getDefault().post(new ChangeFragmentMessage(MainActivity.RECIPES, null));
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        getComments();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ListMessage message) {
        mProgressDialog.dismiss();
        if (null != message.list && message.list.size() != 0)
            mTvNoComments.setVisibility(View.GONE);
        else mTvNoComments.setVisibility(View.VISIBLE);
        mLvComments.setAdapter(new CommentsAdapter(getActivity(), message.list));
    }

    public void onEventMainThread(CommonMessage message) {
        mProgressDialog.dismiss();
        if (message.mMessageEvent == CommonMessage.MessageEvent.COMMENT_SENT) {
            mBtnLeaveComment.setText(getString(R.string.button_leave_comments));
            mEtComment.setVisibility(View.GONE);
            getComments();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLeaveComment) {
            if (mEtComment.getVisibility() == View.GONE) {
                mBtnLeaveComment.setText(getString(R.string.button_leave_comments_send));
                mEtComment.setVisibility(View.VISIBLE);
            } else {
                String text = mEtComment.getText().toString();
                if ("".equals(text)) {
                    mBtnLeaveComment.setText(getString(R.string.button_leave_comments));
                    mEtComment.setVisibility(View.GONE);
                } else {
                    mProgressDialog.setMessage(getString(R.string.dialog_message_sending_comment));
                    mProgressDialog.show();
                    CommentBody comment = new CommentBody(text, mPreferences.getUserId(),
                            getArguments().getLong(RECIPE_ID));
                    ApiWorker.getInstance(getActivity()).sendComment(comment);
                }
            }
        }
    }
}
