package com.example.weatherapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements Serializable {

    private MenuItem c;
    private MenuItem f;
    private final List<Hourly> hourlyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Adapter adapter;
    private String unit;
    private Weather weather = new Weather();


    private TextView location;
    private TextView dateTime;
    private TextView temperature;
    private TextView description;
    private TextView clouds;
    private TextView winds;
    private TextView humidity;
    private TextView uvi;
    private TextView snow;
    private TextView visibility;
    private TextView day;
    private TextView noon;
    private TextView evening;
    private TextView night;
    private TextView dayTime;
    private TextView noonTime;
    private TextView eveningTime;
    private TextView nightTime;
    private TextView sunrise;
    private TextView sunset;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAllFields();

        recyclerView = findViewById(R.id.hourlyTemp);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        WeatherAPI weatherAPI = new WeatherAPI(this, unit);
        new Thread(weatherAPI).start();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initializeAllFields() {
        location = findViewById(R.id.location);
        dateTime = findViewById(R.id.datetime);
        temperature = findViewById(R.id.temperature);
        description = findViewById(R.id.description);
        clouds = findViewById(R.id.clouds);
        winds = findViewById(R.id.winds);
        humidity = findViewById(R.id.humidity);
        uvi = findViewById(R.id.uvi);
        snow = findViewById(R.id.snow);
        visibility = findViewById(R.id.visibility);
        day = findViewById(R.id.day);
        noon = findViewById(R.id.noon);
        evening = findViewById(R.id.evening);
        night = findViewById(R.id.night);
        dayTime = findViewById(R.id.dayTime);
        noonTime = findViewById(R.id.noonTime);
        eveningTime = findViewById(R.id.eveningTime);
        nightTime = findViewById(R.id.nightTime);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        weatherIcon = findViewById(R.id.weatherIcon);

        unit = "metric";
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        c = menu.findItem(R.id.unit_c);
        f = menu.findItem(R.id.unit_f);

        c.setVisible(true);
        f.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.unit_c) {
            item.setVisible(false);
            f.setVisible(true);
            unit = "imperial";

            WeatherAPI weatherAPI = new WeatherAPI(this, unit);
            new Thread(weatherAPI).start();

            return true;
        } else if (item.getItemId() == R.id.unit_f) {
            item.setVisible(false);
            c.setVisible(true);
            unit = "metric";

            WeatherAPI weatherAPI = new WeatherAPI(this, unit);
            new Thread(weatherAPI).start();

            return true;
        } else {
            Intent intent = new Intent(this, DailyActivity.class);

            intent.putExtra("weather", new Weather(
                    weather.getLat(),
                    weather.getLon(),
                    weather.getTimezone(),
                    weather.getTimezoneOffset(),
                    weather.getCurrent(),
                    weather.getHourly(),
                    weather.getDaily()
            ));
            intent.putExtra("unit", unit);
            startActivity(intent);

            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(Weather weatherData) {
        if (weatherData != null) {
            setWeatherData(weatherData);

           weather = weatherData;

            adapter = new Adapter(hourlyList, this, unit, weatherData);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            setRecyclerViewData(weatherData.getHourly());

            progressBar.setVisibility(View.GONE);
        }
    }

    public void downloadFailed() {
        hourlyList.clear();
        adapter.notifyItemRangeChanged(0, hourlyList.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeatherData(Weather weatherData) {
        String pattern = "EEE MMM dd h:mm a, yyyy";
        String sunrisePattern = "h:mm a";

        Current current = weatherData.getCurrent();
        Daily daily = weatherData.getDaily().get(0);

        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(current.getDt() + weatherData.getTimezoneOffset(), 0, ZoneOffset.UTC);
        LocalDateTime sunriseDt =
                LocalDateTime.ofEpochSecond(current.getSunrise() + weatherData.getTimezoneOffset(), 0, ZoneOffset.UTC);
        LocalDateTime sunsetDt =
                LocalDateTime.ofEpochSecond(current.getSunset() + weatherData.getTimezoneOffset(), 0, ZoneOffset.UTC);

        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
        DateTimeFormatter sunrisetf =
                DateTimeFormatter.ofPattern(sunrisePattern, Locale.getDefault());


        String iconCode = "_" + current.getWeather().get(0).getIcon();
        int iconResId = getResources().getIdentifier(iconCode,
                "drawable",
                getPackageName());

        location.setText(weatherData.getTimezone());
        dateTime.setText(ldt.format(dtf));
        temperature.setText(String.format("%s%s", current.temp, weatherData.formatUnit(unit)));
        description.setText(String.format("Feels like %s%s", current.feelsLike, weatherData.formatUnit(unit)));
        clouds.setText(String.format("%s (%s Clouds)", weatherData.formatClouds(current.getWeather().get(0).getDescription()), current.getClouds() +"%"));
        winds.setText(String.format("Winds: %s at %s%s", getDirection(current.getWindDeg()), current.getWindSpeed(), formatWindSpeed()));
        humidity.setText(String.format("Humidity: %s%%", current.humidity));
        uvi.setText(String.format("UV Index: %s", current.getUvi()));
        visibility.setText(String.format("Visibility: %s", current.getVisibility()));
        day.setText(String.format("%s%s", daily.getTemp().getMorn(), weatherData.formatUnit(unit)));
        noon.setText(String.format("%s%s", daily.getTemp().getDay(), weatherData.formatUnit(unit)));
        evening.setText(String.format("%s%s", daily.getTemp().getEve(), weatherData.formatUnit(unit)));
        night.setText(String.format("%s%s", daily.getTemp().getNight(), weatherData.formatUnit(unit)));
        weatherIcon.setImageResource(iconResId);

        dayTime.setText(String.format("%s", "8am"));
        noonTime.setText(String.format("%s", "1pm"));
        eveningTime.setText(String.format("%s", "5pm"));
        nightTime.setText(String.format("%s", "11pm"));

        sunrise.setText(String.format("Sunrise: %s", sunriseDt.format(sunrisetf)));
        sunset.setText(String.format("Sunset: %s", sunsetDt.format(sunrisetf)));
    }

    private void setRecyclerViewData(List<Hourly> h) {
        hourlyList.addAll(h);
        adapter.notifyItemRangeChanged(0, h.size());
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    private String formatWindSpeed() {
        return unit.equals("metric") ? "kmph" : "mph";
    }
}