package com.softtechglobal.androidcarmanager.Database;

public class UserInfoDB {
    String fullname, gender, phone;

    public UserInfoDB() {
    }

    public UserInfoDB(String fullname, String gender, String phone) {
        this.fullname = fullname;
        this.gender = gender;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
