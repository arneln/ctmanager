package com.cloudstaff.cstm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudstaff.cstm.model.Metrics;
import com.cloudstaff.cstm.model.MyTeam;
import com.cloudstaff.cstm.model.Working;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "manager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STAFF_DETAILS = "staff_details";
    private static final String TABLE_METRICS = "metrics";
    private static final String TABLE_WORKING = "working";

    private static final String ID = "id";
    private static final String STAFF_ID = "stf_id";
    private static final String USERNAME = "username";
    private static final String NAME = "name";
    private static final String PHOTO = "photo";
    private static final String SHIFT_START = "shift_start";
    private static final String SHIFT_END = "shift_end";
    private static final String TEAM = "team";
    private static final String POSITION = "position";
    private static final String STATUS = "status";
    private static final String FAVORITE = "favorite";
    private static final String LOGIN = "login";
    private static final String IMAGE = "image";

    private static final String TITLE = "title";
    private static final String DAILY = "daily";
    private static final String WEEKLY = "weekly";
    private static final String VALUE = "value";

    private static final String TASK = "task";
    private static final String DATE = "date";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STAFF_DETAILS + "(" + ID + " INTEGER, "
                + STAFF_ID + " INTEGER, " + USERNAME + " TEXT, " + NAME + " TEXT, "
                + PHOTO + " BLOB, " + SHIFT_START + " TEXT, " + SHIFT_END + " TEXT, "
                + TEAM + " TEXT, " + POSITION + " TEXT, " + STATUS + " TEXT, "
                + FAVORITE + " TEXT, " + LOGIN + " TEXT, " + IMAGE + " TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_METRICS + "("
                + ID + " INTEGER PRIMARY KEY, " + STAFF_ID + " INTEGER, "
                + TITLE + " TEXT, " + DAILY + " TEXT, "
                + WEEKLY + " TEXT, " + VALUE + " TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WORKING + "("
                + ID + " INTEGER PRIMARY KEY, " + STAFF_ID + " INTEGER, "
                + TASK + " TEXT, " + DATE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    //for future use, drop, alter, etc....
                    break;
            }
            upgradeTo++;
        }
    }

    public void cleanTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteStaff = "DELETE FROM " + TABLE_STAFF_DETAILS;
        String deleteMetrics = "DELETE FROM " + TABLE_METRICS;
        db.execSQL(deleteStaff);
        db.execSQL(deleteMetrics);
        db.close();
    }

    public ArrayList<MyTeam> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + "";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getDataById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + STAFF_ID + "=" + id + "";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<Metrics> getMetricsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_METRICS + " WHERE " + STAFF_ID + "=" + id + "";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<Metrics> metricsArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Metrics metrics = new Metrics();
                metrics.setDailyAverage(cursor.getString(cursor.getColumnIndex(DAILY)));
                metrics.setWeeklyAverage(cursor.getString(cursor.getColumnIndex(WEEKLY)));
                metrics.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                metrics.setTotalData(cursor.getString(cursor.getColumnIndex(VALUE)));
                metricsArrayList.add(metrics);
            }
        }
        return metricsArrayList;
    }


    public ArrayList<MyTeam> getAllStaffNoStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + "";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getAllStaffLogin(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + LOGIN + "='" + status + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getAllStaffStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + STATUS + "='" + status + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getAllStaffTeam(String team) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + TEAM + "='" + team + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }


    public ArrayList<MyTeam> getStaffByTeamStatus(String team, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + TEAM + " = '" + team + "' AND " + STATUS + " = '" + status + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getStaffByTeamOnline(String team, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + TEAM + " = '" + team + "' AND " + LOGIN + " = '" + status + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getStaffByFavorites(String team, String favorite) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + TEAM + " = '" + team + "' AND " + FAVORITE + " = '" + favorite + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }

    public ArrayList<MyTeam> getAllStaffByFavorites(String favorite) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + FAVORITE + " = '" + favorite + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }


    public ArrayList<String> getSpinnerData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT DISTINCT " + TEAM + " FROM " + TABLE_STAFF_DETAILS;
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("All");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                spinnerList.add(cursor.getString(cursor.getColumnIndex(TEAM)));
            }
        }
        return spinnerList;
    }

    public ArrayList<Working> getWorkingData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_WORKING + " WHERE " + STAFF_ID + "=" + id + "";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        ArrayList<Working> workingArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Working working = new Working();
                working.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                working.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                workingArrayList.add(working);
            }
        }
        return workingArrayList;
    }

    public ArrayList<MyTeam> getMetrics(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_STAFF_DETAILS + " WHERE "
                + STAFF_ID + " = '" + id + "' COLLATE NOCASE";
        Cursor cursor = db.rawQuery(sqlString, null);
        ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyTeam myTeam = new MyTeam();
                myTeam.setId(cursor.getString(cursor.getColumnIndex(ID)));
                myTeam.setStf_id(cursor.getString(cursor.getColumnIndex(STAFF_ID)));
                myTeam.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                myTeam.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                myTeam.setPhoto(cursor.getString(cursor.getColumnIndex(PHOTO)));
                myTeam.setShift_start(cursor.getString(cursor.getColumnIndex(SHIFT_START)));
                myTeam.setShift_end(cursor.getString(cursor.getColumnIndex(SHIFT_END)));
                myTeam.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                myTeam.setPosition(cursor.getString(cursor.getColumnIndex(POSITION)));
                myTeam.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                myTeam.setFavorite(cursor.getString(cursor.getColumnIndex(FAVORITE)));
                myTeam.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
                myTeam.setBooleanImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IMAGE))));
                String sqlMetrics = "SELECT * FROM " + TABLE_METRICS + " WHERE "
                        + STAFF_ID + "=" + cursor.getString(cursor.getColumnIndex(STAFF_ID)) + ";";
                Cursor cursor2 = db.rawQuery(sqlMetrics, null);
                ArrayList<Metrics> metricsArrayList = new ArrayList<>();
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        Metrics metrics = new Metrics();
                        metrics.setTitle(cursor2.getString(cursor2.getColumnIndex(TITLE)));
                        metrics.setDailyAverage(cursor2.getString(cursor2.getColumnIndex(DAILY)));
                        metrics.setWeeklyAverage(cursor2.getString(cursor2.getColumnIndex(WEEKLY)));
                        metrics.setTotalData(cursor2.getString(cursor2.getColumnIndex(VALUE)));
                        metricsArrayList.add(metrics);
                    }
                }
                myTeam.setMetrics(metricsArrayList);
                myTeamArrayList.add(myTeam);
            }
        }
        return myTeamArrayList;
    }


