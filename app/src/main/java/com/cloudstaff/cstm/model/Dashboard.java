package com.cloudstaff.cstm.model;

public class Dashboard {
    String title;
    String dailyAverage;
    String weeklyAverage;
    String totalData;

    public Dashboard(String title, String dailyAverage, String weeklyAverage, String totalData) {
        this.title = title;
        this.dailyAverage = dailyAverage;
        this.weeklyAverage = weeklyAverage;
        this.totalData = totalData;
    }

    public String getTitle() {
        return title;
    }

    public String getDailyAverage() {
        return dailyAverage;
    }

    public String getWeeklyAverage() {
        return weeklyAverage;
    }

    public String getTotalData() {
        return totalData;
    }
}
