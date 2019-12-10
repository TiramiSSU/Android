package com.example.ssuapp;

public class DBTodoList {
    private int cnt;
    private String countedStr;

    public DBTodoList() {
        this.cnt = 0;
        countedStr = "";
    }

    public DBTodoList(int cnt, String countedStr) {
        this.cnt = cnt;
        this.countedStr = countedStr;
    }

    public int getCnt() {
        return cnt;
    }

    public String getCountedStr() {
        return countedStr;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setCountedStr(String countedStr) {
        this.countedStr = countedStr;
    }
}
