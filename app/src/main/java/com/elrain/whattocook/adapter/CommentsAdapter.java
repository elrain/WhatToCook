package com.elrain.whattocook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elrain.whattocook.R;
import com.elrain.whattocook.util.TimeUtil;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;

import java.util.List;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsAdapter extends BaseAdapter {

    private final List<CommentsResponse> mComments;
    private final LayoutInflater mInflater;

    public CommentsAdapter(Context context, List<CommentsResponse> mComments) {
        this.mComments = mComments;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public CommentsResponse getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mComments.get(position).getCommentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.comments_view, parent, false);
            holder = new ViewHolder();
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvCommentDateTime);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            holder.tvText = (TextView) convertView.findViewById(R.id.tvCommentText);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.tvDateTime.setText(TimeUtil.getFullTime(getItem(position).getTime()));
        holder.tvText.setText(getItem(position).getText());
        holder.tvUserName.setText(getItem(position).getUserName());
        return convertView;
    }

    private static class ViewHolder {
        public TextView tvUserName;
        public TextView tvDateTime;
        public TextView tvText;
    }
}
