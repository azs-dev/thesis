package com.example.myapplication;

public class Vacancy {
    private String vacancy;
    private String employer;
    private String location;

    public Vacancy(String vacancy, String employer, String location) {
        this.vacancy = vacancy;
        this.employer = employer;
        this.location = location;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
