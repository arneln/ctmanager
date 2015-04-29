package com.cloudstaff.cstm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudstaff.cstm.utils.AndroidCodes;
import com.cloudstaff.cstm.utils.AppUtils;
import com.cloudstaff.cstm.utils.Dialog;
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

public class SendMessage extends Activity {
    private Button btnCancel, btnSend;
    private EditText etMessage;
    private String staffId;
    private String staffName;
    private ProgressDialog mProgressDialog;
    private SharedPreference mPreference;
    private AndroidCodes mAndroidCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sndmsg);
        initVariables();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString().trim().length() == 0) {
                    Dialog.showSingleOptionDialog(SendMessage.this,
                            getString(R.string.message_empty),
                            getString(R.string.message_empty_details),
                            getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, false);
                } else {
                    //proceed with API CALL
                    String pdText = String.format(
                            getString(R.string.message_sending), staffName);
                    final String toastText = String.format(
                            getString(R.string.message_sent), staffName);
//                    mProgressDialog = ProgressDialog.show
//                            (SendMessage.this, "", pdText, true, false);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressDialog.dismiss();
//                            Toast.makeText(SendMessage.this,
//                                    toastText,
//                                    Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                    }, 3000);
                }
            }
        });
    }

    private void initVariables() {
        btnCancel = (Button) findViewById(R.id.MsgCnl);
        btnSend = (Button) findViewById(R.id.MsgSnd);
        etMessage = (EditText) findViewById(R.id.MsgMsg);
        Intent intent = getIntent();
        staffId = intent.getStringExtra("STAFF_ID");
        staffName = intent.getStringExtra("STAFF_NAME");
        Log.d("staffId", "" + staffId);
        mAndroidCodes = new AndroidCodes(SendMessage.this);
        mPreference = new SharedPreference(SendMessage.this);
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
                nameValuePairs.add(new BasicNameValuePair("staffID",
                        staffId));
                nameValuePairs.add(new BasicNameValuePair("message",
                        etMessage.getText().toString()));
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
