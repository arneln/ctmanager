package com.cloudstaff.cstm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import static com.cloudstaff.cstm.utils.Constants.LOGIN;
import static com.cloudstaff.cstm.utils.Constants.SALT;

public class LoginActivity extends Activity implements View.OnFocusChangeListener {
    private EditText etEmail;
    private EditText etPass;
    private Button btnLogin;
    private TextView tvForgot;
    private ProgressDialog mProgressDialog;
    private SharedPreference mSharedPreference;
    private AndroidCodes mAndroidCodes;
    private String forgotEmail;
    private String overwrite = "no";

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initVariables();
        etEmail.setOnFocusChangeListener(this);
        etPass.setOnFocusChangeListener(this);
        etPass.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnLogin.performClick();
                    return true;
                }
                return false;
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString()
                        .trim().length() == 0 && etPass.getText().toString().trim().length() == 0) {
                    Dialog.showSingleOptionDialog(LoginActivity.this, "",
                            getString(R.string.email_pass_empty), getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, false);
                } else if (etEmail.getText().toString().trim()
                        .length() == 0) {
                    Dialog.showSingleOptionDialog(LoginActivity.this, "",
                            getString(R.string.email_empty), getString(R.string.close
                            ), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, false);
                } else if (etPass.getText().toString().trim()
                        .length() == 0) {
                    Dialog.showSingleOptionDialog(LoginActivity.this, "",
                            getString(R.string.pass_empty), getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, false);
                } else {
                    Login login = new Login(LoginActivity.this);
                    login.execute();
//                    if (etEmail.getText().toString().equalsIgnoreCase("admin") &&
//                            etPass.getText().toString().equalsIgnoreCase("1234")) {
//                        mSharedPreference.createLoginSession(etEmail.getText().toString());
//                        LoginActivity.this.startActivity(
//                                new Intent(LoginActivity.this, MainActivity.class));
//                        LoginActivity.this.finish();
//                        //call async
//                    } else {
//                        Dialog.showSingleOptionDialog(LoginActivity.this, "",
//                                getString(R.string.email_pass_incorrect), getString(R.string.close),
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }, false);
//                    }

                }
            }
        });
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Dialog dialog = new android.app.Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgot_password);
                final EditText emailAddress = (EditText) dialog.findViewById(R.id.et_forgot_email);
                Button resetPass = (Button) dialog.findViewById(R.id.btn_send_forgot_pass);
                Button cancelPass = (Button) dialog.findViewById(R.id.btn_cancel_forgot_pass);
                resetPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //async TODO
                        if (isValidEmail(emailAddress.getText().toString())) {
                            forgotEmail = emailAddress.getText().toString();
                            dialog.dismiss();
                            // TODO DELETE
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    testJSON();
                                }
                            }, 1500);
                            //TODO
                        } else {
                            dialog.dismiss();
                            Dialog.showSingleOptionDialog(LoginActivity.this,
                                    "Email is invalid",
                                    "Your email address is invalid.",
                                    getString(R.string.close),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }, true);
                        }
                    }
                });
                cancelPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void testJSON() {
        String result = "{ \"code\": 200, \"message\": \"An email " +
                "has been sent for resetting your new password.\" }";
        Log.d("test", "" + result);
        try {
            JSONObject jsonObjectResult = new JSONObject(result);
            if (jsonObjectResult.getString("code").equals("200")) {
                Dialog.showSingleOptionDialog(LoginActivity.this,
                        getString(R.string.password_reset),
                        jsonObjectResult.getString("message"),
                        getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, true);
            } else {
//                        Dialog.showSingleOptionDialog(mContext,
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initVariables() {
        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);
        btnLogin = (Button) findViewById(R.id.btn_log_in);
        tvForgot = (TextView) findViewById(R.id.tv_forgot_email_pass);
        mSharedPreference = new SharedPreference(LoginActivity.this);
        mAndroidCodes = new AndroidCodes(LoginActivity.this);
//        Log.e("LOGIN INTERNET!!!!", "" + InternetChecker.isNetworkAvailable(LoginActivity.this));
//        if (!InternetChecker.isNetworkAvailable(LoginActivity.this)){
////            Dialog.showDialog();
//        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.et_email: {
                    if (etEmail.getText().toString().isEmpty()) {
                        etEmail.setError(etEmail.getHint() + " is required");
                    }
                }
                case R.id.et_pass: {
                    if (etPass.getText().toString().isEmpty()) {
                        etPass.setError(etPass.getHint() + " is required");
                    }
                }

            }
        }
    }

    private class Login extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public Login(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext,
                    "",
                    getString(R.string.logging_in),
                    true);
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {

            HttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(BASE_URL + LOGIN);
            int timeoutConnection = 8000;
            int timeoutSocket = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("username", etEmail
                        .getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("password",
                        mAndroidCodes.SHA1(SALT + etPass.getText().toString())));
                nameValuePairs.add(new BasicNameValuePair("deviceID",
                        mAndroidCodes.getDeviceID()));
                nameValuePairs.add(new BasicNameValuePair("devicename",
                        mAndroidCodes.getDeviceName()));
                nameValuePairs.add(new BasicNameValuePair("secureID",
                        mAndroidCodes.md5(getString(R.string.manager))));
                if (overwrite.equalsIgnoreCase("yes")) {
                    nameValuePairs.add(new BasicNameValuePair("overwrite", "yes"));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
//                httpPost.setHeader("Content-Type",
//                        "application/x-www-form-urlencoded");
                HttpResponse response = httpClient.execute(httpPost);
                Log.d("NVP", "" + nameValuePairs.toString());
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
                    Log.e("JSON", "" + result);
                    if (jsonObjectResult.getString("code").equals("200")) {
                        final String clientID = jsonObjectResult.getString("clientID");
                        final String sessionID = jsonObjectResult.getString("sessionID");
                        JSONObject jsonSettings = jsonObjectResult.getJSONObject("settings");
                        final int manualAuto = Integer.parseInt((String) jsonSettings.get("fetchdata"));
                        final String defaultMessage = (String) jsonSettings.get("defaultmessage");
                        final boolean isManual;
                        if (manualAuto == 0) {
                            isManual = true;
                        } else {
                            isManual = false;
                        }
                        if (jsonObjectResult.getString("message")
                                .equalsIgnoreCase("Login successfully.")) {
                            mSharedPreference.setClientId(clientID);
                            mSharedPreference.setSessionId(sessionID);
                            mSharedPreference.setIsUpdateManual(isManual);
                            mSharedPreference.setDefaultPing(defaultMessage);
                            if (!isManual) {
                                mSharedPreference.setUpdateMinutes(manualAuto);
                            }
//                            mSharedPreference.setClientId();
                            mContext.startActivity(
                                    new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            Dialog.showDialog(mContext,
                                    getString(R.string.logged_from_another_device),
                                    jsonObjectResult.getString("message"),
                                    getString(R.string.yes),
                                    getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //proceed with logging in
                                            //TODO overwrite set to yes
                                            overwrite = "yes";
                                            Login login = new Login(mContext);
                                            login.execute();
//                                            mSharedPreference.setClientId(clientID);
//                                            mSharedPreference.setSessionId(sessionID);
//                                            mSharedPreference.setIsUpdateManual(isManual);
//                                            mSharedPreference.setDefaultPing(defaultMessage);
//                                            if (!isManual) {
//                                                mSharedPreference.setUpdateMinutes(manualAuto);
//                                            }
//                                            mContext.startActivity(
//                                                    new Intent(mContext, MainActivity.class));
//                                            finish();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }, true);

                        }
                    } else {
                        Dialog.showSingleOptionDialog(mContext,
                                getString(R.string.log_in_error),
                                jsonObjectResult.getString("message"),
                                getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                                Login login = new Login(mContext);
                                login.execute();
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

    private class ForgotPassword extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public ForgotPassword(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext,
                    "",
                    getString(R.string.logging_in),
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
                nameValuePairs.add(new BasicNameValuePair("email", forgotEmail));
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
                        Dialog.showSingleOptionDialog(mContext,
                                getString(R.string.password_reset),
                                jsonObjectResult.getString("message"),
                                getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }, true);
                    } else {
//                        Dialog.showSingleOptionDialog(mContext,
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
                Dialog.showDialog(mContext,
                        getString(R.string.server_null),
                        getString(R.string.server_null_details),
                        getString(R.string.retry),
                        getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ForgotPassword forgotPassword = new ForgotPassword(mContext);
                                forgotPassword.execute();
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
