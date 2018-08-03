package ru.shadowsparky.messenger.vk.vkmessenger.MVP.OpenMessage;

import android.content.Context;

import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RV_MessageHistoryAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;

public interface IOpenMessage {
    interface IOpenMessageView {
        void initVariables();
        MessagesObject getMessageData();
        UserInfoObject getUserData();
        void setAdapter (RV_MessageHistoryAdapter adapter);
        Context getContext();
        String getMessageBody();
        void buttonInit();
        void clearMessageBox();
    }
    interface IOpenMessagePresenter {
        void handleResult();
        void getMessages();
        void sendMessageClick();
        void scrollCallback();
    }
    interface IOpenMessageModel {
        void getMessageHistoryRequest(int UserID);
        void sendMessage(int UserID, String message, PublishSubject<String> result);
        void handleResponse();
    }
}
