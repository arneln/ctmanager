package com.cloudstaff.cstm.utils;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AppUtils {

    /**
     * Get HTML string response
     *
     * @param response
     */
    public static String getHTMLStringResponse(HttpResponse response) {
        try {
            return new GetHTMLStringResponseTask().execute(response).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetHTMLStringResponseTask extends
            AsyncTask<HttpResponse, Void, String> {

        @Override
        protected String doInBackground(HttpResponse... params) {
            HttpEntity entity = params[0].getEntity();
            try {
                return EntityUtils.toString(entity);
            } catch (IOException ioe) {
                // Parsing error || No data retrieved
            }
            return null;
        }
    }

}
