package com.softtechglobal.androidcarmanager.Database;

public class ExpensesDB {
    String expenseTitle, expenseType;
    Long date, time;
    Double odometer, cost, ltr;
    String image;

    public ExpensesDB() {
    }

//  while adding odometer reading
    public ExpensesDB(Long date, Long time, Double odometer) {
        this.date = date;
        this.time = time;
        this.odometer = odometer;
    }
//  while adding other expenses except fuel
    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
    }

//  while addding fuel expense
    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost, Double ltr) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
        this.ltr = ltr;
    }
//  while adding fuel expense with images
//    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost, Double ltr, String image) {
//        this.expenseTitle = expenseTitle;
//        this.expenseType = expenseType;
//        this.date = date;
//        this.time = time;
//        this.odometer = odometer;
//        this.cost = cost;
//        this.ltr = ltr;
//        this.image = image;
//    }
//  whiel adding expenses with images
    public ExpensesDB(String expenseTitle, String expenseType, Long date, Long time, Double odometer, Double cost, String image) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.cost = cost;
        this.image = image;
    }

    public Double getLtr() {
        return ltr;
    }

    public void setLtr(Double ltr) {
        this.ltr = ltr;
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
