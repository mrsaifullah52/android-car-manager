package com.softtechglobal.androidcarmanager.Views;

public class ModelForList {
    String title;
    Long date;

    public ModelForList(String title, Long date) {
        this.title = title;
        this.date = date;
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
}
