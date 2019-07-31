package com.bachao.dcc_project.model;

public class modelForComplain {

    String date  , postId , complain , lat ,lonG  ;


    public modelForComplain() {
    }

    public modelForComplain(String date, String postId, String complain, String lat, String lonG) {
        this.date = date;
        this.postId = postId;
        this.complain = complain;
        this.lat = lat;
        this.lonG = lonG;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLonG() {
        return lonG;
    }

    public void setLonG(String lonG) {
        this.lonG = lonG;
    }

    public modelForComplain(String date, String postId, String complain) {
        this.date = date;
        this.postId = postId;
        this.complain = complain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }
}
