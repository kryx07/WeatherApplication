package com.example.sda.weatherapplication;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private final static String API_URL = "http://api.weatherbit.io/v1.0/forecast/3hourly/geosearch?key=9a57fc45c2854ee6b04afcd5ce6dd452&city=London&country=UK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
/*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                downloadWeather();
            }
        };*/

        Button button = (Button) findViewById(R.id.download_weather_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadWeatherTask downloadWeatherTask = new DownloadWeatherTask();
                downloadWeatherTask.execute();               /* new Thread(runnable).start();*/
                /*downloadWeather();*/
            }
        });
    }

    private class DownloadWeatherTask extends AsyncTask<Void, Void, Response> {


        @Override
        protected Response doInBackground(Void... params) {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                Gson gson = new Gson();
                Response response = gson.fromJson(reader, Response.class);

                Iterator iterator = response.getData().iterator();
                while (iterator.hasNext()) {
                    Forecast forecast = (Forecast) iterator.next();
                    Log.d("Main Activity", "downloadWeather: " + "temperature = " + forecast.getTemp() + ", datetime=" + forecast.getDatetime());
                    // Tutaj zrob cos z prognoza
                }

                reader.close();
                connection.disconnect();

                return response;

            } catch (MalformedURLException e) {
                // Nieprawidlowy adres
                return null;
            } catch (IOException e) {
                // Nie udalo sie polaczyc
                return null;
            }


        }

    }
}
