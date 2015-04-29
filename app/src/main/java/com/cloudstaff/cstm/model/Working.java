package com.cloudstaff.cstm.model;

public class Working {
    private String task;
    private String date;

    public Working() {
        this.task = "";
        this.date = "";
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
