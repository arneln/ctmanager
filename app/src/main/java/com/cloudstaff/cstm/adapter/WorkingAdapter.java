package com.cloudstaff.cstm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.Working;

import java.util.ArrayList;

public class WorkingAdapter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater inflater;
    private ArrayList<Working> workingArrayList;

    public WorkingAdapter(Activity context, ArrayList<Working> workingArrayList) {
        this.mContext = context;
        this.workingArrayList = new ArrayList<>();
        this.workingArrayList = workingArrayList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return workingArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return workingArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_working, null);
            viewHolder.tvTask = (TextView) convertView.findViewById(R.id.tv_working_task);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_working_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTask.setText(workingArrayList.get(position).getTask());
        viewHolder.tvDate.setText(workingArrayList.get(position).getDate());
        return convertView;
    }

    public class ViewHolder {
        TextView tvTask;
        TextView tvDate;
    }
}
