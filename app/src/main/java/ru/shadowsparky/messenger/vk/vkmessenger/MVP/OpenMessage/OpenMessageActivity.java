package ru.shadowsparky.messenger.vk.vkmessenger.MVP.OpenMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.shadowsparky.messenger.vk.vkmessenger.Adapter.RV_MessageHistoryAdapter;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessageHistory;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessagesObject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.UserInfoObject;
import ru.shadowsparky.messenger.vk.vkmessenger.R;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiOwner;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import static ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main.MainPresenter.MAIN_TAG;

public class OpenMessageActivity extends AppCompatActivity implements IOpenMessage.IOpenMessageView {
    MessagesObject message;
    UserInfoObject user;
    IOpenMessage.IOpenMessagePresenter presenter;
    RecyclerView messagesList;
    EditText MessageEditText;
    Button SendMessageButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_message);
        initVariables();
        messagesList = findViewById(R.id.MessageHistory);
        Log.println(Log.DEBUG, MAIN_TAG, message.getBody() + " " + user.getFirst_name());
        presenter = new OpenMessagePresenter(this);
        buttonInit();
        setTitle(user.getFirst_name() + " " + user.getLast_name());
    }
    @Override public void initVariables() {
        message = (MessagesObject) getIntent().getSerializableExtra("Message");
        user = (UserInfoObject) getIntent().getSerializableExtra("User");
        MessageEditText = findViewById(R.id.messageBody);
        SendMessageButton = findViewById(R.id.sendButton);
    }
    @Override public MessagesObject getMessageData() {
        return message;
    }
    @Override public UserInfoObject getUserData() {
        return user;
    }
    @Override public void setAdapter(RV_MessageHistoryAdapter adapter) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        messagesList.setLayoutManager(llm);
        messagesList.setHasFixedSize(false);
        messagesList.setAdapter(adapter);
        messagesList.scrollToPosition(adapter.getItemCount() - 1);
    }
    @Override public Context getContext() {
        return getApplicationContext();
    }
    @Override public String getMessageBody() {
        return MessageEditText.getText().toString();
    }
    @Override public void buttonInit() {
        SendMessageButton.setOnClickListener(view -> presenter.sendMessageClick());
        RxTextView.textChanges(MessageEditText)
                .map(messageStr -> messageStr.toString().trim().length() > 0)
                .subscribe(SendMessageButton::setEnabled);
    }
    @Override public void clearMessageBox() {
        MessageEditText.setText("");
    }
}
