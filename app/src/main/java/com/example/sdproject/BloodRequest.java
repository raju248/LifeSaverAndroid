package com.example.sdproject;

public class BloodRequest {
    String name;
    String mobile;
    String address;
    String comment;
    String bloodGroup;
    String id;


    public BloodRequest(String name, String mobile, String address, String comment, String BloodGroup, String Id) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.comment = comment;
        this.bloodGroup = BloodGroup;
        this.id = Id;
    }

    public BloodRequest()
    {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
