package com.adedayo.gadsleaderboard.model;


public class Skills {
    public String name;
    public String hours;
    public String country;
    public String badgeUrl;
    public String score;

    public Skills(String name, String hours, String country, String badgeUrl, String score) {
        this.name = name;
        this.hours = hours;
        this.country = country;
        this.badgeUrl = badgeUrl;
        this.score = score;
    }
}