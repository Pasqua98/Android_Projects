package com.unipd.http;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pasqua98 on 8/15/17.
 */

public class HttpConnector {

    private static HttpConnector instance;
    private static HttpsURLConnection connection;
    private HttpConnector(){

    }

    public static HttpConnector getInstance() {
        if(instance==null){
            instance=new HttpConnector();
        }
        return instance;
    }

    public void openConnection(URL url){
        if (url == null) {
            throw new UnsupportedOperationException("Url mustn't be null");
        }else if(connection!=null){
            connection.disconnect();
        }
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Connection",connection.toString());
    }

    public void receiveWebData(){
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str=br.readLine();
            System.out.println(connection.getResponseMessage());
            while (str!=null){
                Log.d("s",str);
                str=br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
