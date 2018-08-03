package ru.shadowsparky.messenger.vk.vkmessenger.Utils;

import android.app.Application;

import com.vk.sdk.VKSdk;

public class Object extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
