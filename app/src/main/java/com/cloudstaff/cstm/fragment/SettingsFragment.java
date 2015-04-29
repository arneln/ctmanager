package com.cloudstaff.cstm.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.utils.Alarm;
import com.cloudstaff.cstm.utils.AndroidCodes;
import com.cloudstaff.cstm.utils.AppUtils;
import com.cloudstaff.cstm.utils.SharedPreference;

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

public class SettingsFragment extends android.app.Fragment {

    private RadioGroup rgUpdate;
    private RadioButton rbManual, rbAuto;
    private EditText etDefaultPing, etAutoMinutes;
    private Button btnUpdate;
    private AndroidCodes mAndroidCodes;
    private SharedPreference mPreference;
    private ProgressDialog mProgressDialog;
    private String fetchData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(false);
        getActivity().invalidateOptionsMenu();
        rgUpdate = (RadioGroup) view.findViewById(R.id.rg_update_freq);
        rbManual = (RadioButton) view.findViewById(R.id.rb_update_manual);
        rbAuto = (RadioButton) view.findViewById(R.id.rb_update_automatic);
        etDefaultPing = (EditText) view.findViewById(R.id.et_default_ping);
        etAutoMinutes = (EditText) view.findViewById(R.id.et_auto_minutes);
        btnUpdate = (Button) view.findViewById(R.id.btn_update);
        mPreference = new SharedPreference(getActivity());
        mAndroidCodes = new AndroidCodes(getActivity());
        etDefaultPing.setText(mPreference.getDefaultPing());
        etAutoMinutes.setText("" + mPreference.getUpdateMinutes());
        if (mPreference.isUpdateManual()) {
            rbManual.setChecked(true);
            fetchData = "0";
        } else {
            rbAuto.setChecked(true);
            fetchData = "" + mPreference.getUpdateMinutes();
            etAutoMinutes.setEnabled(true);
        }
        rgUpdate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_update_automatic) {
                    etAutoMinutes.setEnabled(true);
                    mPreference.setIsUpdateManual(false);
                } else {
                    etAutoMinutes.setEnabled(false);
                    mPreference.setIsUpdateManual(true);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(getActivity(), "", "Updating your settings", true);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        mPreference.setDefaultPing(etDefaultPing.getText().toString());
                        mPreference.setUpdateMinutes(Integer.parseInt(etAutoMinutes.getText().toString()));
                        Alarm alarm = new Alarm();
                        if (mPreference.isUpdateManual()) {
                            alarm.CancelAlarm(getActivity());
                        } else {
                            alarm.SetAlarm(getActivity(), Integer.parseInt(etAutoMinutes.getText().toString()));
                        }
                    }
                }, 1500);
            }
        });
        return view;
    }

    private class Message extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public Message(Context context) {
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
                nameValuePairs.add(new BasicNameValuePair("fetchdata",
                        fetchData));
                nameValuePairs.add(new BasicNameValuePair("default_message",
                        mPreference.getDefaultPing()));
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
                                Message sendMessage = new Message(mContext);
                                sendMessage.execute();
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
