package com.example.weatherapp;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherAPI implements Runnable {

    private MainActivity mainActivity;
    private String unit;
    private final String KEY = "d8efe8af2f20910c27f0eac1561ea0f7";

    private String DATA_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=41.8675766&lon=-87.616232&units=metric&lang=en&exclude=minutely&appid=" + KEY;
    private static final String TAG = "WeatherAPI";

    public WeatherAPI() {
    }

    public WeatherAPI(MainActivity mainActivity, String unit) {
        this.mainActivity = mainActivity;
        this.unit = unit;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: URL " + DATA_URL);
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode() + " , " +conn.getResponseMessage());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            reader.close();
            is.close();

            Log.d(TAG, "run: " + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        handleResults(sb.toString());
    }

    private void handleResults(String s) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }

        final Weather weatherData = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (weatherData != null)
                Toast.makeText(mainActivity, "Loaded " + weatherData.getLat() + ", " + weatherData.getLon() + " - " + weatherData.getTimezone(), Toast.LENGTH_LONG).show();
//            mainActivity.updateData(countryList);
        });
    }

    private Weather parseJSON(String s) {
        Weather weather = null;
        try {
            JSONObject jsonObject = new JSONObject(s);

            double lat = jsonObject.getDouble("lat");
            double lon = jsonObject.getDouble("lon");
            String timezone = jsonObject.getString("timezone");
            long timezoneOffset = jsonObject.getLong("timezone_offset");
            WeatherData current = null;
            List<Hourly> hourly = new ArrayList<>();
            List<Daily> daily = new ArrayList<>();

            if (jsonObject.has("current")) {
                JSONObject currentObj = jsonObject.getJSONObject("current");
                JSONArray currentWeatherJsonArray = currentObj.getJSONArray("weather");

                List<WeatherInfo> currentWeatherInfo = new ArrayList<>();
                if (currentWeatherJsonArray.length() > 0) {
                    JSONObject currentWeather = currentWeatherJsonArray.getJSONObject(0);
                    WeatherInfo weatherInfo = new WeatherInfo(
                            currentWeather.getInt("id"),
                            currentWeather.getString("main"),
                            currentWeather.getString("description"),
                            currentWeather.getString("icon")
                    );
                    currentWeatherInfo.add(weatherInfo);
                }

                current = new WeatherData(
                        currentObj.getLong("dt"),
                        currentObj.getLong("sunrise"),
                        currentObj.getLong("sunset"),
                        currentObj.getDouble("temp"),
                        currentObj.getDouble("feels_like"),
                        currentObj.getDouble("pressure"),
                        currentObj.getDouble("humidity"),
                        currentObj.getDouble("dew_point"),
                        currentObj.getInt("uvi"),
                        currentObj.getInt("clouds"),
                        currentObj.getLong("visibility"),
                        currentObj.getDouble("wind_speed"),
                        currentObj.getDouble("wind_deg"),
                        currentWeatherInfo
                );
            }

            if (jsonObject.has("hourly")) {
                JSONArray hourlyJsonArray = jsonObject.getJSONArray("hourly");

                for (int i = 0; i < hourlyJsonArray.length(); i++) {
                    JSONObject jo = hourlyJsonArray.getJSONObject(i);
                    JSONArray hourlyWeatherJsonArray = jo.getJSONArray("weather");

                    List<WeatherInfo> hourlyWeatherInfo = new ArrayList<>();
                    if (hourlyWeatherJsonArray.length() > 0) {
                        JSONObject hourlyWeather = hourlyWeatherJsonArray.getJSONObject(0);
                        WeatherInfo weatherInfo = new WeatherInfo(
                                hourlyWeather.getInt("id"),
                                hourlyWeather.getString("main"),
                                hourlyWeather.getString("description"),
                                hourlyWeather.getString("icon")
                        );
                        hourlyWeatherInfo.add(weatherInfo);
                    }

                    Hourly h = new Hourly(
                            jo.getLong("dt"),
                            jo.getDouble("temp"),
                            hourlyWeatherInfo,
                            jo.getDouble("pop")
                    );

                    hourly.add(i, h);
                }
            }

            if (jsonObject.has("daily")) {

            }





            weather = new Weather(
                lat,
                lon,
                timezone,
                timezoneOffset,
                current,
                hourly,
                daily
            );

            return weather;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
