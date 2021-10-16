package com.example.weatherapp;

import java.util.List;

public class Daily {
    long dt;
    Temperature temp;
    List<WeatherInfo> weather;
    double pop;
    int uvi;

    public Daily() {
    }

    public Daily(long dt, Temperature temp, List<WeatherInfo> weather, double pop, int uvi) {
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

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }
}
