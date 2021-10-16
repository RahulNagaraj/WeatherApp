package com.example.weatherapp;

public class Temperature {
    int day;
    int min;
    int max;
    int night;
    int eve;
    int morn;

    public Temperature() {
    }

    public Temperature(int day, int min, int max, int night, int eve, int morn) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getNight() {
        return night;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public int getEve() {
        return eve;
    }

    public void setEve(int eve) {
        this.eve = eve;
    }

    public int getMorn() {
        return morn;
    }

    public void setMorn(int morn) {
        this.morn = morn;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "day=" + day +
                ", min=" + min +
                ", max=" + max +
                ", night=" + night +
                ", eve=" + eve +
                ", morn=" + morn +
                '}';
    }
}
