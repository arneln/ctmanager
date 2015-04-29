package com.cloudstaff.cstm.model;


public class MyTeamModel {

    public String NickN, FullName, Shift, Team, Position, Status, ShiftStart,ShiftEnd, TeamTrack, PositionTrack, StatusTrack;
//    public int MTPhoto;
    public String MTPhoto;
    public int Heart;
    public int Msg;
    public int Bell;
    public int DotDot;
    public Boolean myfavorite;
    public Boolean OnOffLine;
    public MyTeamModel(String NickN, String FullName,  String ShiftStart,String ShiftEnd, String TeamTrack, String PositionTrack,
                       String StatusTrack, String MTPhoto,Boolean myfavorite,Boolean OnOffLine ) {
        this.NickN = NickN;
        this.FullName = FullName;
//        this.Shift = Shift;
//        this.Team = Team;
//        this.Position = Position;
//        this.Status = Status;
        this.ShiftStart = ShiftStart;
        this.TeamTrack = TeamTrack;
        this.PositionTrack = PositionTrack;
        this.StatusTrack = StatusTrack;
//        this.MTPhoto = MTPhoto;
        this.MTPhoto = MTPhoto;
        this.ShiftEnd = ShiftEnd;
//        this.Heart = Heart;
//        this.Msg = Msg;
//        this.Bell = Bell;
//        this.DotDot = DotDot;
        this.myfavorite = myfavorite;
        this.OnOffLine = OnOffLine;
    }


    public String getNickN() {
        return this.NickN;
    }

    public String getFullName() {
        return this.FullName;
    }

//    public String getShift() {
//        return this.Shift;
//    }

//    public String getTeam() {
//        return this.Team;
//    }

//    public String getPosition() {
//        return this.Position;
//    }

//    public String getStatus() {
//        return this.Status;
//    }

    public String getShiftStart() {
        return this.ShiftStart;
    }

    public String getShiftEnd(){
        return this.ShiftEnd;
    }

    public String getTeamTrack() {
        return this.TeamTrack;
    }

    public String getPositionTrack() {
        return this.PositionTrack;
    }

    public String getStatusTrack() {
        return this.StatusTrack;
    }
    public String getMTPhoto() {
        return this.MTPhoto;
    }

//    public int getMTPhoto() {
//        return this.MTPhoto;
//    }



//    public int getHeart() {
//        return this.Heart;
//    }
//
//    public int getMsg() {
//        return this.Msg;
//    }
//
//    public int getBell() {
//        return this.Bell;
//    }
//
//    public int getDotDot() {
//        return this.DotDot;
//    }
    public Boolean getmyfavorite(){
        return this.myfavorite;
    }
    public Boolean getOnOffLine(){
        return this.OnOffLine;
    }


}
