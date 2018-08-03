package ru.shadowsparky.messenger.vk.vkmessenger.MVP.OpenMessage;

import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RV_MessageHistoryAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessageHistory;

public class OpenMessagePresenter implements IOpenMessage.IOpenMessagePresenter {
    IOpenMessage.IOpenMessageView view;
    IOpenMessage.IOpenMessageModel model;
    PublishSubject <LinkedList<MessageHistory>> resultCallback = PublishSubject.create();
    PublishSubject<String> ScrollCallback = PublishSubject.create();

    public OpenMessagePresenter(IOpenMessage.IOpenMessageView view) {
        this.view = view;
        handleResult();
        scrollCallback();
        model = new OpenMessageModel(resultCallback);
        getMessages();
    }

    @Override public void handleResult() {
        resultCallback
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Collections.reverse(result);
                    RV_MessageHistoryAdapter adapter = new RV_MessageHistoryAdapter(result, view.getContext(), view.getMessageData().getUser_id(), ScrollCallback);
                    view.setAdapter(adapter);
                    Log.println(Log.DEBUG, "MAIN_TAG", "Messages has been loaded");
        });
    }

    @Override public void getMessages() {
        model.getMessageHistoryRequest(view.getMessageData().getUser_id());
    }

    @Override public void sendMessageClick() {
        PublishSubject<String> result = PublishSubject.create();
        result.filter(resStr -> resStr.equals("OK"))
                .subscribe(resStr -> {
                        getMessages();
                        view.clearMessageBox();
                        Toast.makeText(view.getContext(), "message sended", Toast.LENGTH_SHORT).show();
        });
        model.sendMessage(view.getMessageData().getUser_id(), view.getMessageBody(), result);
    }

    @Override public void scrollCallback() {
        ScrollCallback.map(res -> res.equals("START"))
                .subscribe(res -> Log.println(Log.DEBUG, "MAIN_TAG", "scrolled on start"));
    }
}