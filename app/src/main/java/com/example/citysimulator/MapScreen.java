package com.example.citysimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

// map screen activity
public class MapScreen extends AppCompatActivity
{
    // private class fields
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        SelectorFragment selector = (SelectorFragment) fm.findFragmentById(R.id.selector);
        MapFragment map = (MapFragment) fm.findFragmentById(R.id.map);
        StatusFragment status = (StatusFragment) fm.findFragmentById(R.id.status);

        if (selector == null)
        {
            selector = new SelectorFragment();
            fm.beginTransaction().add(R.id.selector, selector).commit();
        }

        if (map == null)
        {
            map = new MapFragment();
            fm.beginTransaction().add(R.id.map, map).commit();
        }

        if (status == null)
        {
            status = new StatusFragment();
            fm.beginTransaction().add(R.id.status, status).commit();
        }

        // objects being set go here
        map.setSelectorFragment(selector);
        map.setStatusFragment(status);
    }
}