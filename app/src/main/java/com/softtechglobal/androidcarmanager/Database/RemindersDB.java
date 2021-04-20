package com.softtechglobal.androidcarmanager.Database;

public class RemindersDB {
    String title;
    Long date, time;

    public RemindersDB() {
    }

    public RemindersDB(String title, Long date, Long time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
