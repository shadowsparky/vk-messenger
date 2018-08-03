package ru.shadowsparky.messenger.vk.vkmessenger.MVP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.shadowsparky.messenger.vk.vkmessenger.MVP.Main.MainActivity;
import ru.shadowsparky.messenger.vk.vkmessenger.R;

public class auth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reAuth();
        initButton();
    }

    public void reAuth() {
        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                switch(res){
                    case LoggedIn:
                        finish();
                        OK();
                        break;
                    default:
                        setContentView(R.layout.activity_auth);
                }
            }

            @Override
            public void onError(VKError error) {
                ERROR();
            }
        });
    }

    public void initButton(){
        Button authButton = findViewById(R.id.authButton);
        authButton.setOnClickListener(view -> VKSdk.login(this, new String[] { VKScope.FRIENDS, VKScope.MESSAGES }));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {

            @Override public void onResult(VKAccessToken res) {
                OK();
            }

            @Override public void onError(VKError error) {
                ERROR();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void OK(){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void ERROR(){
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }
}
