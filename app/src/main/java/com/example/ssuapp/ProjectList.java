package com.example.ssuapp;

public class ProjectList {
    private String projectlist;
    private int projectcnt;

    public ProjectList() {    }

    public ProjectList(String projectlist, int projectcnt){
        this.projectlist = projectlist;
        this.projectcnt = projectcnt;
    }

    public int getProjectcnt() {
        return projectcnt;
    }

    public String getProjectlist() {
        return projectlist;
    }

    public void setProjectcnt(int projectcnt) {
        this.projectcnt = projectcnt;
    }

    public void setProjectlist(String projectlist) {
        this.projectlist = projectlist;
    }
}
