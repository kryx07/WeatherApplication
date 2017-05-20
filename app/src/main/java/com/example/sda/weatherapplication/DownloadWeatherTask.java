package com.example.sda.weatherapplication;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class DownloadWeatherTask extends AsyncTask<Void, Void, Response> {
    private final static String API_URL = "http://api.weatherbit.io/v1.0/forecast/3hourly/geosearch?key=9a57fc45c2854ee6b04afcd5ce6dd452&city=London&country=UK";

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