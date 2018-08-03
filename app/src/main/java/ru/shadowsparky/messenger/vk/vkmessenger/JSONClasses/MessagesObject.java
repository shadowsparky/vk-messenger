package ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessagesObject implements Serializable {
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    private int user_id;
    private String body;
}
