package com.shareconnect.services.slaves;

import android.app.Service;

import java.net.InetAddress;

/**
 * Created by pasqua98 on 7/20/17.
 */

public interface Talker extends Runnable {
    @Override
    void run();

    boolean isConnected();

    void bindService(Service service);

    void ping(InetAddress ip);
}
