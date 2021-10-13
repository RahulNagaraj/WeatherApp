package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private MenuItem c;
    private MenuItem f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherAPI weatherAPI = new WeatherAPI(this, "metric");
        new Thread(weatherAPI).start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        c = menu.findItem(R.id.unit_c);
        f = menu.findItem(R.id.unit_f);

        c.setVisible(true);
        f.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.unit_c) {
//            isChecked = !item.isChecked();
            item.setVisible(false);
            f.setVisible(true);
            return true;
        } else if (item.getItemId() == R.id.unit_f) {
            item.setVisible(false);
            c.setVisible(true);
            return true;
        }
        return false;
    }

    public void downloadFailed() {
//        countryList.clear();
//        mAdapter.notifyItemRangeChanged(0, countryList.size());
    }
}