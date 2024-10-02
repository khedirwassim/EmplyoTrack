package com.example.damproject;

public class EmployeeClass {
    String id, f_name, l_name, phone, mail;
    byte[] image;

    public EmployeeClass(String id, String f_name, String l_name, String phone, String mail, byte[] image) {
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.phone = phone;
        this.mail = mail;
        this.image = image;
    }
    public String getId() {
        return id;
    }

    public String getF_name() {
        return f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}