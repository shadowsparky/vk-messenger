package ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetDialogResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;
import ru.shadowsparky.messenger.vk.vkmessenger.Utils.MergedObject;

import static ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main.MainPresenter.MAIN_TAG;

public class MainModel implements IMain.IMainModel {
    /*Callbacks*/
    PublishSubject<VKApiGetDialogResponse> dialogResult = PublishSubject.create();
    PublishSubject<JSONObject> dataResult = PublishSubject.create();
    PublishSubject<String> endCallback = PublishSubject.create();
    PublishSubject<MergedObject> workEndedCallback;

    /*Result lists*/
    volatile LinkedList<MessagesObject> MessageList = new LinkedList<>();
    volatile LinkedList<UserInfoObject> UserList = new LinkedList<>();

    public MainModel(PublishSubject<MergedObject> workEndedCallback) {
        this.workEndedCallback = workEndedCallback;
        getDialogsCallback();
        getDataCallback();
        endCallback();
    }

    public void LOG(String data) {
        Log.println(Log.DEBUG, MAIN_TAG, data);
    }

    /*Handle api results*/
    @Override public void getDialogsCallback(){
        dialogResult.observeOn(Schedulers.io())
                .subscribe(res->{
                    for (int i = 0; i < res.items.getCount(); i++) {
                        MessagesObject messageItem = new MessagesObject();
                        messageItem.setBody(res.items.get(i).message.body);
                        messageItem.setUser_id(res.items.get(i).message.user_id);
                        Thread.sleep(300);
                        initUsers(messageItem.getUser_id());
                        LOG("Message ID: " + i+ " user id: " +messageItem.getUser_id());
                        MessageList.add(messageItem);
                    }
                    endCallback.onNext("OK");
                }, error -> LOG(error.toString()));
    }

    @Override public void endCallback() {
        endCallback.observeOn(AndroidSchedulers.mainThread())
        .subscribe(res-> {
            MergedObject mObject = new MergedObject();
            mObject.setMessages(MessageList);
            mObject.setUser(UserList);
            workEndedCallback.onNext(mObject);
        }, error -> LOG(error.toString()));
    }

    @Override public void getDataCallback(){
        dataResult
                .subscribe(jsonObject -> {
                    UserInfoObject data = new UserInfoObject();
                    try {
//                        Thread.sleep(400);
                        JSONArray resultArr = jsonObject.getJSONArray("response");
                        JSONObject resultObject = resultArr.getJSONObject(0);
                        data.setFirst_name(resultObject.getString("first_name"));
                        data.setLast_name(resultObject.getString("last_name"));
                        data.setPhotoUrl(resultObject.getString("photo_big"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    UserList.add(data);
                    LOG("user last name: " + data.getLast_name());
                }, error -> LOG(error.toString()));
    }

    /*VK API*/
    @Override public void initUsers(int UserID) {
        VKRequest request = VKApi.users().get();
        request.addExtraParameter("user_ids", UserID);
        request.addExtraParameter("fields", "photo_big");
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                dataResult.onNext(response.json);
            }

            @Override
            public void onError(VKError error) {
                LOG("IN INIT USER VK ERROR " + error.toString());
//                super.onError(error);
            }
        });
    }

    @Override public void initDialogs() {
        MessageList.clear();
        UserList.clear();
        VKRequest request = VKApi.messages().getDialogs();
        request.addExtraParameter("count", 10);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override public void onComplete(VKResponse response) {
                super.onComplete(response);
                dialogResult.onNext((VKApiGetDialogResponse) response.parsedModel);
            }
            @Override public void onError(VKError error) {
                LOG("IN INIT DIALOGS VK ERROR " + error.toString());
            }
        });
    }
}
