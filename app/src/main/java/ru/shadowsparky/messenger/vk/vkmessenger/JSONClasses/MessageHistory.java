package ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses;

public class MessageHistory {
    private int date;
    private int from_id;
    private int to_id;
    private String message_subject;

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }
    public int getFrom_id() {
        return from_id;
    }
    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }
    public int getTo_id() {
        return to_id;
    }
    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }
    public String getMessage_subject() {
        return message_subject;
    }
    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }
}
