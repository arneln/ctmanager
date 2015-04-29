package com.cloudstaff.cstm.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.MyTeam;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PingAdapter extends BaseAdapter {
    //    public ListView PingListAdap;
//    PingModel[] modelItems = null;
    Context context;
    ImageView MyPingCircle;
    //    boolean Checked = false;
    private List<MyTeam> myTeamList = null;
    private LayoutInflater inflater;

    public PingAdapter(Context context, ArrayList<MyTeam> resource) {
//        super(context, R.layout.pingchecker, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.myTeamList = resource;
        this.myTeamList = new ArrayList<MyTeam>();
        this.myTeamList.addAll(resource);
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return myTeamList.size();
    }

    @Override
    public Object getItem(int position) {
        return myTeamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.pingchecker, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView team = (TextView) convertView.findViewById(R.id.team);
        CheckBox cbemp = (CheckBox) convertView.findViewById(R.id.checkBoxEmp);
        ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
        MyPingCircle = (ImageView) convertView.findViewById(R.id.MyPingCircle);
//        MyPingCircle.setVisibility(convertView.GONE);
        name.setText(myTeamList.get(position).getName());
        team.setText(myTeamList.get(position).getTeam());
        Picasso.with(context)
                .load(myTeamList.get(position).getPhoto())
                .resize(60, 60)
                .centerInside()
                .placeholder(R.mipmap.unknown)
                .into(photo);
        cbemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myTeamList.get(position).setChecked(isChecked);
            }
        });
        cbemp.setChecked(myTeamList.get(position).isChecked());
//        if (modelItems[position].getValue() == true) {
//            cbemp.setChecked(true);
//        } else {
//            cbemp.setChecked(false);
//        }
//        if (myTeamList.get(position).getLogin() == "offline") {
//            MyPingCircle.setImageResource(R.drawable.circle3);
//        } else {
//            MyPingCircle.setImageResource(R.drawable.circle2);
//        }

//
        BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
//        photo.setBackground(getContext().getResources().getDrawable(R.drawable.circle2));
        photo.setImageBitmap(circleBitmap);


        return convertView;
    }

}