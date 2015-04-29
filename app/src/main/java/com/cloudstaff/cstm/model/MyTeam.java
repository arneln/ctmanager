package com.cloudstaff.cstm.model;

import java.util.ArrayList;

public class MyTeam {
    private String id;
    private String stf_id;
    private String username;
    private String name;
    private String photo;
    private String shift_start;
    private String shift_end;
    private String team;
    private String position;
    private String status;
    private String favorite;
    private String login;
    private boolean image;
    private boolean checkBox;
    private ArrayList<Metrics> metrics;

    public MyTeam() {
        this.id = "";
        this.stf_id = "";
        this.username = "";
        this.name = "";
        this.photo = "";
        this.shift_start = "";
        this.shift_end = "";
        this.team = "";
        this.position = "";
        this.status = "";
        this.favorite = "";
        this.login = "";
        this.image = false;
        this.checkBox = false;
        this.metrics = new ArrayList<Metrics>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStf_id() {
        return this.stf_id;
    }

    public void setStf_id(String stf_id) {
        this.stf_id = stf_id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getShift_start() {
        return this.shift_start;
    }

    public void setShift_start(String shift_start) {
        this.shift_start = shift_start;
    }

    public String getShift_end() {
        return this.shift_end;
    }

    public void setShift_end(String shift_end) {
        this.shift_end = shift_end;
    }

    public String getTeam() {
        return this.team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFavorite() {
        return this.favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ArrayList<Metrics> getMetrics() {
        return this.metrics;
    }

    public void setMetrics(ArrayList<Metrics> metrics) {
        this.metrics = metrics;
    }

    public boolean getBooleanImage() {
        return this.image;
    }

    public void setBooleanImage(boolean image) {
        this.image = image;
    }

    public boolean isChecked() {
        return this.checkBox;
    }

    public void setChecked(boolean checked) {
        this.checkBox = checked;
    }
}
