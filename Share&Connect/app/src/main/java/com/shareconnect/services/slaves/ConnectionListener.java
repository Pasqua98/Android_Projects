package com.shareconnect.services.slaves;

import android.app.Service;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.format.Formatter;

import com.shareconnect.constants.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by pasqua98 on 7/20/17.
 */

public class ConnectionListener extends AsyncTask implements Talker {

    private MulticastSocket socket;
    private Service boundService;
    private byte[] BUFFER;

    public ConnectionListener(Service boundService) {
        super();
        BUFFER = new byte[32];
        bindService(boundService);
        try {
            socket = new MulticastSocket(Constants.PORT);
            InetAddress ip = InetAddress.getByName(Constants.BROADCAST_IP);
            socket.joinGroup(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        final byte[] ACTION_ID = BigInteger.valueOf(Constants.ACTION_SEND_BROADCAST).toByteArray();
        final byte[] UID = BigInteger.valueOf(createUid()).toByteArray();
        final byte[] DEVICE_NAME = Build.MODEL.getBytes();
        final byte[] CURRENT_IP = getCurrentIP().getBytes();

        ByteBuffer byteBuffer = ByteBuffer.wrap(BUFFER);
        byteBuffer.put(ACTION_ID);
        byteBuffer.put(UID);
        byteBuffer.put(DEVICE_NAME);
        byteBuffer.put(CURRENT_IP);


        BUFFER = byteBuffer.array();
        DatagramPacket infoPacket = new DatagramPacket(BUFFER, BUFFER.length);

        try {
            socket.send(infoPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void bindService(Service service) {
        this.boundService = service;
    }

    @Override
    public void ping(InetAddress ip) {

    }

    private String getCurrentIP() {
        WifiManager wm = (WifiManager) boundService.getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private int createUid() {
        Integer i = new Integer((int) System.currentTimeMillis());
        return i.hashCode();
    }

}
