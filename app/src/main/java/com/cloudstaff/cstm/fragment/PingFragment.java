package com.cloudstaff.cstm.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.MyTeam;
import com.cloudstaff.cstm.model.PingModel;
import com.cloudstaff.cstm.utils.AndroidCodes;
import com.cloudstaff.cstm.utils.AppUtils;
import com.cloudstaff.cstm.utils.Database;
import com.cloudstaff.cstm.utils.SharedPreference;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.cloudstaff.cstm.utils.Constants.BASE_URL;

public class PingFragment extends android.app.Fragment {

    PingModel[] modelItems;
    ListView PingList;
    int check = 0;
    CheckBox ChkUnChk;
    //    Boolean wahaha = false;
    ListView listView;
    private ImageButton btnPing;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> imageLinks = new ArrayList<String>();
    private ArrayList<String> imageLinkList = new ArrayList<String>();
    private ArrayList<MyTeam> myTeamArrayList = new ArrayList<MyTeam>();
    private Database mDatabase;
    private int numChecked = 0;
    private String unselectedId;
    private SharedPreference mPreference;
    private boolean selected;
    private BaseAdapter adapters = new BaseAdapter() {

        Activity context;
        ImageView MyPingCircle;

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
            // TODO Auto-generated method stub
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            convertView = inflater.inflate(R.layout.pingchecker, parent, false);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pingchecker, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView team = (TextView) convertView.findViewById(R.id.team);
            CheckBox cbemp = (CheckBox) convertView.findViewById(R.id.checkBoxEmp);
            ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
            MyPingCircle = (ImageView) convertView.findViewById(R.id.MyPingCircle);
//            MyPingCircle.setVisibility(convertView.GONE);
            name.setText(myTeamArrayList.get(position).getName());
            team.setText(myTeamArrayList.get(position).getTeam());
//            int color = 0;
            if (myTeamArrayList.get(position).getLogin().equalsIgnoreCase("online")) {
                MyPingCircle.setImageResource(R.mipmap.online_list);
            } else {
                MyPingCircle.setImageResource(R.mipmap.offline_list);
            }
            Picasso.with(context)
                    .load(myTeamArrayList.get(position).getPhoto())
                    .resize(60, 60)
//                    .transform(new CircleTransform(context, color))
                    .centerInside()
                    .placeholder(R.mipmap.unknown)
                    .into(photo);

            cbemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Log.e("clicked", "" + isChecked);
                    myTeamArrayList.get(position).setChecked(isChecked);

                    numChecked = 0;
                    for (int i = 0; i < myTeamArrayList.size(); i++) {
                        if (myTeamArrayList.get(i).isChecked()) {
                            numChecked++;
                        }
                    }
                    if (myTeamArrayList.size() == numChecked) {
                        ChkUnChk.setChecked(true);
                    }

                    if (ChkUnChk.isChecked()) {
                        if (!isChecked) {
                            selected = true;
                            unselectedId = myTeamArrayList.get(position).getStf_id();
                            ChkUnChk.setChecked(false);
                        }
                    }
//                    else {
//
//                    }


//                    for (int i = 0; i < myTeamArrayList.size(); i++) {
//                        if (isChecked) {
//                            numChecked++;
//                        } else {
//                            numChecked--;
//                        }
//                    }

//                    if (numChecked == myTeamArrayList.size()) {
//                        ChkUnChk.setChecked(true);
//                    }

//                    Log.d("num: " + numChecked, "size: " + myTeamArrayList.size());
                }
            });
//            dataSetChanged();
            cbemp.setChecked(myTeamArrayList.get(position).isChecked());
            return convertView;
        }

    };
    private AndroidCodes mAndroidCodes;
    private String pingMessage;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ping, container, false);

        ChkUnChk = (CheckBox) view.findViewById(R.id.checkBox);
        PingList = (ListView) view.findViewById(R.id.PingList);
        btnPing = (ImageButton) view.findViewById(R.id.btn_ping);

        mDatabase = new Database(getActivity());
        mPreference = new SharedPreference(getActivity());
        mAndroidCodes = new AndroidCodes(getActivity());
        myTeamArrayList = mDatabase.getData();

        dataSetChanged();
