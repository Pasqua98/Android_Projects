package com.shareconnect.services.slaves;

import android.app.Service;

import java.net.InetAddress;

/**
 * Created by pasqua98 on 7/20/17.
 */

public class MessageHandler implements Talker {
    @Override
    public void run() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void bindService(Service service) {

    }

    @Override
    public void ping(InetAddress ip) {

    }
}
