package ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RecyclerViewMessengerAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;
import ru.shadowsparky.messenger.vk.vkmessenger.MVP.OpenMessage.OpenMessageActivity;
import ru.shadowsparky.messenger.vk.vkmessenger.Utils.MergedObject;

public class MainPresenter implements IMain.IMainPresenter {
    public static final String MAIN_TAG = "MAIN_TAG";
    PublishSubject<MergedObject> result = PublishSubject.create();
    PublishSubject<Integer> clickedElement;

    IMain.IMainModel _model;
    IMain.IMainView _view;

    public MainPresenter(IMain.IMainView _view) {
        this._view = _view;
        catchResult();
        _model = new MainModel(result);
        messageListUpdate();
    }

    @Override public void catchResult() {
        result.subscribe(res -> {
            clickedElement = PublishSubject.create();
            onClicked(res);
            RecyclerViewMessengerAdapter adapter = new RecyclerViewMessengerAdapter(res, _view.getContext(), clickedElement);
            _view.setAdapter(adapter);
            _view.setLoading(false);
            Log.println(Log.DEBUG, MAIN_TAG, "Adapter configured");
        });
    }

    @Override public void onClicked(MergedObject mObject) {
        clickedElement.subscribeOn(Schedulers.io())
                .subscribe(position -> {
                    openMessage(mObject.getMessages().get(position), mObject.getUser().get(position));
        });
    }

    @Override public void openMessage(MessagesObject message, UserInfoObject user) {
        Intent i = new Intent(_view.getContext(), OpenMessageActivity.class);
        i.putExtra("Message", message);
        i.putExtra("User", user);
        _view.openMessageActivity(i);
    }

    @Override
    public void messageListUpdate() {
        _view.setLoading(true);
        _model.initDialogs();
    }
}
