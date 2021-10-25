package com.example.weatherapp;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Hourly> hourlyList;
    private MainActivity mainAct;
    private String unit;
    private Weather weather;

    public Adapter() {}

    public Adapter(List<Hourly> hourlyList, MainActivity mainActivity, String unit, Weather weather) {
        this.hourlyList = hourlyList;
        mainAct = mainActivity;
        this.unit = unit;
        this.weather = weather;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_card, parent, false);

        itemView.setOnClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);

        String pattern = "HH:MM a";
        String timePattern = "EEEE";

        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(hourly.getDt() + weather.getTimezoneOffset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault());

        DateTimeFormatter tf =
                DateTimeFormatter.ofPattern(timePattern, Locale.getDefault());

        holder.day.setText(ldt.format(tf));
        holder.time.setText(ldt.format(dtf));
        holder.temperature.setText(String.format("%s%s", hourly.getTemp(), weather.formatUnit(unit)));


        String iconCode = "_" + hourly.getWeather().get(0).getIcon();
        int iconResId = mainAct.getResources().getIdentifier(iconCode,
                "drawable",
                mainAct.getPackageName());
        holder.imageView.setImageResource(iconResId);

        holder.clouds.setText(weather.formatClouds(hourly.getWeather().get(0).getDescription()));
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }
}
