package com.softtechglobal.androidcarmanager.Database;

public class ExpensesDB {
    String expenseType;
    Long date, time;
    Double odometer, cost;

    public ExpensesDB() {
    }

    public ExpensesDB(String expenseType, Long date, Long time, Double odometer, Double cost) {
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
