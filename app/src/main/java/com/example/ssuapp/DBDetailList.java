package com.example.ssuapp;

public class DBDetailList {
    String detail1;
    String detail2;
    String detail3;
    boolean completed;

    public DBDetailList() {
        this.detail1 = "";
        this.detail2 = "";
        this.detail3 = "";
        this.completed = false;
    }

    public DBDetailList(String detail1, String detail2, String detail3, boolean completed) {
        this.detail1 = detail1;
        this.detail2 = detail2;
        this.detail3 = detail3;
        this.completed = completed;
    }

    public String getDetail1() {
        return detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public String getDetail3() {
        return detail3;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    public void setDetail3(String detail3) {
        this.detail3 = detail3;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
