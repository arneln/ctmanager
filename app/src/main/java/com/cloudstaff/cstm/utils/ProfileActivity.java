package com.cloudstaff.cstm.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudstaff.cstm.MainActivity;
import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.WhatRYouWorkingOn;
import com.cloudstaff.cstm.model.MyTeam;
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


public class ProfileActivity extends Activity {
    ArrayList<MyTeam> myTeamArrayList;
    ImageView ProfilePhoto;
    ImageView PhotoCircle;
    TextView ProfileName;
    TextView ProfileFullN;
    TextView ProfileShift;
    TextView ProfileSSTrack;
    TextView ProfileSETrack;
    TextView ProfileTeamTrack;
    TextView ProfilePosTrack;
    TextView ProfileStatusTrack;
    ImageView ProfileSendping;
    ImageView ProfileMessage;
    ImageView ProfileReport;
    ImageView ProfileMetrics;
    ImageView ProfileFavorite;
    ListView StaffDetailsModelList;
    LinearLayout SndPng;
    LinearLayout SndMsg;
    LinearLayout SndRep;
    LinearLayout SndMet;
    LinearLayout SndFav;
    SharedPreference mPreference;
    AndroidCodes mAndroidCodes;
    String defaultPing;
    String staffId;
    boolean OnlineOffline;
    String Heart;
    Database mDatabase;
    String pingMessage;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staffdetails);
        ProfilePhoto = (ImageView) findViewById(R.id.ProfilePhoto);
        PhotoCircle = (ImageView) findViewById(R.id.PhotoCircle);
        ProfileSendping = (ImageView) findViewById(R.id.ProfileSendping);
        ProfileMessage = (ImageView) findViewById(R.id.ProfileMessage);
        ProfileReport = (ImageView) findViewById(R.id.ProfileReport);
        ProfileMetrics = (ImageView) findViewById(R.id.ProfileMetrics);
        ProfileFavorite = (ImageView) findViewById(R.id.ProfileFavorite);
        ProfileName = (TextView) findViewById(R.id.ProfileName);
        ProfileFullN = (TextView) findViewById(R.id.ProfileFullN);
        ProfileSSTrack = (TextView) findViewById(R.id.ProfileSSTrack);
        ProfileSETrack = (TextView) findViewById(R.id.ProfileSETrack);
        ProfileTeamTrack = (TextView) findViewById(R.id.ProfileTeamTrack);
        ProfilePosTrack = (TextView) findViewById(R.id.ProfilePosTrack);
        ProfileStatusTrack = (TextView) findViewById(R.id.ProfileStatusTrack);
        SndPng = (LinearLayout) findViewById(R.id.SndPng);
        SndMsg = (LinearLayout) findViewById(R.id.SndMsg);
        SndRep = (LinearLayout) findViewById(R.id.SndRep);
        SndMet = (LinearLayout) findViewById(R.id.SndMet);
        SndFav = (LinearLayout) findViewById(R.id.SndFav);
        myTeamArrayList = new ArrayList<>();
        mPreference = new SharedPreference(this);
        mDatabase = new Database(this);
        mAndroidCodes = new AndroidCodes(this);
        defaultPing = mPreference.getDefaultPing();

        Intent intent = getIntent();
        staffId = intent.getStringExtra("StaffID");
        myTeamArrayList = mDatabase.getDataById(staffId);

//            int itemValue = (int) intent.getIntExtra("itemValue", 0);
//            String ProfileNames = (String) intent.getStringExtra("MyTeamName");
        ProfileName.setText(myTeamArrayList.get(0).getUsername());
//            String ProfileFullNs = (String) intent.getStringExtra("MyTeamFullN");
        ProfileFullN.setText(myTeamArrayList.get(0).getName());
//            String ProfileShifts = (String) bundle.get("MyTeamName");
//            ProfileShift.setText(ProfileShifts);
//            String ProfileSETracks = (String) intent.getStringExtra("MyTeamSETrack");
        ProfileSETrack.setText(myTeamArrayList.get(0).getShift_end());
