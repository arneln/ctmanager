package com.cloudstaff.cstm.model;

public class Metrics {
    String title;
    String dailyAverage;
    String weeklyAverage;
    String totalData;

    public Metrics() {
        this.title = "";
        this.dailyAverage = "";
        this.weeklyAverage = "";
        this.totalData = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDailyAverage() {
        return dailyAverage;
    }

    public void setDailyAverage(String dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    public String getWeeklyAverage() {
        return weeklyAverage;
    }

    public void setWeeklyAverage(String weeklyAverage) {
        this.weeklyAverage = weeklyAverage;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }
}
