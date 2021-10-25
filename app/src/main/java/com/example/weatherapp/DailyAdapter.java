package com.example.weatherapp;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DailyAdapter extends RecyclerView.Adapter<DailyViewHolder> {
    private final List<Daily> dailyList;
    private DailyActivity dayAct;
    private Weather weather;
    private String unit;

    public DailyAdapter(List<Daily> dailyList, DailyActivity dailyActivity, String unit, Weather weather) {
        this.dailyList = dailyList;
        dayAct = dailyActivity;
        this.unit = unit;
        this.weather = weather;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_card, parent, false);

        return new DailyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        Daily daily = dailyList.get(position);
        String pattern = "EEEE, MM/dd";

        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(daily.getDt() + weather.getTimezoneOffset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault());

        String iconCode = "_" + daily.getWeather().get(0).getIcon();
        int iconResId = dayAct.getResources().getIdentifier(
                iconCode,
                "drawable",
                dayAct.getPackageName()
        );

        holder.dailyDay.setText(ldt.format(dtf));
        holder.highLow.setText(String.format("%s / %s", daily.getTemp().getMax() + weather.formatUnit(unit),
                daily.getTemp().getMin() + weather.formatUnit(unit)));
        holder.cloudsDescription.setText(daily.getWeather().get(0).getDescription());
        holder.precipitation.setText(String.format("Precipitation: %s", daily.getPop()));
        holder.uvIndex.setText(String.format("UV Index: %s", daily.getUvi()));
        holder.dailyCloudImage.setImageResource(iconResId);
        holder.dailyMorning.setText(String.format("%s%s", daily.getTemp().getMorn(), weather.formatUnit(unit)));
        holder.dailyNoon.setText(String.format("%s%s", daily.getTemp().getDay(), weather.formatUnit(unit)));
        holder.dailyEvening.setText(String.format("%s%s", daily.getTemp().getEve(), weather.formatUnit(unit)));
        holder.dailyNight.setText(String.format("%s%s", daily.getTemp().getNight(), weather.formatUnit(unit)));

    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }
}
