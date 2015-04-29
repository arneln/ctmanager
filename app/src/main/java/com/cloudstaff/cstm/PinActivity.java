package com.cloudstaff.cstm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudstaff.cstm.utils.Dialog;
import com.cloudstaff.cstm.utils.SharedPreference;

public class PinActivity extends Activity {
    private EditText pin1;
    private EditText pin2;
    private EditText pin3;
    private EditText pin4;
    private Button btnSubmit;
    private StringBuilder sb1;
    private StringBuilder sb2;
    private StringBuilder sb3;
    private StringBuilder sb4;
    private SharedPreference mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        initVariables();
        pin1.requestFocus();
        pin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb1.length() == 1) {
                    sb1.deleteCharAt(0);
                }
                pin1.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb1.length() == 0 && pin1.length() == 1) {
                    sb1.append(s);

                    pin1.clearFocus();
                    pin2.requestFocus();
                    pin2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (sb1.length() == 0) {
//                    pin1.requestFocus();
//                }
            }
        });

        pin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb1.length() == 1) {
                    sb1.deleteCharAt(0);
                }
                pin2.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb1.length() == 0 && pin2.length() == 1) {
                    sb1.append(s);
                    pin2.clearFocus();
                    pin3.requestFocus();
                    pin3.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (sb1.length() == 0) {
//
//                    pin1.requestFocus();
//                }
            }
        });

        pin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb1.length() == 1) {
                    sb1.deleteCharAt(0);
                }
                pin3.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb1.length() == 0 && pin3.length() == 1) {
                    sb1.append(s);
                    pin3.clearFocus();
                    pin4.requestFocus();
                    pin4.setCursorVisible(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (sb1.length() == 0) {
//                    pin2.requestFocus();
//                }
            }
        });

        pin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (sb1.length() == 1) {
                    sb1.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb1.length() == 0 && pin4.length() == 1) {
                    sb1.append(s);
                    pin3.clearFocus();
                    pin4.requestFocus();
                    pin4.setCursorVisible(true);
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                pin4.clearFocus();
                if (sb1.length() == 0) {
//                    pin3.requestFocus();
                    btnSubmit.setVisibility(View.GONE);
                }
            }
        });

//        pin2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d("test","has: "+hasFocus);
//                if (!hasFocus){
//                    pin2.clearFocus();
//                }
//            }
//        });

//        pin2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                pin2.clearFocus();
//            }
//        });


//        pin1.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    pin1.clearFocus();
//                    pin1.requestFocus();
//                    pin1.getText().clear();
//                }
//                return false;
//            }
//        });
//        pin2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    pin2.clearFocus();
//                    pin1.requestFocus();
//                    pin2.getText().clear();
//                }
//                return false;
//            }
//        });
//        pin3.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    pin3.clearFocus();
//                    pin2.requestFocus();
//                    pin3.getText().clear();
//                }
//                return false;
//            }
//        });
//        pin4.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d("keycode", "" + event);
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    pin4.clearFocus();
//                    pin3.requestFocus();
//                    pin4.getText().clear();
//                }
//                return false;
//            }
//        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pin = pin1.getText().toString() + pin2.getText().toString() +
                        pin3.getText().toString() + pin4.getText().toString();
                if (pin.equalsIgnoreCase("1234")) {
                    mSharedPreference.createLoginSession("yung_email_ng_user");
                    PinActivity.this.startActivity(
                            new Intent(PinActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    PinActivity.this.finish();
                } else {
                    Dialog.showSingleOptionDialog(PinActivity.this, "",
                            getString(R.string.pin_incorrect), getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    pin1.getText().clear();
                                    pin2.getText().clear();
                                    pin3.getText().clear();
                                    pin4.getText().clear();
                                    pin1.requestFocus();
                                }
                            }, false);
                }
            }
        });
    }

    private void initVariables() {
        pin1 = (EditText) findViewById(R.id.pin1);
        pin2 = (EditText) findViewById(R.id.pin2);
        pin3 = (EditText) findViewById(R.id.pin3);
        pin4 = (EditText) findViewById(R.id.pin4);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        sb1 = new StringBuilder();
        sb2 = new StringBuilder();
        sb3 = new StringBuilder();
        sb4 = new StringBuilder();
        mSharedPreference = new SharedPreference(PinActivity.this);
    }
}
