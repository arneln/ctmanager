package com.cloudstaff.cstm.model;

public class PingModel {

    public String name;
    public String pingposisyon;
    public Boolean value;
    public int pingphoto;
    public Boolean OnOffLinePing;
    public PingModel(String name,String pingposisyon, Boolean value,int pingphoto, Boolean OnOffLinePing) {
        this.name = name;
        this.pingposisyon = pingposisyon;
        this.value = value;
        this.pingphoto = pingphoto;
        this.OnOffLinePing=OnOffLinePing;
    }

    public String getName() {
        return this.name;
    }
    public String getPingPosis() {
        return this.pingposisyon;
    }
    public Boolean getValue() {
        return this.value;
    }
    public int getpingphoto() {
        return this.pingphoto;
    }
    public Boolean getOnOffLinePing(){
        return this.OnOffLinePing;
    }

}
