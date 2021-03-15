package com.softtechglobal.androidcarmanager.Database;

public class Expenses {
    String expenseName;
    long date, time;
    int odometer;
    double cost;

    public Expenses() {
    }

    public Expenses(String expenseName, long date, long time, int odometer, double cost) {
        this.expenseName = expenseName;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
