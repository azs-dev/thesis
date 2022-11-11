package com.example.myapplication;

public class Jobfair {
    private String jfname;
    private String jflocation;
    private String jfdate;

    public Jobfair(String jfname, String jflocation, String jfdate) {
        this.jfname = jfname;
        this.jflocation = jflocation;
        this.jfdate = jfdate;
    }

    public String getJfname() {
        return jfname;
    }

    public void setJfname(String jfname) {
        this.jfname = jfname;
    }

    public String getJflocation() {
        return jflocation;
    }

    public void setJflocation(String jflocation) {
        this.jflocation = jflocation;
    }

    public String getJfdate() {
        return jfdate;
    }

    public void setJfdate(String jfdate) {
        this.jfdate = jfdate;
    }
}