//            String ProfileSSTracks = (String) intent.getStringExtra("MyTeamSSTrack");
        ProfileSSTrack.setText(myTeamArrayList.get(0).getShift_start());
//            String ProfileTeamTracks = (String) intent.getStringExtra("MyTeamTeamTrack");
        ProfileTeamTrack.setText(myTeamArrayList.get(0).getTeam());
//            String ProfilePosTracks = (String) intent.getStringExtra("MyTeamPosTrack");
        ProfilePosTrack.setText(myTeamArrayList.get(0).getPosition());
//            String ProfileStatusTracks = (String) intent.getStringExtra("MyTeamStatusTrack");
        ProfileStatusTrack.setText(myTeamArrayList.get(0).getStatus());

//        Bitmap b = BitmapFactory.decodeByteArray(
//                getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);

        Picasso.with(this)
                .load(myTeamArrayList.get(0).getPhoto())
                .placeholder(R.mipmap.unknown)
                .error(R.mipmap.unknown)
//                        .transform(new CircleTransform(getActivity(), color))
                .resize(150, 150)
                .centerInside()
                .into(ProfilePhoto);

//            ProfilePhoto.setImageBitmap(b);
//            staffId = intent.getStringExtra("StaffID");
        Log.d("login", "" + myTeamArrayList.get(0).getLogin());
        Log.d("favorite", "" + myTeamArrayList.get(0).getFavorite());
        if (myTeamArrayList.get(0).getLogin().equalsIgnoreCase("online")) {
            PhotoCircle.setImageResource(R.mipmap.online_list);
        } else {
            PhotoCircle.setImageResource(R.mipmap.offline_list);
        }
        if (myTeamArrayList.get(0).getFavorite().equalsIgnoreCase("yes")) {
            ProfileFavorite.setImageResource(R.drawable.icon_favourite);
        } else {
            ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
        }
//        OnlineOffline = Boolean.parseBoolean(myTeamArrayList.get(0).getLogin());
        Heart = myTeamArrayList.get(0).getFavorite();
////            boolean OnlineOffline = getIntent().getExtras().getBoolean("MyTeamCircle");
//        if (OnlineOffline) {
//            PhotoCircle.setImageResource(R.mipmap.online_list);
////                Toast.makeText(ProfileActivity.this, "TU RU RUUUUUUUUUU", Toast.LENGTH_LONG).show();
//        } else {
//            PhotoCircle.setImageResource(R.mipmap.offline_list);
////                Toast.makeText(ProfileActivity.this, "Nuuuu Nuuuu Nuuuuuuuuuuuuuu", Toast.LENGTH_LONG).show();
//        }
////            boolean Heart = getIntent().getExtras().getBoolean("MyTeamHeart");
//        if (Heart) {
//            ProfileFavorite.setImageResource(R.drawable.icon_favourite);
////                Toast.makeText(ProfileActivity.this, "TU RU RUUUUUUUUUU", Toast.LENGTH_LONG).show();
//        } else {
//            ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
////                Toast.makeText(ProfileActivity.this, "Nuuuu Nuuuu Nuuuuuuuuuuuuuu", Toast.LENGTH_LONG).show();
//        }

