package com.shareconnect.services.slaves;

import android.app.Service;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.format.Formatter;

import com.shareconnect.constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.content.Context.WIFI_SERVICE;
import static com.shareconnect.constants.Constants.ACTION_SEND_BROADCAST;
import static com.shareconnect.constants.Constants.PORT;

/**
 * Created by pasqua98 on 7/20/17.
 */

public class ConnectionListener extends AsyncTask implements Talker {

    private MulticastSocket socket;
    private Service boundService;
    private byte[] BUFFER;

    public ConnectionListener(Service boundService) {
        super();
        bindService(boundService);
        BUFFER = new byte[64];
        try {
            socket = new MulticastSocket(PORT);
            InetAddress ip = InetAddress.getByName(Constants.BROADCAST_IP);
            socket.joinGroup(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        final byte[] ACTION_ID = toByteArray(ACTION_SEND_BROADCAST);
        final byte[] UID = toByteArray(createUid());
        final byte[] DEVICE_NAME = Build.MODEL.getBytes();

        System.out.println(Arrays.toString(ACTION_ID));
        ByteBuffer byteBuffer = ByteBuffer.wrap(BUFFER);
        byteBuffer.put(ACTION_ID);
        byteBuffer.put(UID);
        byteBuffer.put(DEVICE_NAME);


        BUFFER = byteBuffer.array();
        DatagramPacket infoPacket = null;
        try {
            infoPacket = new DatagramPacket(BUFFER, BUFFER.length, InetAddress.getByName(Constants.BROADCAST_IP), PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(BUFFER));

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

    private byte[] toByteArray(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

}
