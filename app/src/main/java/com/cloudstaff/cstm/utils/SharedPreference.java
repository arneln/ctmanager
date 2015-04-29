package com.cloudstaff.cstm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cloudstaff.cstm.LoginActivity;
import com.cloudstaff.cstm.MainActivity;
import com.cloudstaff.cstm.SplashActivity;

public class SharedPreference {
    static final String PREFERENCE_NAME = "CSTM";
    static final String IS_LOGIN = "IsLoggedIn";
    static final String EMAIL = "Email";
    static final String UPDATE = "Update";
    static final String UPDATE_MINUTES = "UpdateMinutes";
    static final String DEFAULT_PING = "DefaultPing";
    static final String CLIENT_ID = "ClientId";
    static final String SESSION_ID = "SessionId";
    static int PRIVATE_MODE = 0;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void createLoginSession(String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public String getClientId() {
        return sharedPreferences.getString(CLIENT_ID, "");
    }

    public void setClientId(String clientId) {
        editor.putString(CLIENT_ID, clientId).commit();
    }

    public String getSessionId() {
        return sharedPreferences.getString(SESSION_ID, "");
    }

    public void setSessionId(String sessionId) {
        editor.putString(SESSION_ID, sessionId).commit();
    }


    public boolean isUpdateManual() {
        return sharedPreferences.getBoolean(UPDATE, true);
    }

    public void setIsUpdateManual(boolean isManual) {
        editor.putBoolean(UPDATE, isManual);
        editor.commit();
    }

    public String getDefaultPing() {
        return sharedPreferences.getString(DEFAULT_PING, "");

    }

    public void setDefaultPing(String ping) {
        editor.putString(DEFAULT_PING, ping);
        editor.commit();
    }

    public int getUpdateMinutes() {
        return sharedPreferences.getInt(UPDATE_MINUTES, 0);
    }

    public void setUpdateMinutes(int minutes) {
        editor.putInt(UPDATE_MINUTES, minutes);
        editor.commit();
    }

}
