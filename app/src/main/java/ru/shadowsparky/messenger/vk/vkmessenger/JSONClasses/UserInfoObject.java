package ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses;

import java.io.Serializable;

public class UserInfoObject implements Serializable {
    private String first_name;
    private String last_name;

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    String photoUrl;
}
