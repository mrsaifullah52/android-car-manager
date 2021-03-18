package com.softtechglobal.androidcarmanager.Database;

public class ExpensesDB {
    String expenseType;
    long date, time;
    double odometer,cost;

    public ExpensesDB() {
    }

    public ExpensesDB(String expenseType, long date, long time, double odometer, double cost) {
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
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

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
