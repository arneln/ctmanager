package com.cloudstaff.cstm.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.MyTeam;
import com.cloudstaff.cstm.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardImageListAdapter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater inflater;
    private ArrayList<MyTeam> myTeamArrayList;

    public DashboardImageListAdapter(Activity context, ArrayList<MyTeam> myTeams) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.myTeamArrayList = new ArrayList<>();
        this.myTeamArrayList = myTeams;
    }

    @Override
    public int getCount() {
        return myTeamArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return myTeamArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DashboardImageListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_view_image, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int color = 0;
        if (myTeamArrayList.get(position).getLogin().equalsIgnoreCase("online")) {
            color = Color.parseColor("#7DBC00");
        } else {
            color = Color.parseColor("#CCCCCC");
        }
        Picasso.with(mContext)
                .load(myTeamArrayList.get(position).getPhoto())
                .placeholder(R.mipmap.unknown)
                .error(R.mipmap.unknown)
                .transform(new CircleTransform(mContext, color))
                .resize(180, 180)
                .centerInside()
                .into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
    }
}
