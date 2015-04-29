package com.cloudstaff.cstm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cloudstaff.cstm.R;
import com.cloudstaff.cstm.adapter.MyTeamAdapter;
import com.cloudstaff.cstm.model.MyTeam;
import com.cloudstaff.cstm.utils.Database;
import com.cloudstaff.cstm.utils.ProfileActivity;

import java.util.ArrayList;

public class MyTeamFragment extends android.app.Fragment {
    ListView listView;
    Spinner spinnerStatus;
    Spinner spinner;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<String> spinnerStatusArrayAdapter;
    //    private ProgressDialog mProgressDialog;
//    private ArrayList<String> imageLinks = new ArrayList<String>();
//    private ArrayList<String> imageLinkList = new ArrayList<String>();
    private ArrayList<MyTeam> myTeamArrayList = new ArrayList<>();
    private Database mDatabase;
    private ArrayList<String> spinnerString = new ArrayList<>();
    private String status = "";
    private String stat = "";
    private String team = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myteam, container, false);
        listView = (ListView) view.findViewById(R.id.MyTeamListView);
        mDatabase = new Database(getActivity());

        myTeamArrayList = mDatabase.getData();
        spinnerString = mDatabase.getSpinnerData();
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
//        GetData getData = new GetData(getActivity());
//
//        getData.execute();

//        MyTeamModelItems[0] = new MyTeamModel("ElvinD", "EL-Vine Del aCross", "7 00am","4 00pm", "Over All Kahit Saan", "Cat Two Long", "Chat That Boooooty!!!", R.drawable.elvind, false, true);
//        MyTeamModelItems[1] = new MyTeamModel("Toe Terp", "Kristo Terp", "6h 30am","4 00pm", "Mah Lee Boys", "Dug Style", "Yosi Break", R.drawable.christoperc, true, false);
//        MyTeamModelItems[2] = new MyTeamModel("Ren Zhey", "Renz De Real", "7h 00am","4 00pm", "Bhews", "Guh Ward Diya", "Look Out sa mga Parating na HR", R.drawable.renzs, false, true);
//        MyTeamModelItems[3] = new MyTeamModel("Mokz Wun", "Arnel NoKey", "6 30am","4 00pm", "The Radio DJ", "Play Song & Stuff", "Code Code Code", R.drawable.arneln, true, false);
//        MyTeamModelItems[4] = new MyTeamModel("Ch3w", "Madam Ritcheld Abenson", "6 30am","4 00pm", "Mobile Team", "Mother Superior", "Working on CSTM", R.drawable.richeldav, true, true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                if (view == null)
//                    view = inflater.inflate(R.layout.myteamadapter, null);
//                String MyTeamName = String.valueOf(((TextView) view.findViewById(R.id.MyTeamName)).getText());
//                String MyTeamFullN = String.valueOf(((TextView) view.findViewById(R.id.MyTeamFullN)).getText());
//                String MyTeamSSTrack = String.valueOf(((TextView) view.findViewById(R.id.MyTeamSSTrack)).getText());
//                String MyTeamSETrack = String.valueOf(((TextView) view.findViewById(R.id.MyTeamSETrack)).getText());
//                String MyTeamTeamTrack = String.valueOf(((TextView) view.findViewById(R.id.MyTeamTeamTrack)).getText());
//                String MyTeamPosTrack = String.valueOf(((TextView) view.findViewById(R.id.MyTeamPosTrack)).getText());
//                String MyTeamStatusTrack = String.valueOf(((TextView) view.findViewById(R.id.MyTeamStatusTrack)).getText());
//                ImageView MyTeamPhoto = (ImageView) view.findViewById(R.id.MyTeamPhoto);
//                ImageView MyTeamCircle = (ImageView) view.findViewById(R.id.MyTeamCircle);
//                ImageView MyTeamHeart = (ImageView) view.findViewById(R.id.MyTeamHeart);
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("StaffID", myTeamArrayList.get(position).getStf_id());
//                i.putExtra("MyTeamName", MyTeamName);
//                i.putExtra("MyTeamFullN", MyTeamFullN);
//                i.putExtra("MyTeamSSTrack", MyTeamSSTrack);
//                i.putExtra("MyTeamSETrack", MyTeamSETrack);
//                i.putExtra("MyTeamTeamTrack", MyTeamTeamTrack);
//                i.putExtra("MyTeamPosTrack", MyTeamPosTrack);
//                i.putExtra("MyTeamStatusTrack", MyTeamStatusTrack);
//                MyTeamPhoto.buildDrawingCache();
//                Bitmap b = MyTeamPhoto.getDrawingCache();
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.PNG, 50, bs);
//                i.putExtra("byteArray", bs.toByteArray());
////                i.putExtra("position",position);
////                i.putExtra("MyTeamPhoto", b);
//                if (MyTeamCircle.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.mipmap.online_list).getConstantState())) {
//                    i.putExtra("MyTeamCircle", true);
//                } else if (MyTeamCircle.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.mipmap.offline_list).getConstantState())) {
//                    i.putExtra("MyTeamCircle", false);
//                }
//
//                if (MyTeamHeart.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.drawable.icon_favourite).getConstantState())) {
//                    i.putExtra("MyTeamHeart", true);
//                } else if (MyTeamHeart.getDrawable().getConstantState().equals
//                        (getResources().getDrawable(R.drawable.icon_unfavourite).getConstantState())) {
//                    i.putExtra("MyTeamHeart", false);
//                }

                startActivity(i);

            }
        });

        spinner = (Spinner) view.findViewById(R.id.DepartMent);
        spinnerArrayAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spincolor, spinnerString);//array
        spinnerArrayAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("item", "" + parent.getItemAtPosition(position));
                team = parent.getItemAtPosition(position).toString();
                if (stat.equalsIgnoreCase("online") || stat.equalsIgnoreCase("offline")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (stat.equalsIgnoreCase("assigned") || stat.equalsIgnoreCase("unassigned")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, stat);
                    }
                } else if (stat.equalsIgnoreCase("favorite")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllFavorites("yes");
                    } else {
                        updateListFavorites(team, "yes");
                    }
                } else if (stat.equalsIgnoreCase("none")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAll();
                    } else {
                        updateListAllWithTeam(team);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStatus = (Spinner) view.findViewById(R.id.Status);
        spinnerStatusArrayAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spincolor2, getResources().getStringArray(R.array.Status));
        spinnerStatusArrayAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusArrayAdapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("status", "" + parent.getItemAtPosition(position));
                status = parent.getItemAtPosition(position).toString();
                if (status.equalsIgnoreCase("Online Staffs")) {
                    stat = "online";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (status.equalsIgnoreCase("Offline Staffs")) {
                    stat = "offline";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (status.equalsIgnoreCase("Assigned Staffs")) {
                    stat = "assigned";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, "assigned");
                    }

                } else if (status.equalsIgnoreCase("Unassigned Staffs")) {
                    stat = "unassigned";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, "unassigned");
                    }

                } else if (status.equalsIgnoreCase("My Favorites")) {
                    stat = "favorite";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllFavorites("yes");
                    } else {
                        updateListFavorites(team, "yes");
                    }
                } else if (status.equalsIgnoreCase("None")) {
                    stat = "none";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAll();
                    } else {
                        updateListAllWithTeam(team);
                    }
                }
