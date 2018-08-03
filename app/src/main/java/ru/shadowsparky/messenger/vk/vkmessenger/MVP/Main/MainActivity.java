package ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RecyclerViewMessengerAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.R;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;
import ru.shadowsparky.messenger.vk.vkmessenger.Utils.MergedObject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetDialogResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements IMain.IMainView {
    IMain.IMainPresenter presenter;
    RecyclerView _list;
    SwipeRefreshLayout mRefresher;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _list = findViewById(R.id.messagesList);
        mRefresher = findViewById(R.id.refresh_messages);
        presenter = new MainPresenter(this);
        initRefresher();
        setTitle("Сообщения");
    }
    @Override public void setAdapter(RecyclerViewMessengerAdapter adapter) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        _list.setLayoutManager(llm);
        _list.setHasFixedSize(false);
        _list.setAdapter(adapter);
    }
    @Override public Context getContext() {
        return getApplicationContext();
    }
    @Override public void openMessageActivity(Intent i) {
        startActivity(i);
    }
    @Override public void initRefresher() {
        mRefresher.setOnRefreshListener(() -> presenter.messageListUpdate());
    }
    @Override public void setLoading(boolean result) {
        if (result)
            _list.setVisibility(View.INVISIBLE);
        else
            _list.setVisibility(View.VISIBLE);
        mRefresher.setRefreshing(result);
    }
}
