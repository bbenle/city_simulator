package com.example.citysimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsScreen extends AppCompatActivity
{
    // private class fields
    private EditText editMapHeight;
    private EditText editMapWidth;
    private EditText editInitialMoney;
    private EditText editCityName;
    private Button backButton;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);


        editMapHeight = (EditText) findViewById(R.id.editMapHeight);
        editMapWidth = (EditText) findViewById(R.id.editMapWidth);
        editInitialMoney = (EditText) findViewById(R.id.editInitialMoney);
        editCityName = (EditText) findViewById(R.id.editCityName);
        backButton = (Button) findViewById(R.id.backButton);
        applyButton = (Button) findViewById(R.id.applyButton);

        // shows the text of the width, height, money and city name
        displayValues();

        // back button for returning to main menu
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish(); // goes back to previous activity
            }
        });

        // apply button onClick
        applyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // set the city name to whatever the user enters
                GameData.get().setCityName(editCityName.getText().toString());

                // user can only have a map of height of 1-20 and width of 10-50
                // user can only enter a money amount between 1,000 and 50,000
                if ((Integer.parseInt(editMapHeight.getText().toString()) >= 1 && (Integer.parseInt(editMapHeight.getText().toString()) <= 20) &&
                    (Integer.parseInt(editMapWidth.getText().toString()) >= 10 && (Integer.parseInt(editMapWidth.getText().toString()) <= 50) &&
                    (Integer.parseInt(editInitialMoney.getText().toString()) >= 1000) && (Integer.parseInt(editInitialMoney.getText().toString()) <= 50000))))
                {
                    // this changes the variables to what has been entered on the settings screen
                    GameData.get().getSettings().setMapHeight(Integer.parseInt(editMapHeight.getText().toString())); // set height to what user enters
                    GameData.get().getSettings().setMapWidth(Integer.parseInt(editMapWidth.getText().toString())); // set width to what user enters
                    GameData.get().setMoney(Integer.parseInt(editInitialMoney.getText().toString())); // set money to what user enters

                    GameData.get().dropTables(getApplicationContext()); // drops gameData and gameElement tables

                    GameData.get().regenerateGrid(); // regenerates the grid
                    GameData.get().addSettings(GameData.get().getSettings()); // update settings database

                    // send a toast saying changes applied and previous progress is lost (because database is wiped)
                    Toast toast = Toast.makeText(getApplicationContext(), "Applying changes\nAny previous progress is lost", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();

                    finish();
                }
                else
                {
                    // send a toast saying user can only have a map of height of 1-20 and width of 10-50
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Height must be between 1 and 20\nWidth must be between 10 and 50\nMoney must be between 1,000 - 50,000", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    // method to display values of the height, width, money and city name
    public void displayValues()
    {
        editMapHeight.setText(String.valueOf(GameData.get().getSettings().getMapHeight()));
        editMapWidth.setText(String.valueOf(GameData.get().getSettings().getMapWidth()));
        editInitialMoney.setText(String.valueOf(GameData.get().getMoney()));
        editCityName.setText(GameData.get().getCityName());
    }
}