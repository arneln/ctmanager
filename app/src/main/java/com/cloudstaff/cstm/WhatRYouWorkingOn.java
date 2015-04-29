package com.cloudstaff.cstm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.cloudstaff.cstm.adapter.WorkingAdapter;
import com.cloudstaff.cstm.model.Working;
import com.cloudstaff.cstm.utils.Database;

import java.util.ArrayList;

public class WhatRYouWorkingOn extends Activity {
    ListView WRURO;
    ArrayList<Working> workingArrayList;
    Database mDatabase;
    WorkingAdapter mWorkingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatareyouworkingon);
        mDatabase = new Database(this);
        workingArrayList = new ArrayList<>();

        WRURO = (ListView) findViewById(R.id.WRURO);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        Intent intent = getIntent();
        String id = intent.getStringExtra("STAFFID");
        workingArrayList = mDatabase.getWorkingData(id);
        mWorkingAdapter = new WorkingAdapter(this, workingArrayList);
        WRURO.setAdapter(mWorkingAdapter);

//        WRURO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // ListView Clicked item index
//                int itemPosition = position;
//
//                // ListView Clicked item value
//                String itemValue = (String) WRURO.getItemAtPosition(position);
//
//                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
//                        .show();
//
//            }
//
//        });

    }
}
