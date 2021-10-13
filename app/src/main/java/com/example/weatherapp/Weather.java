package com.example.weatherapp;

import java.util.List;

public class Weather {
    double lat;
    double lon;
    String timezone;
    long timezoneOffset;
    WeatherData current;
    List<WeatherData> hourly;
    List<WeatherData> daily;

    public Weather() {
    }

    public Weather(double lat, double lon, String timezone, long timezoneOffset, WeatherData current, List<WeatherData> hourly, List<WeatherData> daily) {
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

    public WeatherData getCurrent() {
        return current;
    }

    public void setCurrent(WeatherData current) {
        this.current = current;
    }

    public List<WeatherData> getHourly() {
        return hourly;
    }

    public void setHourly(List<WeatherData> hourly) {
        this.hourly = hourly;
    }

    public List<WeatherData> getDaily() {
        return daily;
    }

    public void setDaily(List<WeatherData> daily) {
        this.daily = daily;
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
