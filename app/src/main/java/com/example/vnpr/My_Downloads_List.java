package com.example.vnpr;

public class My_Downloads_List {
    private String regis_no;
    private String owner_name;
    private String owner_type;
    private String verhicle_img;

    public My_Downloads_List(String regis_no, String owner_name, String owner_type, String verhicle_img) {
        this.regis_no = regis_no;
        this.owner_name = owner_name;
        this.owner_type = owner_type;
        this.verhicle_img = verhicle_img;
    }

    public String getRegis_no() {
        return regis_no;
    }

    public void setRegis_no(String regis_no) {
        this.regis_no = regis_no;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public String getVerhicle_img() {
        return verhicle_img;
    }

    public void setVerhicle_img(String verhicle_img) {
        this.verhicle_img = verhicle_img;
    }
}
