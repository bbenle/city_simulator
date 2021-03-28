package com.example.citysimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    // private class fields
    private TextView title, myName;
    private Button mapButton, settings, restartButton;
    private final int REQUEST_CODE = 0000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        myName = (TextView) findViewById(R.id.myName);
        mapButton = (Button) findViewById(R.id.mapButton);
        settings = (Button) findViewById(R.id.settingsButton);
        restartButton = (Button) findViewById(R.id.restartButton);

        GameData.get();
        GameData.get().loadSettings(getApplicationContext()); // settings database
        GameData.get().loadGameData(getApplicationContext()); // gameData database
        GameData.get().loadGameElement(getApplicationContext()); // gameElement database

        // map activity
        mapButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // create new intent and 
                Intent intent = new Intent(MainActivity.this, MapScreen.class);
                startActivity(intent);

                Toast toast = Toast.makeText(getApplicationContext(), "Resuming Game", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        // settings activity
        settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SettingsScreen.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // restart activity
        restartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameData.get().deleteDatabase(); // deletes all the databases

                GameData.get().loadSettings(getApplicationContext()); // settings database
                GameData.get().loadGameData(getApplicationContext()); // gameData database
                GameData.get().loadGameElement(getApplicationContext()); // gameElement database

                Toast toast = Toast.makeText(getApplicationContext(), "Starting a New Game\nClick Resume to Start", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (requestCode == REQUEST_CODE)
            GameData.get().loadGameElement(getApplicationContext());
    }
}