package com.example.weatherapp;

import java.util.List;

public class Hourly {
    long dt;
    int temp;
    List<WeatherInfo> weather;
    double pop;

    public Hourly() {
    }

    public Hourly(long dt, int temp, List<WeatherInfo> weather, double pop) {
        this.dt = dt;
        this.temp = temp;
        this.weather = weather;
        this.pop = pop;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    @Override
    public String toString() {
        return "Hourly{" +
                "dt=" + dt +
                ", temp=" + temp +
                ", weather=" + weather +
                '}';
    }
}
