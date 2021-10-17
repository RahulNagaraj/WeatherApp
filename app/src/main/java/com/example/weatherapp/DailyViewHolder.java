package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DailyViewHolder extends RecyclerView.ViewHolder {
    TextView dailyDay;
    TextView highLow;
    TextView cloudsDescription;
    TextView precipitation;
    TextView uvIndex;
    ImageView dailyCloudImage;
    TextView dailyMorning;
    TextView dailyNoon;
    TextView dailyEvening;
    TextView dailyNight;

    public DailyViewHolder(View view) {
        super(view);
        dailyDay = view.findViewById(R.id.dailyDay);
        highLow = view.findViewById(R.id.highLow);
        cloudsDescription = view.findViewById(R.id.cloudsDescription);
        precipitation = view.findViewById(R.id.precipitation);
        uvIndex = view.findViewById(R.id.uvIndex);
        dailyCloudImage = view.findViewById(R.id.dailyCloudImage);
        dailyMorning = view.findViewById(R.id.dailyMorning);
        dailyNoon = view.findViewById(R.id.dailyNoon);
        dailyEvening = view.findViewById(R.id.dailyEvening);
        dailyNight = view.findViewById(R.id.dailyNight);
    }
}