//        GetData getData = new GetData(getActivity());
//        getData.execute();

        ChkUnChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getActivity(),
//                        "NGA NGA", Toast.LENGTH_SHORT).show();
                if (selected) {
                    for (int i = 0; i < myTeamArrayList.size(); i++) {
                        if (myTeamArrayList.get(i).getStf_id().equalsIgnoreCase(unselectedId)) {
                            myTeamArrayList.get(i).setChecked(false);
                        } else {
                            myTeamArrayList.get(i).setChecked(true);
                        }

                    }
                    selected = false;
                } else {
                    for (int i = 0; i < myTeamArrayList.size(); i++) {
                        myTeamArrayList.get(i).setChecked(isChecked);
                    }
                }
                dataSetChanged();
            }
        });


        btnPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < myTeamArrayList.size(); i++) {
                    if (myTeamArrayList.get(i).isChecked()) {
                        Log.d("Name: ",
                                "" + myTeamArrayList.get(i).getName());
                    }
                }
                Log.d("num: " + numChecked, "size: " + myTeamArrayList.size());
                if (numChecked > 0) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialogping);
                    final EditText pingMsg = (EditText) dialog.findViewById(R.id.PingMsg);
                    Button sendping = (Button) dialog.findViewById(R.id.sendping);
                    Button cancelping = (Button) dialog.findViewById(R.id.cancelping);
                    pingMsg.setText(mPreference.getDefaultPing());
                    sendping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Async
                            pingMessage = pingMsg.getText().toString();
                            Toast.makeText(getActivity(),
                                    "Your ping has been sent", Toast.LENGTH_LONG).show();
                        }
                    });
                    cancelping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    com.cloudstaff.cstm.utils.Dialog.showSingleOptionDialog
                            (getActivity(),
                                    getString(R.string.ping_empty),
                                    getString(R.string.ping_empty_details),
                                    getString(R.string.close),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }, false);
                }
            }
        });

        PingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("test", "" + myTeamArrayList.get(position).getStf_id());
                if (myTeamArrayList.get(position).isChecked()) {
                    myTeamArrayList.get(position).setChecked(false);
                } else {
                    myTeamArrayList.get(position).setChecked(true);
                }

            }
        });
        return view;
    }

    private void dataSetChanged() {
        PingList.setAdapter(adapters);
    }


    private class Ping extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public Ping(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext,
                    "",
                    getString(R.string.sending_ping),
                    true);
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
                for (int i = 0; i < myTeamArrayList.size(); i++) {
                    if (myTeamArrayList.get(i).isChecked()) {
                        nameValuePairs.add(new BasicNameValuePair("staffID[]",
                                myTeamArrayList.get(i).getStf_id()));
                    }
                }
                nameValuePairs.add(new BasicNameValuePair("message", pingMessage));
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
                mProgressDialog.dismiss();
                String result = AppUtils.getHTMLStringResponse(httpResponse);
                try {
                    JSONObject jsonObjectResult = new JSONObject(result);
                    if (jsonObjectResult.getString("code").equals("200")) {
                        com.cloudstaff.cstm.utils.Dialog.showSingleOptionDialog(mContext,
                                getString(R.string.ping_success),
                                jsonObjectResult.getString("message"),
                                getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }, true);
                    } else {
//                        com.cloudstaff.cstm.utils.Dialog.showSingleOptionDialog(mContext,
//                                getString(R.string.log_in_error),
//                                jsonObjectResult.getString("message"),
//                                getString(R.string.close), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mProgressDialog.dismiss();
                com.cloudstaff.cstm.utils.Dialog.showDialog(mContext,
                        getString(R.string.server_null),
                        getString(R.string.server_null_details),
                        getString(R.string.retry),
                        getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ping ping = new Ping(mContext);
                                ping.execute();
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
