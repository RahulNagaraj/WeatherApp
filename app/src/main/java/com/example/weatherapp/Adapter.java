package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {
    private final List<Hourly> hourlyList;
    private final MainActivity mainActivity;

    public Adapter(List<Hourly> hourlyList, MainActivity mainActivity) {
        this.hourlyList = hourlyList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_hourly_weather, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);

        String pattern = "HH:MM a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String time = simpleDateFormat.format(hourly.dt);

        holder.day.setText("Today");
        holder.time.setText(time);
        holder.temperature.setText("" + hourly.getTemp());

        setImage(holder, hourly);

        holder.clouds.setText(formatClouds(hourly.getWeather().get(0).getDescription()));
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    private void setImage(MyViewHolder holder, Hourly hourly) {
        String icon = hourly.getWeather().get(0).getIcon();
        if (icon.equals("01d"))
            holder.imageView.setImageResource(R.drawable._01d);
        else if (icon.equals("01n"))
            holder.imageView.setImageResource(R.drawable._01n);
        else if (icon.equals("02d"))
            holder.imageView.setImageResource(R.drawable._02d);
        else if (icon.equals("02n"))
            holder.imageView.setImageResource(R.drawable._02n);
        else if (icon.equals("03d"))
            holder.imageView.setImageResource(R.drawable._03d);
        else if (icon.equals("03n"))
            holder.imageView.setImageResource(R.drawable._03n);
        else if (icon.equals("04d"))
            holder.imageView.setImageResource(R.drawable._04d);
        else if (icon.equals("04n"))
            holder.imageView.setImageResource(R.drawable._04n);
        else if (icon.equals("09d"))
            holder.imageView.setImageResource(R.drawable._09d);
        else if (icon.equals("10d"))
            holder.imageView.setImageResource(R.drawable._10d);
        else if (icon.equals("10n"))
            holder.imageView.setImageResource(R.drawable._10n);
        else if (icon.equals("11d"))
            holder.imageView.setImageResource(R.drawable._11d);
        else if (icon.equals("11n"))
            holder.imageView.setImageResource(R.drawable._11n);
        else if (icon.equals("13d"))
            holder.imageView.setImageResource(R.drawable._13d);
        else if (icon.equals("13n"))
            holder.imageView.setImageResource(R.drawable._13n);
        else if (icon.equals("50d"))
            holder.imageView.setImageResource(R.drawable._50d);
        else if (icon.equals("50n"))
            holder.imageView.setImageResource(R.drawable._50n);
    }

    private String formatClouds(String text) {
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
}