//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            int itemValue = (int) intent.getIntExtra("itemValue", 0);
//            String ProfileNames = (String) intent.getStringExtra("MyTeamName");
//            ProfileName.setText(ProfileNames);
//            String ProfileFullNs = (String) intent.getStringExtra("MyTeamFullN");
//            ProfileFullN.setText(ProfileFullNs);
////            String ProfileShifts = (String) bundle.get("MyTeamName");
////            ProfileShift.setText(ProfileShifts);
//            String ProfileSETracks = (String) intent.getStringExtra("MyTeamSETrack");
//            ProfileSETrack.setText(ProfileSETracks);
//            String ProfileSSTracks = (String) intent.getStringExtra("MyTeamSSTrack");
//            ProfileSSTrack.setText(ProfileSSTracks);
//            String ProfileTeamTracks = (String) intent.getStringExtra("MyTeamTeamTrack");
//            ProfileTeamTrack.setText(ProfileTeamTracks);
//            String ProfilePosTracks = (String) intent.getStringExtra("MyTeamPosTrack");
//            ProfilePosTrack.setText(ProfilePosTracks);
//            String ProfileStatusTracks = (String) intent.getStringExtra("MyTeamStatusTrack");
//            ProfileStatusTrack.setText(ProfileStatusTracks);
//            Bitmap b = BitmapFactory.decodeByteArray(
//                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
//            ProfilePhoto.setImageBitmap(b);
//            staffId = intent.getStringExtra("StaffID");
//            OnlineOffline = intent.getBooleanExtra("MyTeamCircle", false);
//            Heart = intent.getBooleanExtra("MyTeamHeart", false);
////            boolean OnlineOffline = getIntent().getExtras().getBoolean("MyTeamCircle");
//            if (OnlineOffline) {
//                PhotoCircle.setImageResource(R.mipmap.online_list);
////                Toast.makeText(ProfileActivity.this, "TU RU RUUUUUUUUUU", Toast.LENGTH_LONG).show();
//            } else {
//                PhotoCircle.setImageResource(R.mipmap.offline_list);
////                Toast.makeText(ProfileActivity.this, "Nuuuu Nuuuu Nuuuuuuuuuuuuuu", Toast.LENGTH_LONG).show();
//            }
////            boolean Heart = getIntent().getExtras().getBoolean("MyTeamHeart");
//            if (Heart) {
//                ProfileFavorite.setImageResource(R.drawable.icon_favourite);
////                Toast.makeText(ProfileActivity.this, "TU RU RUUUUUUUUUU", Toast.LENGTH_LONG).show();
//            } else {
//                ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
////                Toast.makeText(ProfileActivity.this, "Nuuuu Nuuuu Nuuuuuuuuuuuuuu", Toast.LENGTH_LONG).show();
//            }
//
//        }

//        i.putExtra("MyTeamName", MyTeamName );
//        i.putExtra("MyTeamFullN", MyTeamFullN );
//        i.putExtra("MyTeamSETrack", MyTeamSETrack );
//        i.putExtra("MyTeamTeamTrack", MyTeamTeamTrack );
//        i.putExtra("MyTeamPosTrack", MyTeamPosTrack );
//        i.putExtra("MyTeamStatusTrack", MyTeamStatusTrack );


        SndPng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogping);
                final EditText message = (EditText) dialog.findViewById(R.id.PingMsg);
                Button sendping = (Button) dialog.findViewById(R.id.sendping);
                Button cancelping = (Button) dialog.findViewById(R.id.cancelping);
                message.setText(defaultPing);
                sendping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        pingMessage = message.getText().toString();
                        Log.d("sample", "" + message.getText().toString());
                    }
                });
                cancelping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        SndMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
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
                        Toast.makeText(ProfileActivity.this, "Message Has Been Sent", Toast.LENGTH_LONG).show();
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
//                Ibalik Mo ito para sa Pag lipat
//                ProfileActivity.this.startActivity(
//                        new Intent(ProfileActivity.this, SendMessage.class)
//                                .putExtra("STAFF_ID", myTeamArrayList.get(0).getStf_id())
//                                .putExtra("STAFF_NAME", myTeamArrayList.get(0).getName()));
            }
        });

        SndMet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                i.putExtra("a", staffId);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
