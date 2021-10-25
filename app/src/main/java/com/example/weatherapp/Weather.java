package com.example.weatherapp;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Weather implements Serializable {
    double lat;
    double lon;
    String timezone;
    long timezoneOffset;
    Current current;
    List<Hourly> hourly;
    List<Daily> daily;

    public Weather() {}

    public Weather(double lat, double lon, String timezone, long timezoneOffset, Current current, List<Hourly> hourly, List<Daily> daily) {
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezoneOffset = timezoneOffset;
        this.current = current;
        this.hourly = hourly;
        this.daily = daily;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(long timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public String formatUnit(String unit) {
        return unit.equals("metric") ? "°C" : "°F";
    }

    public String formatClouds(String text) {
        String WORD_SEPARATOR = " ";
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(WORD_SEPARATOR))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(WORD_SEPARATOR));
    }

    public String formatVisibility(long visibility, String unit) {
        if (unit.equalsIgnoreCase("metric")) {
            String vis = String.format("%.1f", visibility * 0.001);
            return String.format("%s km", vis);
        } else {
            String vis = String.format("%.1f", visibility * 0.000621371);
            return String.format("%s mi", vis);
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", timezoneOffset=" + timezoneOffset +
                ", current=" + current +
                ", hourly=" + hourly +
                ", daily=" + daily +
                '}';
    }
}
