package com.cloudstaff.cstm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.Metrics;

import java.util.ArrayList;
import java.util.List;


public class DashboardListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Metrics> myMetricsList = null;
    private ArrayList<Metrics> myMetricsArrayList;

    public DashboardListAdapter(Context context, ArrayList<Metrics> metricses) {
        this.context = context;
        this.myMetricsList = metricses;
        this.myMetricsList = new ArrayList<>();
        this.myMetricsList.addAll(metricses);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myMetricsList.size();
    }

    @Override
    public Object getItem(int position) {
        return myMetricsList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_dashboard, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvDailyAverage = (TextView) convertView.findViewById(R.id.tv_daily_average);
            viewHolder.tvWeeklyAverage = (TextView) convertView.findViewById(R.id.tv_weekly_average);
            viewHolder.tvTotalData = (TextView) convertView.findViewById(R.id.tv_total);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(myMetricsList.get(position).getTitle());
        viewHolder.tvDailyAverage.setText(myMetricsList.get(position).getDailyAverage());
        viewHolder.tvWeeklyAverage.setText(myMetricsList.get(position).getWeeklyAverage());
        viewHolder.tvTotalData.setText(myMetricsList.get(position).getTotalData());
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle, tvDailyAverage, tvWeeklyAverage, tvTotalData;
    }
}
