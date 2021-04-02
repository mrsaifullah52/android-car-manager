package com.softtechglobal.androidcarmanager.Database;

public class ExpensesDB {
    String expenseTitle, expenseType;
    Long date, time;
    Double odometer, cost;
    String image;

    public ExpensesDB() {
    }

    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
    }

    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost, String image) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
