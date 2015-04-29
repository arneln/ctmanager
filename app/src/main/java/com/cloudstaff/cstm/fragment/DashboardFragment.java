package com.cloudstaff.cstm.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.adapter.DashboardListAdapter;
import com.cloudstaff.cstm.model.MyTeam;
import com.cloudstaff.cstm.utils.AndroidCodes;
import com.cloudstaff.cstm.utils.AppUtils;
import com.cloudstaff.cstm.utils.Database;
import com.cloudstaff.cstm.utils.Dialog;
import com.cloudstaff.cstm.utils.ProfileActivity;
import com.cloudstaff.cstm.utils.SharedPreference;
import com.devsmart.android.ui.HorizontalListView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.cloudstaff.cstm.utils.Constants.BASE_URL;

public class DashboardFragment extends android.app.Fragment {
    private TextView tvStaffName;
    private String staffId;
    private ListView lvDashboard;
    private DashboardListAdapter mDashboardListAdapter;
    private SharedPreference mPreference;
    //    private LinearLayout imagesLayout;
//    private ArrayList<String> imageLinkList = new ArrayList<String>();
    private ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
    private Database mDatabase;
    private AndroidCodes mAndroidCodes;
    private ProgressDialog mProgressDialog;
    private HorizontalListView listView;
    private BaseAdapter mAdapter = new BaseAdapter() {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_image, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.imageStat = (ImageView) convertView.findViewById(R.id.image_stat);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (myTeamArrayList.get(position).getLogin().equalsIgnoreCase("online")) {
//                color = Color.parseColor("#7DBC00");
                viewHolder.imageStat.setImageDrawable(getResources().getDrawable(R.mipmap.online));
            } else {
//                color = Color.parseColor("#CCCCCC");
                viewHolder.imageStat.setImageDrawable(getResources().getDrawable(R.mipmap.offline));
            }
            if (myTeamArrayList.get(position).getBooleanImage()) {
                Picasso.with(getActivity())
                        .load(myTeamArrayList.get(position).getPhoto())
                        .placeholder(R.mipmap.unknown)
                        .error(R.mipmap.unknown)
//                        .transform(new CircleTransform(getActivity(), color))
                        .resize(150, 150)
                        .centerInside()
                        .into(viewHolder.imageView);
            } else {
                Picasso.with(getActivity())
                        .load(R.mipmap.unknown)
                        .placeholder(R.mipmap.unknown)
                        .error(R.mipmap.unknown)
//                        .transform(new CircleTransform(getActivity(), color))
                        .resize(150, 150)
                        .centerInside()
                        .into(viewHolder.imageView);
            }
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            final String itemValue;
            if (bundle != null) {
                itemValue = (String) bundle.get("a");
                Log.e("dashboard", "" + itemValue);
                dataSetChangeMetrics(itemValue);
                viewHolder.imageView.performClick();
                intent.removeExtra("a");
                bundle.clear();
            }
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataSetChange(position);
                }
            });
            viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent i = new Intent(getActivity(), ProfileActivity.class);
                    i.putExtra("StaffID", myTeamArrayList.get(position).getStf_id());
                    startActivity(i);
                    return true;
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            ImageView imageStat;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lvDashboard = (ListView) view.findViewById(R.id.lv_dashboard);
        setHasOptionsMenu(true);
//        imagesLayout = (LinearLayout) view.findViewById(R.id.images);
        mDatabase = new Database(getActivity());
        mAndroidCodes = new AndroidCodes(getActivity());
        mPreference = new SharedPreference(getActivity());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tvStaffName = (TextView) view.findViewById(R.id.tv_staff_name);
        listView = (HorizontalListView) view.findViewById(R.id.listView_image);
//        mProgressDialog = ProgressDialog.show(getActivity(), "", "Getting data....", true);


        if (mDatabase.getData().isEmpty()) {
            GetData getData = new GetData(getActivity());
            getData.execute();
        } else {
            myTeamArrayList = mDatabase.getData();
            dataSetChange(0);
        }


        tvStaffName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("StaffID", staffId);
                startActivity(i);
            }
        });
//        mDashboardImageListAdapter = new DashboardImageListAdapter(getActivity(), myTeamArrayList);
        Log.d("arraylist", "" + myTeamArrayList.size());
        listView.setAdapter(mAdapter);
//        mDashboardImageListAdapter.notifyDataSetChanged();
//        for (int i = 0; i < myTeamArrayList.size(); i++) {
//            addImages(myTeamArrayList.get(i).getPhoto());
//        }
//        mProgressDialog.dismiss();


