package com.cloudstaff.cstm;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cloudstaff.cstm.adapter.NavDrawerListAdapter;
import com.cloudstaff.cstm.fragment.DashboardFragment;
import com.cloudstaff.cstm.fragment.MyTeamFragment;
import com.cloudstaff.cstm.fragment.PingFragment;
import com.cloudstaff.cstm.fragment.SettingsFragment;
import com.cloudstaff.cstm.model.NavDrawerItem;
import com.cloudstaff.cstm.utils.AndroidCodes;
import com.cloudstaff.cstm.utils.AppUtils;
import com.cloudstaff.cstm.utils.Database;
import com.cloudstaff.cstm.utils.InternetChecker;
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

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private SharedPreference mPreference;
    private AndroidCodes mAndroidCodes;
    private Database mDatabase;

    private ProgressDialog mProgressDialog;
    private boolean isDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mPreference = new SharedPreference(MainActivity.this);
        mAndroidCodes = new AndroidCodes(MainActivity.this);
        mDatabase = new Database(MainActivity.this);
//        TODO
        Log.e("MainAct INTERNET!!!!", "" + InternetChecker.isNetworkAvailable(MainActivity.this));
        refreshMenu();

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
//        GetData getData = new GetData(MainActivity.this);
//        getData.execute();
//        Log.d("SHA1", "" + mAndroidCodes.SHA1("test"));
    }

    @Override
    public void onBackPressed() {
        if (isDashboard) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.exit_app)).setIcon(null);
            builder.setMessage(getString(R.string.exit_app_details));
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //implement your logic for YES
                    finish();
                }
            });
            builder.setNeutralButton("Log Out & Exit", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //implement your logic for NO

                    mPreference.logoutUser();
                    finish();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //implement your logic for NO
                    dialog.cancel();
                }
            });
            builder.setOnCancelListener(null);
            builder.show();
        } else {
            isDashboard = true;
            Fragment fragment = new DashboardFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(0, true);
            mDrawerList.setSelection(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.action_refresh:
//                Log.d("Hello", "testing!");
//                GetData getData = new GetData(MainActivity.this);
//                getData.execute();
//                return true;
//            default:
//        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                isDashboard = true;
                break;
            case 1:
                fragment = new MyTeamFragment();
                isDashboard = false;
                break;
            case 2:
                fragment = new PingFragment();
                isDashboard = false;
                break;
            case 3:
                fragment = new SettingsFragment();
                isDashboard = false;
                break;
            case 4:
                mPreference.logoutUser();
                mDatabase.cleanTable();
                MainActivity.this.finish();
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            fragmentTransaction
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void refreshMenu() {
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        // dashboard
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        // my team
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        // ping
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        // log out
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
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
                        mPreference.logoutUser();
                        mDatabase.cleanTable();
                        MainActivity.this.finish();
                        com.cloudstaff.cstm.utils.Dialog.showSingleOptionDialog(mContext,
                                mContext.getString(R.string.ping_success),
                                jsonObjectResult.getString("message"),
                                mContext.getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
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