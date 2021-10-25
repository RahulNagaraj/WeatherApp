package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    private MenuItem c;
    private MenuItem f;
    private final List<Hourly> hourlyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Adapter adapter;
    private String unit;
    private Weather weather = new Weather();
    private double[] latLon = new double[] {41.8675766, -87.616232};
    private String locale = "";
    private SwipeRefreshLayout swiper;
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPref;


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

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAllFields();

        recyclerView = findViewById(R.id.hourlyTemp);
        progressBar = findViewById(R.id.progressBar);

        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(this::onRefresh);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        unit = sharedPref.getString(getString(R.string.unit_shared_prefs), getString(R.string.metric));
    }

    @Override
    protected void onResume() {
        getLatestData();
        super.onResume();
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
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        c = menu.findItem(R.id.unit_c);
        f = menu.findItem(R.id.unit_f);

        if (unit.equals("metric")) {
            c.setVisible(false);
            f.setVisible(true);
        } else {
            c.setVisible(true);
            f.setVisible(false);
        }

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
            unit = getString(R.string.metric);

            editor = sharedPref.edit();
            if (!sharedPref.contains(getString(R.string.unit_shared_prefs))) {
                editor.putString(getString(R.string.unit_shared_prefs), unit);
                editor.apply();
            }

            getLatestData();

            return true;
        } else if (item.getItemId() == R.id.unit_f) {
            item.setVisible(false);
            c.setVisible(true);
            unit = getString(R.string.imperial);

            editor = sharedPref.edit();
            if (!sharedPref.contains(getString(R.string.unit_shared_prefs))) {
                editor.putString(getString(R.string.unit_shared_prefs), unit);
                editor.apply();
            }

            getLatestData();

            return true;
        } else if (item.getItemId() == R.id.daily) {
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
            intent.putExtra(getString(R.string.unit_shared_prefs), unit);
            intent.putExtra("locale", locale);
            startActivity(intent);

            return true;
        } else {
            openDialog();
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
            swiper.setRefreshing(false);
        }
    }

    public void downloadFailed() {
        hourlyList.clear();
        adapter.notifyItemRangeChanged(0, hourlyList.size());
        swiper.setRefreshing(false);
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

        String locale = getLocationName(weatherData.getLat(), weatherData.getLon());
        if (locale != null) {
            location.setText(locale);
            dateTime.setText(ldt.format(dtf));
            temperature.setText(String.format("%s%s", current.temp, weatherData.formatUnit(unit)));
            description.setText(String.format("Feels like %s%s", current.feelsLike, weatherData.formatUnit(unit)));
            clouds.setText(String.format("%s (%s Clouds)", weatherData.formatClouds(current.getWeather().get(0).getDescription()), current.getClouds() +"%"));
            winds.setText(String.format("Winds: %s at %s %s", getDirection(current.getWindDeg()), current.getWindSpeed(), formatWindSpeed()));
            humidity.setText(String.format("Humidity: %s%%", current.humidity));
            uvi.setText(String.format("UV Index: %s", current.getUvi()));
            visibility.setText(String.format("Visibility: %s", weatherData.formatVisibility(current.getVisibility(), unit)));
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
        } else {
            Toast.makeText(this, "No such city/country exits", Toast.LENGTH_SHORT).show();
        }
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
        return unit.equals(getString(R.string.metric)) ? "kmph" : "mph";
    }

    private String getLocationName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocation(lat, lon, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country == null) return null;
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            // Failure to get an Address object
            Toast.makeText(this, "No such city/country exits", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);

        final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("For US location, enter as 'City' or 'City, State' \n \n For international location, enter as 'City, Country' ");
        builder.setTitle("Enter a location");

        // Set the inflated view to be the builder's view
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, id) -> {

            // Multiply the 2 values together and display the results
            EditText et1 = view.findViewById(R.id.locationName);
            double[] l = getLatLon(et1.getText().toString());

            if (l != null) {
                latLon = l;
                getLatestData();
            } else {
                Toast.makeText(this, "Invalid city/state", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        double lat, lon;
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            lat = address.get(0).getLatitude();
            lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            Log.d(TAG, "getLatLon: " + e.getMessage());
            Toast.makeText(this, "Invalid city/state", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void getLatestData() {
        if (hasNetworkConnection()) {
            progressBar.setVisibility(View.VISIBLE);

            WeatherAPI weatherAPI = new WeatherAPI(this, latLon, unit);
            new Thread(weatherAPI).start();
        } else {
            location.setText("");
            dateTime.setText(String.format("%s", getString(R.string.no_network_connection)));
            temperature.setText("");
            description.setText("");
            clouds.setText("");
            winds.setText("");
            humidity.setText("");
            uvi.setText("");
            visibility.setText("");
            day.setText("");
            noon.setText("");
            evening.setText("");
            night.setText("");
            weatherIcon.setImageDrawable(null);
            dayTime.setText("");
            noonTime.setText("");
            eveningTime.setText("");
            nightTime.setText("");
            sunrise.setText("");
            sunset.setText("");
            hourlyList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void onRefresh() {
        getLatestData();
    }

    private void openCalendar() {
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, Calendar.getInstance().getTimeInMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        openCalendar();
    }
}