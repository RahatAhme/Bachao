package com.bachao.dcc_project.model;

public class dataModel {

    String name , uid , ownNumber  , emNum , emnumTwo   , adress , nidNo ;

    public dataModel() {
    }

    public dataModel(String name, String uid, String ownNumber, String emNum, String emnumTwo, String adress, String nidNo) {
        this.name = name;
        this.uid = uid;
        this.ownNumber = ownNumber;
        this.emNum = emNum;
        this.emnumTwo = emnumTwo;
        this.adress = adress;
        this.nidNo = nidNo;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNidNo() {
        return nidNo;
    }

    public void setNidNo(String nidNo) {
        this.nidNo = nidNo;
    }

    public dataModel(String name, String uid, String ownNumber, String emNum, String emnumTwo) {
        this.name = name;
        this.uid = uid;
        this.ownNumber = ownNumber;
        this.emNum = emNum;
        this.emnumTwo = emnumTwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOwnNumber() {
        return ownNumber;
    }

    public void setOwnNumber(String ownNumber) {
        this.ownNumber = ownNumber;
    }

    public String getEmNum() {
        return emNum;
    }

    public void setEmNum(String emNum) {
        this.emNum = emNum;
    }

    public String getEmnumTwo() {
        return emnumTwo;
    }

    public void setEmnumTwo(String emnumTwo) {
        this.emnumTwo = emnumTwo;
    }
}
