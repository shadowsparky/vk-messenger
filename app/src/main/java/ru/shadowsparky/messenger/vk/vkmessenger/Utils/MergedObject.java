package ru.shadowsparky.messenger.vk.vkmessenger.Utils;

import java.util.LinkedList;

import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;

public class MergedObject {
    private LinkedList<MessagesObject> messages;
    private LinkedList<UserInfoObject> user;

    public LinkedList<MessagesObject> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<MessagesObject> messages) {
        this.messages = messages;
    }

    public LinkedList<UserInfoObject> getUser() {
        return user;
    }

    public void setUser(LinkedList<UserInfoObject> user) {
        this.user = user;
    }
}