//                if (team.equalsIgnoreCase("all")) {
//                    //all offline, online
//                    updateListAllWithStatus(status);
//                }
//                status = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
        //48
        //72
        //96
        //144
    }

    @Override
    public void onStart() {
        super.onStart();
        myTeamArrayList = mDatabase.getData();

        spinnerArrayAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spincolor, spinnerString);//array
        spinnerArrayAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("item", "" + parent.getItemAtPosition(position));
                team = parent.getItemAtPosition(position).toString();
                if (stat.equalsIgnoreCase("online") || stat.equalsIgnoreCase("offline")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (stat.equalsIgnoreCase("assigned") || stat.equalsIgnoreCase("unassigned")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, stat);
                    }
                } else if (stat.equalsIgnoreCase("favorite")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllFavorites("yes");
                    } else {
                        updateListFavorites(team, "yes");
                    }
                } else if (stat.equalsIgnoreCase("none")) {
                    if (team.equalsIgnoreCase("all")) {
                        updateListAll();
                    } else {
                        updateListAllWithTeam(team);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStatusArrayAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spincolor2, getResources().getStringArray(R.array.Status));
        spinnerStatusArrayAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusArrayAdapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("status", "" + parent.getItemAtPosition(position));
                status = parent.getItemAtPosition(position).toString();
                if (status.equalsIgnoreCase("Online Staffs")) {
                    stat = "online";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (status.equalsIgnoreCase("Offline Staffs")) {
                    stat = "offline";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithLogin(stat);
                    } else {
                        updateListOnline(team, stat);
                    }
                } else if (status.equalsIgnoreCase("Assigned Staffs")) {
                    stat = "assigned";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, "assigned");
                    }

                } else if (status.equalsIgnoreCase("Unassigned Staffs")) {
                    stat = "unassigned";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllWithStatus(stat);
                    } else {
                        updateListStatus(team, "unassigned");
                    }

                } else if (status.equalsIgnoreCase("My Favorites")) {
                    stat = "favorite";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAllFavorites("yes");
                    } else {
                        updateListFavorites(team, "yes");
                    }
                } else if (status.equalsIgnoreCase("None")) {
                    stat = "none";
                    if (team.equalsIgnoreCase("all")) {
                        updateListAll();
                    } else {
                        updateListAllWithTeam(team);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateListAll() {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getAllStaffNoStatus();
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListAllWithLogin(String status) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getAllStaffLogin(status);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListAllWithStatus(String status) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getAllStaffStatus(status);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListAllWithTeam(String team) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getAllStaffTeam(team);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListStatus(String team, String status) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getStaffByTeamStatus(team, status);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListOnline(String team, String status) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getStaffByTeamOnline(team, status);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListAllFavorites(String favorite) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getAllStaffByFavorites(favorite);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

    public void updateListFavorites(String team, String favorite) {
        myTeamArrayList = new ArrayList<>();
        myTeamArrayList = mDatabase.getStaffByFavorites(team, favorite);
        MyTeamAdapter adapters = new MyTeamAdapter(getActivity(), myTeamArrayList);
        listView.setAdapter(adapters);
    }

}
