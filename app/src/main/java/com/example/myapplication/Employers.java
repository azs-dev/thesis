package com.example.myapplication;

public class Employers {
    private String ename;
    private String count;

    public Employers(String ename, String count) {
        this.ename = ename;
        this.count = count;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
