package com.softtechglobal.androidcarmanager.Database;

public class UserInfoDB {
    String fullname, email, phone, dpUrl;

    public UserInfoDB() {
    }

    public UserInfoDB(String fullname, String email, String phone, String dpUrl) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.dpUrl = dpUrl;
    }

    public UserInfoDB(String fullname, String email, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }
}
