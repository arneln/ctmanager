package com.cloudstaff.cstm.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.model.MyTeam;
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

public class MyTeamAdapter extends BaseAdapter {
    Activity context;
    TextView MyTeamName;
    TextView MyTeamFullN;
    TextView MyTeamSSTrack;
    TextView MyTeamSETrack;
    TextView MyTeamTeamTrack;
    TextView MyTeamPosTrack;
    TextView MyTeamStatusTrack;
    ImageView MyTeamPhoto;
    ImageView MyTeamHeart;
    ImageView MyTeamMsg;
    ImageView MyTeamBell;
    //    ImageView MyTeamDotDot;
    Boolean MyFavorito;
    //    int MeFavorito;
    ImageView OnOffLine;
    SharedPreference mPreference;
    Database mDatabase;
    AndroidCodes mAndroidCodes;
    ProgressDialog mProgressDialog;
    String pingMessage;
    String favorite;
    String staffID;
    int globalPosition;

    //    MyTeamModel[] myteamModelItems = null;
    private List<MyTeam> myTeamList = null;
    private LayoutInflater inflater;

    public MyTeamAdapter(Activity context, ArrayList<MyTeam> resource) {
        this.context = context;
        this.myTeamList = resource;
        this.myTeamList = new ArrayList<>();
        this.myTeamList.addAll(resource);
        inflater = LayoutInflater.from(context);
        mPreference = new SharedPreference(context);
        mDatabase = new Database(context);
        mAndroidCodes = new AndroidCodes(context);
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

        convertView = inflater.inflate(R.layout.myteamadapter, parent, false);
        MyTeamName = (TextView) convertView.findViewById(R.id.MyTeamName);
        MyTeamFullN = (TextView) convertView.findViewById(R.id.MyTeamFullN);
//        TextView MyTeamShift = (TextView) convertView.findViewById(R.id.MyTeamShift);
//        TextView MyTeamT = (TextView) convertView.findViewById(R.id.MyTeamT);
//        TextView MyTeamPosition = (TextView) convertView.findViewById(R.id.MyTeamPosition);
//        TextView MyTeamStatus = (TextView) convertView.findViewById(R.id.MyTeamStatus);
        MyTeamSSTrack = (TextView) convertView.findViewById(R.id.MyTeamSSTrack);
        MyTeamSETrack = (TextView) convertView.findViewById(R.id.MyTeamSETrack);
        MyTeamTeamTrack = (TextView) convertView.findViewById(R.id.MyTeamTeamTrack);
        MyTeamPosTrack = (TextView) convertView.findViewById(R.id.MyTeamPosTrack);
        MyTeamStatusTrack = (TextView) convertView.findViewById(R.id.MyTeamStatusTrack);
        MyTeamPhoto = (ImageView) convertView.findViewById(R.id.MyTeamPhoto);
        MyTeamHeart = (ImageView) convertView.findViewById(R.id.MyTeamHeart);
        MyTeamMsg = (ImageView) convertView.findViewById(R.id.MyTeamMsg);
        MyTeamBell = (ImageView) convertView.findViewById(R.id.MyTeamBell);
//        MyTeamDotDot = (ImageView) convertView.findViewById(R.id.MyTeamDotDot);
        OnOffLine = (ImageView) convertView.findViewById(R.id.MyTeamCircle);
        MyTeamName.setText(myTeamList.get(position).getUsername());
        MyTeamFullN.setText(myTeamList.get(position).getName());
        MyTeamSSTrack.setText(myTeamList.get(position).getShift_start());
        MyTeamSETrack.setText(myTeamList.get(position).getShift_end());
        MyTeamTeamTrack.setText(myTeamList.get(position).getTeam());
        MyTeamPosTrack.setText(myTeamList.get(position).getPosition());
        MyTeamStatusTrack.setText(myTeamList.get(position).getStatus());
//        MyTeamPhoto.setImageResource(myteamModelItems[position].getMTPhoto());
//        int color = 0;
        if (myTeamList.get(position).getLogin().equalsIgnoreCase("online")) {
            OnOffLine.setImageResource(R.mipmap.online_list);
        } else {
            OnOffLine.setImageResource(R.mipmap.offline_list);
        }

        Picasso.with(context)
                .load(myTeamList.get(position).getPhoto())
                .placeholder(R.mipmap.unknown)
                .resize(150, 150)
                .centerInside()
                .into(MyTeamPhoto);

        if (myTeamList.get(position).getFavorite().equalsIgnoreCase("Yes")) {
            MyTeamHeart.setImageResource(R.drawable.icon_favourite);
            MyFavorito = true;
        } else {
            MyTeamHeart.setImageResource(R.drawable.icon_unfavourite);
            MyFavorito = false;
        }

        MyTeamHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Favorite", "" + myTeamList.get(position).getFavorite());
                if (myTeamList.get(position).getFavorite().equalsIgnoreCase("Yes")) {
                    //favorite no
                    staffID = myTeamList.get(position).getStf_id();
                    favorite = "No";
                    globalPosition = position;
                    myTeamList.get(position).setFavorite("No");
                    ((ImageView) v).setImageResource(R.drawable.icon_unfavourite);
                    mDatabase.setStaffFavorite(myTeamList.get(position).getStf_id(), "No");
                } else {
                    //favorite yes
                    staffID = myTeamList.get(position).getStf_id();
                    favorite = "Yes";
                    globalPosition = position;
                    myTeamList.get(position).setFavorite("Yes");
                    ((ImageView) v).setImageResource(R.drawable.icon_favourite);
                    mDatabase.setStaffFavorite(myTeamList.get(position).getStf_id(), "Yes");
                }

            }
        });

        MyTeamMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent(context, SendMessage.class)
