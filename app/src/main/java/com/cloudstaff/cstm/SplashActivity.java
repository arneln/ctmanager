package com.cloudstaff.cstm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudstaff.cstm.utils.InternetChecker;
import com.cloudstaff.cstm.utils.SharedPreference;


public class SplashActivity extends Activity {
    private SharedPreference mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPreference = new SharedPreference(SplashActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (InternetChecker.isNetworkAvailable(SplashActivity.this)) {
                    mSharedPreference.checkLogin();
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    showDialog();
                }
            }
        }, 3000);
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.not_connected_internet));
        builder.setMessage(getString(R.string.not_connected_internet_details));

        builder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setOnCancelListener(null);
        builder.show();

//        Dialog.showDialog(SplashActivity.this,
//                getString(R.string.not_connected_internet),
//                getString(R.string.not_connected_internet_details),
//                getString(R.string.exit),
//                getString(R.string.go_to_settings),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        dialog.cancel();
//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    }
//                }, true);
//                mSharedPreference.checkLogin();
//                finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_splash);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        animation.reset();
        ImageView imageView = (ImageView) findViewById(R.id.iv_splash);
        imageView.clearAnimation();
        imageView.startAnimation(animation);
    }
}
