package ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main;

import android.content.Context;
import android.content.Intent;

import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RecyclerViewMessengerAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;
import ru.shadowsparky.messenger.vk.vkmessenger.Utils.MergedObject;

public interface IMain {
    interface IMainView {
        void setAdapter(RecyclerViewMessengerAdapter adapter);
        Context getContext();
        void openMessageActivity(Intent i);
        void initRefresher();
        void setLoading(boolean result);
    }
    interface IMainPresenter {
        void catchResult();
        void onClicked(MergedObject mObject);
        void openMessage(MessagesObject message, UserInfoObject user);
        void messageListUpdate();
    }
    interface IMainModel {
        void initDialogs();
        void getDialogsCallback();
        void initUsers(int UserID);
        void endCallback();
        void getDataCallback();
    }
}