//    cv.put(ID, dashboardDetailsJsonObject.optString("id", ""));
//    cv.put(STAFF_ID, dashboardDetailsJsonObject.optString("stf_id", ""));
//    cv.put(USERNAME, dashboardDetailsJsonObject.optString("username", ""));
//    cv.put(NAME, dashboardDetailsJsonObject.optString("name", ""));
//    cv.put(PHOTO, dashboardDetailsJsonObject.optString("photo", ""));
//    cv.put(SHIFT_START, dashboardDetailsJsonObject.optString("shift_start", ""));
//    cv.put(SHIFT_END, dashboardDetailsJsonObject.optString("shift_end", ""));
//    cv.put(TEAM, dashboardDetailsJsonObject.optString("team", ""));
//    cv.put(POSITION, dashboardDetailsJsonObject.optString("position", ""));
//    cv.put(STATUS, dashboardDetailsJsonObject.optString("status", ""));
//    cv.put(FAVORITE, dashboardDetailsJsonObject.optString("favorite", ""));
//    cv.put(LOGIN, dashboardDetailsJsonObject.optString("login", ""));
//    metricsJsonArray = dashboardDetailsJsonObject.getJSONArray("metrics");

    public void setData(String ID, String STF_ID, String USERNAME, String NAME,
                        String PHOTO, String SHIFT_START, String SHIFT_END, String TEAM,
                        String POSITION, String STATUS, String FAVORITE, String LOGIN,
                        String IMAGE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(this.ID, ID);
        cv.put(this.STAFF_ID, STF_ID);
        cv.put(this.USERNAME, USERNAME);
        cv.put(this.NAME, NAME);
        cv.put(this.PHOTO, PHOTO);
        cv.put(this.SHIFT_START, SHIFT_START);
        cv.put(this.SHIFT_END, SHIFT_END);
        cv.put(this.TEAM, TEAM);
        cv.put(this.POSITION, POSITION);
        cv.put(this.STATUS, STATUS);
        cv.put(this.FAVORITE, FAVORITE);
        cv.put(this.LOGIN, LOGIN);
        cv.put(this.IMAGE, IMAGE);
        db.insert(TABLE_STAFF_DETAILS, null, cv);
    }

    public void setMetrics(String STF_ID, String TITLE, String DAILY, String WEEKLY, String VALUE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.putNull(ID);
        cv.put(this.STAFF_ID, STF_ID);
        cv.put(this.TITLE, TITLE);
        cv.put(this.DAILY, DAILY);
        cv.put(this.WEEKLY, WEEKLY);
        cv.put(this.VALUE, VALUE);
        db.insert(TABLE_METRICS, null, cv);
    }

    public void setWorking(String STF_ID, String TASK, String DATE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.putNull(ID);
        cv.put(this.STAFF_ID, STF_ID);
        cv.put(this.TASK, TASK);
        cv.put(this.DATE, DATE);
        db.insert(TABLE_WORKING, null, cv);
    }

    public void setStaffFavorite(String id, String favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateFavorite = "UPDATE " + TABLE_STAFF_DETAILS + " SET "
                + FAVORITE + "='" + favorite + "' WHERE " + STAFF_ID + " = '" + id + "'";
        db.execSQL(updateFavorite);
        db.close();
    }

    public void setData2(JSONObject data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            JSONArray myTeamJsonArray = data.getJSONArray("myTeam");
            JSONObject dashboardDetailsJsonObject = null;
            JSONArray metricsJsonArray = null;
            JSONObject metricsObject = null;
            ContentValues cv = new ContentValues();
            for (int i = 0; i < myTeamJsonArray.length(); i++) {
                dashboardDetailsJsonObject = myTeamJsonArray.getJSONObject(i);
                cv.put(ID, dashboardDetailsJsonObject.optString("id", ""));
                cv.put(STAFF_ID, dashboardDetailsJsonObject.optString("stf_id", ""));
                cv.put(USERNAME, dashboardDetailsJsonObject.optString("username", ""));
                cv.put(NAME, dashboardDetailsJsonObject.optString("name", ""));
                cv.put(PHOTO, dashboardDetailsJsonObject.optString("photo", ""));
                cv.put(SHIFT_START, dashboardDetailsJsonObject.optString("shift_start", ""));
                cv.put(SHIFT_END, dashboardDetailsJsonObject.optString("shift_end", ""));
                cv.put(TEAM, dashboardDetailsJsonObject.optString("team", ""));
                cv.put(POSITION, dashboardDetailsJsonObject.optString("position", ""));
                cv.put(STATUS, dashboardDetailsJsonObject.optString("status", ""));
                cv.put(FAVORITE, dashboardDetailsJsonObject.optString("favorite", ""));
                cv.put(LOGIN, dashboardDetailsJsonObject.optString("login", ""));
                metricsJsonArray = dashboardDetailsJsonObject.getJSONArray("metrics");
                ContentValues cv2 = new ContentValues();
                for (int j = 0; j < metricsJsonArray.length(); j++) {
                    metricsObject = metricsJsonArray.getJSONObject(j);
                    cv2.putNull(ID);
                    cv2.put(STAFF_ID, dashboardDetailsJsonObject.optString("stf_id", ""));
                    cv2.put(TITLE, metricsObject.optString("title", ""));
                    cv2.put(DAILY, metricsObject.optString("daily", ""));
                    cv2.put(WEEKLY, metricsObject.optString("weekly", ""));
                    cv2.put(VALUE, metricsObject.optString("value", ""));
                    db.insert(TABLE_METRICS, null, cv2);
                }
                db.insert(TABLE_STAFF_DETAILS, null, cv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }

}
