package com.example.ssuapp;

public class DBUserInformation {
    private String name;
    private int projectcnt;
    private String projectlist;

    public DBUserInformation() {}

    public DBUserInformation(String name, int projectcnt, String projectlist){
        this.name =name;
        this.projectcnt=projectcnt;
        this.projectlist=projectlist;
    }

    public void setProjectlist(String projectlist) {
        this.projectlist = projectlist;
    }

    public void setProjectcnt(int projectcnt) {
        this.projectcnt = projectcnt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectlist() {
        return projectlist;
    }

    public int getProjectcnt() {
        return projectcnt;
    }

    public String getName() {
        return name;
    }
}
