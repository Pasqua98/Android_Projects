package com.shareconnect.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.shareconnect.constants.Constants;
import com.shareconnect.services.slaves.ConnectionListener;

public class ConnectionHandler extends IntentService {

    private ConnectionListener connectionListener;

    public ConnectionHandler() {
        super("NAME");
    }

    public ConnectionHandler(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        switch (intent.getIntExtra(Constants.START_SERVICE_STR, Integer.MAX_VALUE)) {
            case Constants.START_SERVICE:
                connectionListener = new ConnectionListener(this);
                break;
        }
    }
}