//                        .putExtra("STAFF_ID", myTeamList.get(position).getStf_id())
//                        .putExtra("STAFF_NAME", myTeamList.get(position).getName()));
                final android.app.Dialog dialog = new android.app.Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogmessage);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                final EditText message = (EditText) dialog.findViewById(R.id.DialogMessage);
                Button sendmsg = (Button) dialog.findViewById(R.id.sendmsg);
                Button cancelmsg = (Button) dialog.findViewById(R.id.cancelmsg);
                sendmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("send message", "" + message.getText().toString());
                        Toast.makeText(context, "Message Has Been Sent", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                cancelmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        MyTeamBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogping);
                final EditText pingMsg = (EditText) dialog.findViewById(R.id.PingMsg);
                Button sendping = (Button) dialog.findViewById(R.id.sendping);
                Button cancelping = (Button) dialog.findViewById(R.id.cancelping);
                pingMsg.setText(mPreference.getDefaultPing());
                sendping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pingMessage = pingMsg.getText().toString();
                        staffID = myTeamList.get(position).getStf_id();
                        dialog.dismiss();
                    }
                });
                cancelping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Your ping has been sent", Toast.LENGTH_LONG).show();
//                        Toast.makeText(context, "What Theee Puck Say?", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return convertView;
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
                    mContext.getString(R.string.sending_ping),
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
                nameValuePairs.add(new BasicNameValuePair("staffID[]",
                        staffID));
                nameValuePairs.add(new BasicNameValuePair("message", pingMessage));
                nameValuePairs.add(new BasicNameValuePair("sessionID",
                        mPreference.getSessionId()));
                nameValuePairs.add(new BasicNameValuePair("deviceID",
                        mAndroidCodes.getDeviceID()));
                nameValuePairs.add(new BasicNameValuePair("secureID",
                        mAndroidCodes.md5(mContext.getString(R.string.manager))));
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
                                mContext.getString(R.string.ping_success),
                                jsonObjectResult.getString("message"),
                                mContext.getString(R.string.close),
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
                        mContext.getString(R.string.server_null),
                        mContext.getString(R.string.server_null_details),
                        mContext.getString(R.string.retry),
                        mContext.getString(R.string.close),
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


    private class SetFavorite extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public SetFavorite(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext,
                    "",
                    mContext.getString(R.string.please_wait),
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
                nameValuePairs.add(new BasicNameValuePair("staffID",
                        staffID));
                nameValuePairs.add(new BasicNameValuePair("favorite", favorite));
                nameValuePairs.add(new BasicNameValuePair("sessionID",
                        mPreference.getSessionId()));
                nameValuePairs.add(new BasicNameValuePair("deviceID",
                        mAndroidCodes.getDeviceID()));
                nameValuePairs.add(new BasicNameValuePair("secureID",
                        mAndroidCodes.md5(mContext.getString(R.string.manager))));
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
                        myTeamList.get(globalPosition).setFavorite(favorite);
                        mDatabase.setStaffFavorite(myTeamList.get(globalPosition).getStf_id(),
                                favorite);
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
                        mContext.getString(R.string.server_null),
                        mContext.getString(R.string.server_null_details),
                        mContext.getString(R.string.retry),
                        mContext.getString(R.string.close),
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
