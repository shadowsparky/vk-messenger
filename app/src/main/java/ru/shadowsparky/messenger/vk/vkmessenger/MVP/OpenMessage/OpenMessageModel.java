package ru.shadowsparky.messenger.vk.vkmessenger.MVP.OpenMessage;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessageHistory;

import static ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main.MainPresenter.MAIN_TAG;

public class OpenMessageModel implements IOpenMessage.IOpenMessageModel {
    PublishSubject <VKResponse> responseHandle = PublishSubject.create();
    PublishSubject <LinkedList<MessageHistory>> resultCallback;

    public OpenMessageModel(PublishSubject <LinkedList<MessageHistory>> resultCallback) {
        this.resultCallback = resultCallback;
        handleResponse();
    }
    @Override public void getMessageHistoryRequest(int UserID) {
        VKParameters param = new VKParameters();
        param.put("count", 20);
        param.put("user_id", UserID);
        VKRequest request = new VKRequest("messages.getHistory", param);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                responseHandle.onNext(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    @Override public void sendMessage(int UserID, String message, PublishSubject<String> result) {
        VKParameters param = new VKParameters();
        param.put("user_id", UserID);
        param.put("message", message);
        VKRequest request = new VKRequest("messages.send", param);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                result.onNext("OK");
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.println(Log.DEBUG, "MAIN_TAG", error.toString());
                result.onNext("ERROR");
            }
        });
    }

    @Override public void handleResponse() {
        responseHandle
                .observeOn(Schedulers.io())
                .subscribe(result -> {
            JSONObject root_node = new JSONObject();
            JSONArray root_array = new JSONArray();
            LinkedList<MessageHistory> messageList = new LinkedList<>();
            try {
                root_node = result.json.getJSONObject("response");
                root_array = root_node.getJSONArray("items");
            } catch (JSONException e){
                Log.println(Log.DEBUG, MAIN_TAG, "Node not found exception: "+e.toString());
            }
            for (int i = 0; i < root_array.length(); i++) {
                MessageHistory historyObject = new MessageHistory();
                try {
                    JSONObject object = root_array.getJSONObject(i);
                    historyObject.setDate(object.getInt("date"));
                    historyObject.setFrom_id(object.getInt("from_id"));
                    historyObject.setMessage_subject(object.getString("body"));
                    historyObject.setTo_id(object.getInt("user_id"));
                } catch (JSONException e) {
                    Log.println(Log.DEBUG, MAIN_TAG, e.toString());
                }
                messageList.add(historyObject);
            }
            resultCallback.onNext(messageList);
        },
            error -> resultCallback.onNext(null));
    }
}
