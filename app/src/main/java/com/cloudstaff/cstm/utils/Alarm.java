package com.cloudstaff.cstm.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

import static com.cloudstaff.cstm.utils.Constants.BASE_URL;

public class Alarm extends BroadcastReceiver {
    Database mDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (InternetChecker.isNetworkAvailable(context)) {
            mDatabase = new Database(context);
            GetData getData = new GetData(context);
            getData.execute();
        }
    }

    public void SetAlarm(Context context, int minutes) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * minutes, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private class GetData extends AsyncTask<Void, Void, HttpResponse> {
        private Context mContext;

        public GetData(Context context) {
            this.mContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            HttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            String url = BASE_URL;

            int timeoutConnection = 8000;
            int timeoutSocket = 10000;

            HttpConnectionParams.setConnectionTimeout(httpParams,
                    timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
            try {
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);

                if (httpResponse != null) {
                    return httpResponse;
                }
            } catch (UnsupportedEncodingException e) {
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
            }
            super.onPostExecute(httpResponse);
        }
    }
}
