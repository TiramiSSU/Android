package com.example.ssuapp;

public class DBUserInformation {
    private String id;
    private String name;
    private String email;
    private int projectcnt;
    private String projectlist;
    private String invitedproject;

    public DBUserInformation() {}

    public DBUserInformation(String id, String name, String email, int projectcnt, String projectlist, String invitedproject){
        this.id = id;
        this.name =name;
        this.email = email;
        this.projectcnt=projectcnt;
        this.projectlist=projectlist;
        this.invitedproject =invitedproject;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInvitedproject(String invitedproject) {
        this.invitedproject = invitedproject;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getEmail() {
        return email;
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

    public String getInvitedproject() {
        return invitedproject;
    }

    public String getId() {
        return id;
    }
}