//                DashboardFragment fragment = (DashboardFragment)
//                        getFragmentManager().findFragmentById(R.id.frame_container);
//                Log.d("staffId", "" + staffId);
//                fragment.showMetrics(Integer.parseInt(staffId));

            }
        });
        SndRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Profile act", "" + staffId);
                ProfileActivity.this.startActivity(
                        new Intent(ProfileActivity.this, WhatRYouWorkingOn.class)
                                .putExtra("STAFFID", staffId));
            }
        });

        SndFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set favorite/unfavorite
                if (Heart.equalsIgnoreCase("Yes")) {
                    mDatabase.setStaffFavorite(staffId, "No");
                    myTeamArrayList.get(0).setFavorite("No");
                    ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
                    Heart = "No";
                } else {
                    mDatabase.setStaffFavorite(staffId, "Yes");
                    myTeamArrayList.get(0).setFavorite("Yes");
                    ProfileFavorite.setImageResource(R.drawable.icon_favourite);
                    Heart = "Yes";
                }
            }
        });

        ProfileFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Heart.equalsIgnoreCase("Yes")) {
                    mDatabase.setStaffFavorite(staffId, "No");
                    myTeamArrayList.get(0).setFavorite("No");
                    ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
                    Heart = "No";
                } else {
                    mDatabase.setStaffFavorite(staffId, "Yes");
                    myTeamArrayList.get(0).setFavorite("Yes");
                    ProfileFavorite.setImageResource(R.drawable.icon_favourite);
                    Heart = "Yes";
                }
            }
        });

        ProfileSendping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogping);
                final EditText message = (EditText) dialog.findViewById(R.id.PingMsg);
                Button sendping = (Button) dialog.findViewById(R.id.sendping);
                Button cancelping = (Button) dialog.findViewById(R.id.cancelping);
                message.setText(defaultPing);
                sendping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Message Has Been Sent", Toast.LENGTH_LONG).show();
                        Log.d("sample", "" + message.getText().toString());
                    }
                });
                cancelping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        ProfileMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
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

        ProfileReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Profile act", "" + staffId);
                ProfileActivity.this.startActivity(
                        new Intent(ProfileActivity.this, WhatRYouWorkingOn.class)
                                .putExtra("STAFFID", staffId));
//                final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialogwhatwork);
//                Button WhatWorkSend = (Button) dialog.findViewById(R.id.WhatWorkSend);
//                Button WhatWorkCanCel = (Button) dialog.findViewById(R.id.WhatWorkCanCel);
//                WhatWorkSend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(ProfileActivity.this, "Send - That -Tat- Tant- Tant- Tanant!", Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                WhatWorkCanCel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(ProfileActivity.this, "What The Pucks Says", Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
            }
        });
//        ProfileFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ProfileFavorite.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.drawable.icon_unfavourite).getConstantState())) {
//                    ProfileFavorite.setImageResource(R.drawable.icon_favourite);
//                    mDatabase.setStaffFavorite(staffId, "Yes");
//                } else if (ProfileFavorite.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.drawable.icon_favourite).getConstantState())) {
//                    ProfileFavorite.setImageResource(R.drawable.icon_unfavourite);
//                    mDatabase.setStaffFavorite(staffId, "No");
//                }
//            }
//        });


        BitmapDrawable drawable = (BitmapDrawable) ProfilePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
//        MyTeamPhoto.setBackground(getContext().getRe0sources().getDrawable(R.drawable.circle2));
        ProfilePhoto.setImageBitmap(circleBitmap);
    }

    public void editActions(View v) {
        final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogping);
        Button sendping = (Button) dialog.findViewById(R.id.sendping);
        Button cancelping = (Button) dialog.findViewById(R.id.cancelping);
        sendping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "Send - That -Tat- Tant- Tant- Tanant!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        cancelping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "What The Pucks Says", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void editActionss(View v) {
        final android.app.Dialog dialog = new android.app.Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogmessage);
        Button sendmsg = (Button) dialog.findViewById(R.id.sendmsg);
        Button cancelmsg = (Button) dialog.findViewById(R.id.cancelmsg);
        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "Send - That -Tat- Tant- Tant- Tanant!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        cancelmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "What The Pucks Says", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
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
                        myTeamArrayList.get(0).getStf_id()));
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
                        myTeamArrayList.get(0).getStf_id()));
                nameValuePairs.add(new BasicNameValuePair("favorite", Heart));
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
                        myTeamArrayList.get(0).setFavorite(Heart);
                        mDatabase.setStaffFavorite(myTeamArrayList.get(0).getStf_id(),
                                Heart);
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
