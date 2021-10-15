package com.example.weatherapp;

import java.util.List;

public class Daily {
    long dt;
    Temperature temp;
    List<WeatherInfo> weather;
    int pop;
    double uvi;

    public Daily() {
    }

    public Daily(long dt, Temperature temp, List<WeatherInfo> weather, int pop, double uvi) {
        this.dt = dt;
        this.temp = temp;
        this.weather = weather;
        this.pop = pop;
        this.uvi = uvi;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
}