//        ListData listData = new ListData(getActivity());
//        listData.execute();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                GetData getData = new GetData(getActivity());
                getData.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dataSetChange(int index) {
        myTeamArrayList = mDatabase.getData();
        tvStaffName.setText(myTeamArrayList.get(index).getName());
        staffId = myTeamArrayList.get(index).getStf_id();
        mDashboardListAdapter = new DashboardListAdapter(getActivity(), myTeamArrayList.get(index).getMetrics());
        lvDashboard.setAdapter(mDashboardListAdapter);
    }

    public void dataSetChangeMetrics(String id) {
//        myTeamArrayList = mDatabase.getDataById(id);
        tvStaffName.setText(mDatabase.getDataById(id).get(0).getName());
        staffId = mDatabase.getDataById(id).get(0).getStf_id();
        mDashboardListAdapter = new DashboardListAdapter(getActivity(), mDatabase.getMetricsById(id));
        lvDashboard.setAdapter(mDashboardListAdapter);
    }

    private class GetData extends AsyncTask<Void, Void, HttpResponse> {
        private Activity mContext;

        public GetData(Activity context) {
            this.mContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext, "", "Refreshing data....", true);
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {

            HttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(BASE_URL);
            int timeoutConnection = 8000;
            int timeoutSocket = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("clientID",
                        mPreference.getClientId()));
                nameValuePairs.add(new BasicNameValuePair("sessionID",
                        mPreference.getSessionId()));
                nameValuePairs.add(new BasicNameValuePair("deviceID",
                        mAndroidCodes.getDeviceID()));
                nameValuePairs.add(new BasicNameValuePair("secureID",
                        mAndroidCodes.md5(getString(R.string.manager))));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
//                httpPost.setHeader("Content-Type",
//                        "application/x-www-form-urlencoded");
//                httpPost.setHeader("User-Agent", mVersionCodes.getUserAgent());
                HttpResponse response = httpClient.execute(httpPost);

                if (response != null) {
                    return response;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", "" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            if (httpResponse != null) {
                mDatabase.cleanTable();
                String response = AppUtils.getHTMLStringResponse(httpResponse);
                try {
                    JSONObject dashboardJsonObject = new JSONObject(response);
                    JSONArray myTeamJsonArray = dashboardJsonObject.getJSONArray("myTeam");
                    JSONObject dashboardDetailsJsonObject;
                    JSONArray metricsJsonArray;
                    JSONObject metricsObject;
                    JSONArray workingJsonArray;
                    JSONObject workingObject;
                    for (int i = 0; i < myTeamJsonArray.length(); i++) {
                        dashboardDetailsJsonObject = myTeamJsonArray.getJSONObject(i);
                        String ID = dashboardDetailsJsonObject.optString("id", "");
                        String STF_ID = dashboardDetailsJsonObject.optString("stf_id", "");
                        String USERNAME = dashboardDetailsJsonObject.optString("username", "");
                        String NAME = dashboardDetailsJsonObject.optString("name", "");
                        String PHOTO = dashboardDetailsJsonObject.optString("photo", "");
                        String SHIFT_START = dashboardDetailsJsonObject.optString("shift_start", "");
                        String SHIFT_END = dashboardDetailsJsonObject.optString("shift_end", "");
                        String TEAM = dashboardDetailsJsonObject.optString("team", "");
                        String POSITION = dashboardDetailsJsonObject.optString("position", "");
                        String STATUS = dashboardDetailsJsonObject.optString("status", "");
                        String FAVORITE = dashboardDetailsJsonObject.optString("favorite", "");
                        String LOGIN = dashboardDetailsJsonObject.optString("login", "");
                        URLConnection connection;
                        String IMAGE = null;
                        try {
                            connection = new URL(PHOTO).openConnection();
                            String contentType = connection.getHeaderField("Content-Type");
                            IMAGE = "" + contentType.startsWith("image/");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mDatabase.setData(ID, STF_ID, USERNAME, NAME, PHOTO, SHIFT_START,
                                SHIFT_END, TEAM, POSITION, STATUS, FAVORITE, LOGIN, IMAGE);
                        metricsJsonArray = dashboardDetailsJsonObject.getJSONArray("metrics");
                        for (int j = 0; j < metricsJsonArray.length(); j++) {
                            metricsObject = metricsJsonArray.getJSONObject(j);
                            String TITLE = metricsObject.optString("title", "");
                            String DAILY = metricsObject.optString("daily", "");
                            String WEEKLY = metricsObject.optString("weekly", "");
                            String VALUE = metricsObject.optString("value", "");
                            mDatabase.setMetrics(STF_ID, TITLE, DAILY, WEEKLY, VALUE);
                        }
                        workingJsonArray = dashboardDetailsJsonObject.getJSONArray("working");
                        for (int k = 0; k < workingJsonArray.length(); k++) {
                            workingObject = workingJsonArray.getJSONObject(k);
                            String TASK = workingObject.optString("task", "");
                            String DATE = workingObject.optString("date", "");
                            mDatabase.setWorking(STF_ID, TASK, DATE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
                myTeamArrayList = mDatabase.getData();
                dataSetChange(0);
            } else {
                mProgressDialog.dismiss();
                Dialog.showDialog(mContext,
                        getString(R.string.server_null),
                        getString(R.string.server_null_details),
                        getString(R.string.retry),
                        getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GetData getData = new GetData(mContext);
                                getData.execute();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, true);
            }
            super.onPostExecute(httpResponse);
        }
    }


}
