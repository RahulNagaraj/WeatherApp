package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView day;
    TextView time;
    ImageView imageView;
    TextView temperature;
    TextView clouds;

    public MyViewHolder(@NonNull View itemView, TextView day, TextView time, ImageView imageView, TextView temperature, TextView clouds) {
        super(itemView);
        this.day = day;
        this.time = time;
        this.imageView = imageView;
        this.temperature = temperature;
        this.clouds = clouds;
    }

    public MyViewHolder(View view) {
        super(view);
        day = view.findViewById(R.id.day);
        time = view.findViewById(R.id.time);
        imageView = view.findViewById(R.id.cloudsImage);
        temperature = view.findViewById(R.id.temperature);
        clouds = view.findViewById(R.id.clouds);
    }
}
