package com.devwithbensmith.irapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static String URL = "http://192.168.2.3/remote?{q_name}={q_value}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void power(View view) {
        doRemoteCommand(view, Commands.POWER);
    }

    public void source(View view) {
        doRemoteCommand(view, Commands.SOURCE);
    }

    public void mute(View view) {
        doRemoteCommand(view, Commands.MUTE);
    }

    public void channelUp(View view) {
        doRemoteCommand(view, Commands.CHANNEL_UP);
    }

    public void channelDown(View view) {
        doRemoteCommand(view, Commands.CHANNEL_DOWN);
    }

    public void volumeUp(View view) {
        doRemoteCommand(view, Commands.VOLUME_UP);
    }

    public void volumeDown(View view) {
        doRemoteCommand(view, Commands.VOLUME_DOWN);
    }

    private void doRemoteCommand(View view, String... params) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //TODO check if server is up also.
        if (networkInfo != null && networkInfo.isConnected()) {
            new RemoteTask().execute(params);
        } else {
            toaster("No network connection available.");
        }
    }

    private void toaster(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    private class RemoteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

//            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//            messageConverters.add(new StringHttpMessageConverter());
//            restTemplate.setMessageConverters(messageConverters);

            Map<String, String> map = new HashMap<>();
            restTemplate.put(URL, "", params[0], params[1]);
            return "true";
        }

    }


}
